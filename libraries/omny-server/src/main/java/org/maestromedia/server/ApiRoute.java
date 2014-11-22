package org.maestromedia.server;

import org.maestromedia.request.api.MaestroApi;
import org.maestromedia.routing.IRoute;

public class ApiRoute implements IRoute<MaestroApi> {

    final MaestroApi api;
    final String version;

    public ApiRoute(MaestroApi api, String version) {
        this.api = api;
        this.version = version;
    }

    @Override
    public String getPath() {
        return "/api/v"+version+api.getBasePath();
    }

    @Override
    public MaestroApi getObject() {
        return api;
    }
    
}
