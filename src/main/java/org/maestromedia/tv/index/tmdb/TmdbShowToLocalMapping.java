package org.maestromedia.tv.index.tmdb;

public class TmdbShowToLocalMapping extends TmdbShow {
    String localPath;

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }
}
