package org.maestromedia.folders;

import ca.omny.db.IDocumentQuerier;
import java.util.Collection;
import org.maestromedia.services.core.models.Playlist;

public class PlaylistMapper {

    public Collection<String> getPlaylists(IDocumentQuerier querier) {
        return querier.getKeysInRange("playlists", true);
    }

    public Playlist getPlaylist(String playlist, IDocumentQuerier querier) {
        String key = querier.getKey("playlists", playlist);
        return querier.get(key, Playlist.class);
    }

}
