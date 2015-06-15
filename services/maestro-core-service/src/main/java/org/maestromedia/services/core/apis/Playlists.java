package org.maestromedia.services.core.apis;

import javax.inject.Inject;
import org.maestromedia.request.api.ApiResponse;
import org.maestromedia.request.api.MaestroApi;
import org.maestromedia.request.management.RequestResponseManager;
import org.maestromedia.services.cores.mappers.PlaylistMapper;

public class Playlists implements MaestroApi {

    @Inject
    PlaylistMapper playlistMapper;
    
    @Override
    public String getBasePath() {
        return MaestroApiConstants.base+"/playlists/{playlist}";
    }

    @Override
    public String[] getVersions() {
        return MaestroApiConstants.versions;
    }

    @Override
    public ApiResponse getResponse(RequestResponseManager requestResponseManager) {
        String playlist = requestResponseManager.getPathParameter("playlist");
        if(playlist==null) {
            return new ApiResponse(playlistMapper.getPlaylists(), 200);
        }
        return new ApiResponse(playlistMapper.getPlaylist(playlist), 200);
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
