
package org.maestromedia.services.cores.mappers;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import javax.inject.Inject;
import org.maestromedia.services.core.models.Playlist;
import org.maestromedia.services.core.models.PlaylistEntry;

public class PlaylistFolderMapper {
    
    @Inject
    PlaylistMapper playlistMapper;
    
    public String getPath(String path) {
        String[] parts = path.split("/");
        if(parts.length==4) {
            Playlist playlist = playlistMapper.getPlaylist(parts[1]);
            Collection<PlaylistEntry> entries = playlist.getGroups().get(parts[2]);
            for(PlaylistEntry entry: entries) {
                if(entry.getName().equals(parts[3])) {
                    return entry.getPath()+"/"+entry.getName();
                }
            }
        }
        return null;
    }
    public Collection<String> getFiles(String path) {
        String[] parts = path.split("/");
        if(parts.length==3) {
            Playlist playlist = playlistMapper.getPlaylist(parts[1]);
            Collection<PlaylistEntry> entries = playlist.getGroups().get(parts[2]);
            Collection<String> result = new LinkedList<>();
            for(PlaylistEntry entry: entries) {
                result.add(entry.getName());
            }
            return result;
        }
        return new LinkedList<String>();
    }
    
    public Collection<String> getFolders(String path) {
        String[] parts = path.split("/");
        if(parts.length==1) {
            return playlistMapper.getPlaylists();
        }
        Playlist playlist = playlistMapper.getPlaylist(parts[1]);
        if(parts.length==2) {
            return playlist.getGroups().keySet();
        }
        
        return new LinkedList<String>();
    }
}
