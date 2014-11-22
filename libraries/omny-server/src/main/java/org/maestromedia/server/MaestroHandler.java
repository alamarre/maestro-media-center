package org.maestromedia.server;

import org.maestromedia.request.api.ApiResponse;
import org.maestromedia.request.management.RequestResponseManager;
import org.maestromedia.request.api.MaestroApi;
import org.maestromedia.routing.IRoute;
import org.maestromedia.routing.RoutingTree;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.HashMap;
import javax.enterprise.inject.Instance;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

public class MaestroHandler extends AbstractHandler {

    Instance<MaestroApi> apis;
    RoutingTree<MaestroApi> router;
    Weld weld;
    WeldContainer container;
    
    Gson gson = new Gson();
    
    public MaestroHandler() {
        router = new RoutingTree<MaestroApi>("");
        weld = new Weld();
        container = weld.initialize();
        apis = container.instance().select(MaestroApi.class);
        for(MaestroApi api: apis) {
            for(String version: api.getVersions()) {
                ApiRoute route = new ApiRoute(api,version);
                router.addRoute(route);
            }
        }  
    }
     
    @Override
    public void handle(String string, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(!request.getRequestURI().startsWith("/api")) {
            return;
        }
        baseRequest.setHandled(true);
        RequestResponseManager requestResponseManager = new RequestResponseManager();
        requestResponseManager.setRequest(request);
        requestResponseManager.setResponse(response);
        IRoute<MaestroApi> route = router.matchPath(request.getRequestURI());
        if(route==null) {
            response.setStatus(404);
            return;
        }
        MaestroApi api = route.getObject();
        injectPathParameters(request, api, requestResponseManager);
        ApiResponse apiResponse;
        switch(request.getMethod().toLowerCase()) {
            case "post":
               apiResponse = api.postResponse(requestResponseManager); 
               break;
            case "put":
               apiResponse = api.putResponse(requestResponseManager); 
               break;
            case "delete":
               apiResponse = api.deleteResponse(requestResponseManager); 
               break;
            default:
                apiResponse = api.getResponse(requestResponseManager);
        }
                
        response.setStatus(apiResponse.getStatusCode());
        
        if(apiResponse.shouldEncodeAsJson()) {
            response.setHeader("Content-Type", "application/json");
            response.getWriter().write(gson.toJson(apiResponse.getResponse()));
        } else {
            Object responseObject = apiResponse.getResponse();
            if(responseObject instanceof byte[]) {
                IOUtils.write((byte[])responseObject, response.getOutputStream());
            } else {
                response.getWriter().write(responseObject.toString());
            }
            
        }
    }
    
    private void injectPathParameters(HttpServletRequest request, MaestroApi api, RequestResponseManager requestResponseManager) {
        String requestURI = request.getRequestURI();
        String[] parts = requestURI.split("/");
        // /api/v{{version}} is added before the base path
        int offset = 2;
        String[] apiParts = api.getBasePath().split("/");
        HashMap<String,String> parameters = new HashMap<>();
        for(int i=0; i<apiParts.length && i+offset<parts.length; i++) {
            if(apiParts[i].startsWith("{")) {
                String parameterName = apiParts[i].substring(1,apiParts[i].length()-1);
                parameters.put(parameterName, parts[i+offset]);
            }
            
        }
        requestResponseManager.setPathParameters(parameters);
    }
    
}
