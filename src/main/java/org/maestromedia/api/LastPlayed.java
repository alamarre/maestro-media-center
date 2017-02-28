package org.maestromedia.api;

import ca.omny.request.ApiResponse;
import ca.omny.request.OmnyApi;
import ca.omny.request.RequestResponseManager;
import org.maestromedia.users.ProfileMapper;

public class LastPlayed implements OmnyApi {

    @Override
    public String getBasePath() {
        return MaestroApiConstants.base +"/LastPlayed";
    }

    @Override
    public String[] getVersions() {
        return MaestroApiConstants.versions;
    }

    @Override
    public ApiResponse getResponse(RequestResponseManager rrm) {
        String showId = rrm.getQueryStringParameter("showId");
        ProfileMapper lastPlayed = new ProfileMapper();
        if(showId != null) {
            return new ApiResponse(lastPlayed.getLastPlayed(null, showId, rrm.getDatabaseQuerier()), 200);
        }
        
        return new ApiResponse(lastPlayed.getLastPlayedList(null, rrm.getDatabaseQuerier()), 200);
    }

    @Override
    public ApiResponse postResponse(RequestResponseManager rrm) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ApiResponse putResponse(RequestResponseManager rrm) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ApiResponse deleteResponse(RequestResponseManager rrm) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
