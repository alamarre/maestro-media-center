package org.maestromedia.routing;

public class Route implements IRoute<String> {
    
    String path;

    public Route(String path) {
        this.path = path;
    }

    @Override
    public String getPath() {
        return path;
    } 

    @Override
    public String getObject() {
        return path;
    }
}
