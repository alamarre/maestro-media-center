package org.maestromedia.services.cores.mappers;

import java.util.Collection;
import java.util.Map;
import javax.inject.Inject;
import org.maestromedia.documentdb.IDocumentQuerier;
import org.maestromedia.services.core.models.Playlist;

public class PlaylistMapper {

    @Inject
    IDocumentQuerier querier;
    
    public Collection<String> getPlaylists() {
        return querier.getKeysInRange("playlists", true);
    }
    
    public Playlist getPlaylist(String playlist) {
        String key = querier.getKey("playlists",playlist);
        return querier.get(key, Playlist.class);
    }
   
}
