package org.maestromedia.folders;

import ca.omny.db.IDocumentQuerier;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.maestromedia.tv.index.EpisodeHelper;
import org.maestromedia.tv.index.EpisodeInfo;
import org.maestromedia.tv.index.TvShowMapper;

public class TvShowProvider implements IVideoProvider {
    TvShowMapper tvShowMapper;

    public TvShowProvider(TvShowMapper tvShowMapper) {
        this.tvShowMapper = tvShowMapper;
    }

    @Override
    public String getName() {
        return "TV Shows";
    }

    @Override
    public String getPath(String path, IDocumentQuerier querier) {
        if(path == null || path.isEmpty()) {
            return null;
        }
        if(path.startsWith("/")) {
            path = path.substring(1);
        }
        
        String[] parts = path.split("/");
        if(parts.length != 3) {
            return path;
        }
        
        String showId = tvShowMapper.getShowIdFromName(parts[0], querier);
        int season = Integer.parseInt(parts[1]);
        int episode = EpisodeHelper.getEpisodeNumber(parts[2]);
        return tvShowMapper.getEpisodeLocation(showId, season, episode, querier);      
    }
    
    public TvShowMapper getTvShowMapper() {
        return tvShowMapper;
    }
    
    public EpisodeInfo getInfoFromPath(String path, IDocumentQuerier querier) {
        if(path == null || path.isEmpty()) {
            return null;
        }
        if(path.startsWith("/")) {
            path = path.substring(1);
        }
        
        String[] parts = path.split("/");
        if(parts.length != 3) {
            return null;
        }
        
        String showId = tvShowMapper.getShowIdFromName(parts[0], querier);
        int season = Integer.parseInt(parts[1]);
        int episode = EpisodeHelper.getEpisodeNumber(parts[2]);
        EpisodeInfo fakeIt = new EpisodeInfo();
        fakeIt.setEpisode(episode);
        fakeIt.setSeason(season);
        fakeIt.setShowId(showId);
        fakeIt.setTitle(parts[2]);
        return fakeIt;
        //return tvShowMapper.getInfo(showId, season, episode, querier);
    }

    @Override
    public Map<String, Collection<String>> getFiles(String path, IDocumentQuerier querier) {
        if(path == null) {
            path = "";
        }
        if(path.startsWith("/")) {
            path = path.substring(1);
        }
        HashMap<String, Collection<String>> map = new HashMap<>();
        map.put("files", new LinkedList<>());
        map.put("folders", new LinkedList<>());
        String[] parts = path.split("/");
        if(parts.length > 0 && !path.isEmpty()) {
            String show = parts[0];
            String showId = tvShowMapper.getShowIdFromName(show, querier);
            if(parts.length > 1) {
                int season = Integer.parseInt(parts[1]);
                /*Collection<EpisodeInfo> episodes = tvShowMapper.getEpisodes(showId, season, querier);
                LinkedList<String> files = new LinkedList<>();
                for(EpisodeInfo e: episodes) {
                    files.add(e.getEpisode() +" - "+e.getTitle());
                }*/
                Collection<String> episodes = tvShowMapper.getEpisodes(showId, season, querier);
                LinkedList<String> files = new LinkedList<>();
                for(String e: episodes) {
                    files.add(e);
                }
                map.put("files", files);
            } else {
                Collection<Integer> seasons = tvShowMapper.getSeasons(showId, querier);
                LinkedList<String> files = new LinkedList<>();
                LinkedList<String> folders = new LinkedList<>();
                for(Integer s: seasons) {
                    folders.add(""+s);
                }
                map.put("folders", folders);
            }
        } else {
            map.put("folders", tvShowMapper.getShows(querier));
        } 
        
        return map;
    }
    
    
}
