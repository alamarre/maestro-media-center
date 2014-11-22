package org.maestromedia.all;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletHolder;
import org.maestromedia.services.cores.mappers.FolderMapper;

public class VideoHandler extends AbstractHandler {

    @Inject
    FolderMapper folderMapper;
    
    @Override
    public void handle(String url, Request rqst, HttpServletRequest hsr, HttpServletResponse hsr1) throws IOException, ServletException {
        if(url.startsWith("/videos/")) {
            try {
                String path = url.substring("/videos/".length());
                File location = folderMapper.getLocation(path);       
                
                String requestUrl = "/"+location.getName();
                rqst.setRequestURI(requestUrl);
                rqst.setPathInfo(requestUrl);
                DefaultServlet servlet = new DefaultServlet();
                ServletHolder holder=new ServletHolder(servlet);
                holder.doStart();
                holder.setInitParameter("resourceBase", location.getParent());
                holder.handle(rqst, rqst, hsr1);
                /*resourceHandler.setResourceBase(location.getParent());
                resourceHandler.handle(requestUrl, rqst, hsr, hsr1);*/
            } catch (Exception ex) {
                Logger.getLogger(VideoHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
