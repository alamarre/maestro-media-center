package org.maestromedia.users;

import ca.omny.db.IDocumentQuerier;
import java.util.Collection;
import org.maestromedia.tv.index.EpisodeInfo;

public class ProfileMapper {
    
    public EpisodeInfo getLastPlayed(String profile, String showId, IDocumentQuerier querier) {
        profile = normalize(profile);
        String key = querier.getKey("LAST_VIEW", profile, showId);
        return querier.get(key, EpisodeInfo.class);
    }
    
    public void recordLastPlayed(EpisodeInfo info, String profile, String showId, IDocumentQuerier querier) {
        profile = normalize(profile);
        String key = querier.getKey("LAST_VIEW", profile, showId);
        querier.set(key, info);
    }
    
    public Collection<EpisodeInfo> getLastPlayedList(String profile, IDocumentQuerier querier) {
        profile = normalize(profile);
        String key = querier.getKey("LAST_VIEW", profile);
        return querier.getRange(key, EpisodeInfo.class);
    }
    
    public String normalize(String profile) {
        if(profile == null || profile.isEmpty()) {
            return "default";
        }
        
        return profile;
    }
}
