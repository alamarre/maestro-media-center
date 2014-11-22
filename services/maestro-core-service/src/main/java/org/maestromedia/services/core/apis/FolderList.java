package org.maestromedia.services.core.apis;

import javax.inject.Inject;
import org.maestromedia.request.api.ApiResponse;
import org.maestromedia.request.api.MaestroApi;
import org.maestromedia.request.management.RequestResponseManager;
import org.maestromedia.services.cores.mappers.FolderMapper;


public class FolderList implements MaestroApi {
    
    @Inject
    FolderMapper folderMapper;

    @Override
    public String getBasePath() {
        return MaestroApiConstants.base+"/folders";
    }

    @Override
    public String[] getVersions() {
        return MaestroApiConstants.versions;
    }

    @Override
    public ApiResponse getResponse(RequestResponseManager requestResponseManager) {
        String path = requestResponseManager.getQueryStringParameter("path");
        if(path!=null && path.startsWith("/")) {
            path = path.substring(1);
        }
        return new ApiResponse(folderMapper.getFiles(path), 200);
    }

    @Override
    public ApiResponse postResponse(RequestResponseManager requestResponseManager) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ApiResponse putResponse(RequestResponseManager requestResponseManager) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ApiResponse deleteResponse(RequestResponseManager requestResponseManager) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

 
}
