
package org.maestromedia.folders;

import ca.omny.db.IDocumentQuerier;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.maestromedia.services.core.models.Playlist;
import org.maestromedia.services.core.models.PlaylistEntry;

public class PlaylistFolderMapper implements IVideoProvider {
    
    PlaylistMapper playlistMapper;

    public PlaylistFolderMapper(PlaylistMapper playlistMapper) {
        this.playlistMapper = playlistMapper;
    }
     
    @Override
    public String getPath(String path, IDocumentQuerier querier) {
        String[] parts = path.split("/");
        if(parts.length==4) {
            Playlist playlist = playlistMapper.getPlaylist(parts[1], querier);
            Collection<PlaylistEntry> entries = playlist.getGroups().get(parts[2]);
            for(PlaylistEntry entry: entries) {
                if(entry.getName().equals(parts[3])) {
                    return entry.getPath()+"/"+entry.getName();
                }
            }
        }
        return null;
    }
        
    public Collection<String> getFilesInFolder(String path, IDocumentQuerier querier) {
        String[] parts = path.split("/");
        if(parts.length==3) {
            Playlist playlist = playlistMapper.getPlaylist(parts[1], querier);
            Collection<PlaylistEntry> entries = playlist.getGroups().get(parts[2]);
            Collection<String> result = new LinkedList<>();
            for(PlaylistEntry entry: entries) {
                result.add(entry.getName());
            }
            return result;
        }
        return new LinkedList<String>();
    }
    
    public Collection<String> getFolders(String path, IDocumentQuerier querier) {
        String[] parts = path.split("/");
        if(parts.length==1) {
            return playlistMapper.getPlaylists(querier);
        }
        Playlist playlist = playlistMapper.getPlaylist(parts[1], querier);
        if(parts.length==2) {
            return playlist.getGroups().keySet();
        }
        
        return new LinkedList<String>();
    }

    @Override
    public String getName() {
        return "Playlists";
    }

    @Override
    public Map<String, Collection<String>> getFiles(String path, IDocumentQuerier querier) {
        Map<String, Collection<String>> files = new HashMap<>();
        files.put("files", this.getFilesInFolder(path, querier));
        files.put("folders", this.getFolders(path, querier));
        return files;
    }
}
