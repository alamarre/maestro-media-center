package org.maestromedia.all;

import java.io.File;
import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.util.resource.FileResource;
import org.eclipse.jetty.util.resource.Resource;
import org.maestromedia.services.cores.mappers.FolderMapper;

public class VideoServlet extends DefaultServlet {
    
    @Inject
    FolderMapper folderMapper;
    
    public VideoServlet() {
        
    }

    public void setFolderMapper(FolderMapper folderMapper) {
        this.folderMapper = folderMapper;
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doGet(request, response);
    }
    
    @Override
    public Resource getResource(String url) {
        String path = url.substring("/videos/".length());
        File location = folderMapper.getLocation(path);
        Resource r = new FileResource(location.toURI());
        return r;
    }
    
    @Override
    public String getInitParameter(String name) {
        return super.getInitParameter(name);     
    }
}
