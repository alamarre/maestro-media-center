package org.maestromedia.tv.index.api;

public interface ITvShowTracker {
    public void recordShowLocation(String path, String show);
    public boolean recordEpisode(String showId, int season, int episodeNumber, String path);
}
