package org.maestromedia.api;

import ca.omny.request.ApiResponse;
import ca.omny.request.OmnyApi;
import ca.omny.request.RequestResponseManager;
import org.maestromedia.folders.PlaylistMapper;

public class Playlists implements OmnyApi {

    PlaylistMapper playlistMapper;

    public Playlists(PlaylistMapper playlistMapper) {
        this.playlistMapper = playlistMapper;
    }
    
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
            return new ApiResponse(playlistMapper.getPlaylists(requestResponseManager.getDatabaseQuerier()), 200);
        }
        return new ApiResponse(playlistMapper.getPlaylist(playlist, requestResponseManager.getDatabaseQuerier()), 200);
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
