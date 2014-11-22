package org.maestromedia.routing;

public interface IRoute<T> {

    String getPath();
    
    public T getObject();
}
