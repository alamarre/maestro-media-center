package org.maestromedia;

import ca.omny.db.IDocumentQuerier;
import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.util.resource.FileResource;
import org.eclipse.jetty.util.resource.Resource;
import org.maestromedia.folders.FolderMapper;
import org.maestromedia.folders.TvShowProvider;
import org.maestromedia.tv.index.EpisodeInfo;
import org.maestromedia.tv.index.TvShowMapper;
import org.maestromedia.users.ProfileMapper;

public class VideoServlet extends DefaultServlet {
    
    FolderMapper folderMapper;
    IDocumentQuerier querier;
    ProfileMapper lastViewMapper;
    TvShowProvider tvShowProvider;

    public VideoServlet(FolderMapper folderMapper, TvShowProvider tvShowProvider, IDocumentQuerier querier) {
        this.folderMapper = folderMapper;
        this.tvShowProvider = tvShowProvider;
        this.querier = querier;
        lastViewMapper = new ProfileMapper();
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
        File location = folderMapper.getLocation(path, querier);
        if(path.startsWith("TV Shows") && path.endsWith(".mp4")) {
            EpisodeInfo infoFromPath = tvShowProvider.getInfoFromPath(path.substring("TV Shows".length()), querier);
            //infoFromPath.setShowName(tvShowProvider.getTvShowMapper().getName(infoFromPath.getShowId(), querier));
            String[] parts = path.split("/");
            
            infoFromPath.setShowName(parts[1]);
            lastViewMapper.recordLastPlayed(infoFromPath, null, infoFromPath.getShowId(), querier);
        }
        Resource r = new FileResource(location.toURI());
        return r;
    }
    
    @Override
    public String getInitParameter(String name) {
        return super.getInitParameter(name);     
    }
}
