package org.maestromedia.tv.index;

import ca.omny.db.IDocumentQuerier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;
import org.maestromedia.tv.index.api.ISearchIndexer;
import org.maestromedia.tv.index.api.ITvShowInfoProvider;
import org.maestromedia.tv.index.api.ITvShowTracker;

public class TvShowMapper implements ITvShowTracker, ITvShowInfoProvider {
    IDocumentQuerier querier;
    ISearchIndexer searchIndexer;

    public TvShowMapper(ISearchIndexer searchIndexer, IDocumentQuerier querier) {
        this.searchIndexer = searchIndexer;
        this.querier = querier;
    }

    public String getShowIdFromName(String name, IDocumentQuerier querier) {
        String key = querier.getKey("TV_SHOW_NAME", name);
        String result = querier.get(key, String.class);
        
        if(result == null) {
            if(querier.get(querier.getKey("TV_SHOW_IDS", name), String.class) != null) {
                return name;
            }
        }
        return result;
    }

    public String getShowId(String path) {
        String key = querier.getKey("TV_SHOWS", path);
        return querier.get(key, String.class);
    }
    
    public String getName(String showID, IDocumentQuerier querier) {
        String key = querier.getKey("TV_SHOW_IDS", showID);
        return querier.get(key, String.class);
    }

    @Override
    public void recordShowLocation(String path, String show) {
        String key = querier.getKey("TV_SHOWS", path);
        querier.set(key, show);
        
        key = querier.getKey("TV_SHOW_NAME", show);
        querier.set(key, show);

        searchIndexer.indexShowLocation(path, show);
    }

    /***
     * 
     * @param showId
     * @param season
     * @param episodeNumber
     * @param path
     * @param querier
     * @return Whether the show was added. False if already existed.
     */
    @Override
    public boolean recordEpisode(String showId, int season, int episodeNumber, String path) {
        String key = querier.getKey("TV_SHOW_EPISODES", showId, ""+season, ""+episodeNumber);
        if(querier.get(key, String.class) != null) {
            return false;
        }
        
        querier.set(key, path);
        
        return true;
    }
    
    public String getEpisodeLocation(String showId, int season, int episodeNumber, IDocumentQuerier querier) {
        String key = querier.getKey("TV_SHOW_EPISODES", showId, ""+season, ""+episodeNumber);
        return querier.get(key, String.class);
    }
    
    public void saveEpisodeInfo(EpisodeInfo episodeInfo) {
        String key = querier.getKey("TV_SHOW_EPISODE_INFO", episodeInfo.getShowId(), ""+episodeInfo.getSeason(), ""+episodeInfo.getEpisode());
        querier.set(key, episodeInfo);
    }
    
    public EpisodeInfo getInfo(String showId, int season, int episodeNumber) {
        String key = querier.getKey("TV_SHOW_EPISODE_INFO", showId, ""+season, ""+episodeNumber);
        return querier.get(key, EpisodeInfo.class);
    }

    public Collection<Integer> getSeasons(String showId, IDocumentQuerier querier) {
        ConcurrentSkipListSet<Integer> seasons = new ConcurrentSkipListSet<>() ;
        String key = querier.getKey("TV_SHOW_EPISODES", showId);
        Collection<String> keysInRange = querier.getKeysInRange(key, true);
        for(String seasonKey: keysInRange) {
            String seasonInfo = seasonKey.split("/")[0];
            int season = Integer.parseInt(seasonInfo);
            seasons.add(season);
        }
        return seasons;
    }
    
    public Collection<String> getEpisodes(String showId, int season, IDocumentQuerier querier) {
        String key = querier.getKey("TV_SHOW_EPISODES", showId, ""+season);
        
        Collection<String> episodes = querier.getRange(key, String.class)
            .stream()
            .map(name -> EpisodeHelper.getEpisodeName(name))
            .collect(Collectors.toCollection(ArrayList::new));
        
        EpisodeSorter sort = new EpisodeSorter();
        
        ArrayList<String> sortable = new ArrayList<>(episodes);
        Collections.sort(sortable, new StringNumberSorter());
        
        return sortable;
    }

    public Collection<EpisodeInfo> getEpisodesInfo(String showId, int season, IDocumentQuerier querier) {
        String key = querier.getKey("TV_SHOW_EPISODE_INFO", showId, ""+season);
        
        Collection<EpisodeInfo> episodes = querier.getRange(key, EpisodeInfo.class);
        EpisodeSorter sort = new EpisodeSorter();
        ArrayList<EpisodeInfo> sortable = new ArrayList<>(episodes);
        Collections.sort(sortable, sort);
        
        return sortable;
    }
    
    public Collection<String> getShows(IDocumentQuerier querier) {
        String key = querier.getKey("TV_SHOW_NAME");
        
        Collection<String> keysInRange = querier.getKeysInRange(key, false);
        return keysInRange;
    }
}
