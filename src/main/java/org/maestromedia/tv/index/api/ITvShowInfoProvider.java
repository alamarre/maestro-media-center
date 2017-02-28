package org.maestromedia.tv.index.api;

import org.maestromedia.tv.index.EpisodeInfo;

public interface ITvShowInfoProvider {
    public void saveEpisodeInfo(EpisodeInfo episodeInfo);
    public EpisodeInfo getInfo(String showId, int season, int episodeNumber);
}
