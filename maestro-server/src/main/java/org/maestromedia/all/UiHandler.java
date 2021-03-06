package org.maestromedia.all;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;

public class UiHandler extends AbstractHandler {
    
    String htmlFileContents;
    String contentRoot;
    ResourceHandler resourceHandler;

    public UiHandler(String contentRoot) {
        this.contentRoot = contentRoot;
        
        resourceHandler = new ResourceHandler();
        
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setWelcomeFiles(new String[]{ "index.html" });
        resourceHandler.setEtags(true);
        
        resourceHandler.setResourceBase(contentRoot);
    }
    
    @Override
    public void handle(String string, Request rqst, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        response.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept, Accept-Encoding, Range");
        response.addHeader("Access-Control-Max-Age", "1728000");
        
        String requestUrl = request.getRequestURI();
        if(requestUrl.startsWith("/version")) {
            requestUrl = requestUrl.substring(requestUrl.indexOf("/",1));
            requestUrl = requestUrl.substring(requestUrl.indexOf("/",1));
            rqst.setRequestURI(requestUrl);
            rqst.setPathInfo(requestUrl);
            
        }
        boolean ui = false;
        if(requestUrl.startsWith("/partial")) {
            ui = true;
        } else if(requestUrl.startsWith("/template")) {
            ui = true;
        } else if(requestUrl.startsWith("/js")) {
            ui = true;
        } else if (requestUrl.startsWith("/themes")) {
            String file = requestUrl.substring(1);
            String redirect = "/api/v1.0/content";
            String queryString = "file="+file+"&sendFile";
            rqst.setRequestURI(redirect);
            rqst.setQueryString(queryString);
        }  else if (requestUrl.startsWith("/global/themes")) {
            String file = requestUrl.substring(1);
            String redirect = "/api/v1.0/content";
            String queryString = "file="+file+"&sendFile";
            rqst.setRequestURI(redirect);
            rqst.setQueryString(queryString);
        }
        
        if(!ui&&requestUrl.endsWith(".html")) {
            rqst.setHandled(true);
            response.setHeader("Content-Type", "text/html");
            populateHtmlContents();
            response.getWriter().write(htmlFileContents);
        } else {
            resourceHandler.handle(requestUrl, rqst, request, response);
        }
    }
    
    public void populateHtmlContents() {
        try {
            htmlFileContents = FileUtils.readFileToString(new File(contentRoot+"/index.html"));
        } catch (IOException ex) {
            Logger.getLogger(UiHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
