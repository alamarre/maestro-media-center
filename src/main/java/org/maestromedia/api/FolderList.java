package org.maestromedia.api;

import ca.omny.request.ApiResponse;
import ca.omny.request.OmnyApi;
import ca.omny.request.RequestResponseManager;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.maestromedia.folders.FolderMapper;

public class FolderList implements OmnyApi {

    FolderMapper folderMapper;

    public FolderList(FolderMapper folderMapper) {
        this.folderMapper = folderMapper;
    }

    @Override
    public String getBasePath() {
        return MaestroApiConstants.base + "/folders";
    }

    @Override
    public String[] getVersions() {
        return MaestroApiConstants.versions;
    }

    @Override
    public ApiResponse getResponse(RequestResponseManager requestResponseManager) {
        try {
            String path = requestResponseManager.getQueryStringParameter("path");
            if (path != null) {
                path = java.net.URLDecoder.decode(path, "UTF-8");

                if (path.startsWith("/")) {
                    path = path.substring(1);
                }
            }
            
            return new ApiResponse(folderMapper.getFiles(path, requestResponseManager.getDatabaseQuerier()), 200);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(FolderList.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new ApiResponse("", 500);
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
