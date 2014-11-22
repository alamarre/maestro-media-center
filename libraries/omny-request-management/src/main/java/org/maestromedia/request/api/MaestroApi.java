package org.maestromedia.request.api;

import org.maestromedia.request.management.RequestResponseManager;

public interface MaestroApi {
    
    public String getBasePath();
    public String[] getVersions();
    public ApiResponse getResponse(RequestResponseManager requestResponseManager); 
    public ApiResponse postResponse(RequestResponseManager requestResponseManager); 
    public ApiResponse putResponse(RequestResponseManager requestResponseManager); 
    public ApiResponse deleteResponse(RequestResponseManager requestResponseManager); 
}
