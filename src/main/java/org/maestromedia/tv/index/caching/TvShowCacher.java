package org.maestromedia.tv.index.caching;

import ca.omny.db.IDocumentQuerier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import org.maestromedia.tv.index.TitleInfoMapper;
import org.maestromedia.tv.index.api.ITvShowTracker;

public class TvShowCacher implements ITvShowTracker {

    ITvShowTracker uncachedTracker;
    TitleInfoMapper titleInfoMapper;
    private HashSet<String> indexedPaths;
    IDocumentQuerier querier;
    HashMap<String, LinkedList<String>> showMap;

    public TvShowCacher(ITvShowTracker uncachedTracker, TitleInfoMapper titleInfoMapper, IDocumentQuerier querier) {
        this.uncachedTracker = uncachedTracker;
        this.titleInfoMapper = titleInfoMapper;
        showMap = new HashMap<>();
        
        indexedPaths = new HashSet<>();
    }

    @Override
    public void recordShowLocation(String path, String show) {
        if(showMap.containsKey(show)) {
            if(showMap.get(show).contains(path)) {
                return;
            } else {
                showMap.get(show).add(path);
            }
        } else {
            showMap.put(show, new LinkedList<String>(Arrays.asList(path)));
        }
        
        uncachedTracker.recordShowLocation(path, show);
    }

    @Override
    public boolean recordEpisode(String showId, int season, int episodeNumber, String path) {
        if(!indexedPaths.contains(path)) {
            indexedPaths.add(path);
            boolean result = uncachedTracker.recordEpisode(showId, season, episodeNumber, path);
            return result;
        }
        
        return false;
    }
    
}
