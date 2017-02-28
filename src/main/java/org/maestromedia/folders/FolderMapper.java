package org.maestromedia.folders;

import ca.omny.db.IDocumentQuerier;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class FolderMapper {

    Collection<IVideoProvider> videoProviders;
    FolderMapperProvider folderMapperProvider;

    public FolderMapper(Collection<IVideoProvider> videoProviders) {
        this.videoProviders = videoProviders;
        
        for(IVideoProvider provider: videoProviders) {
            if(provider.getName().equals("Browse") && provider instanceof FolderMapperProvider) {
                folderMapperProvider = (FolderMapperProvider)provider;
            }
        }
    }
    
    public File getLocation(String path, IDocumentQuerier querier) {
        String[] parts = path.split("/");
        if(parts.length ==0) {
            return null;
        }
        
        for(IVideoProvider provider: videoProviders) {
            if(parts[0].startsWith(provider.getName())) {
                path = path.substring(provider.getName().length());
                path = provider.getPath(path, querier);
                
                return folderMapperProvider.getLocation(path, querier);
            }
        } 
        
        return null;
    }

    public Map<String, Collection<String>> getFiles(String path, IDocumentQuerier querier) {
        if(path == null || path.isEmpty()) {
            Map<String, Collection<String>> files = new HashMap<>();
            LinkedList<String> folders = new LinkedList<>();
            for(IVideoProvider provider: videoProviders) {
                folders.add(provider.getName());
            }
            
            files.put("folders", folders);
            files.put("files", new LinkedList<String>());
            return files;
        }
        
        String[] parts = path.split("/");
        if(parts.length ==0) {
            return null;
        }
        
        for(IVideoProvider provider: videoProviders) {
            if(parts[0].startsWith(provider.getName())) {
                return provider.getFiles(path.substring(provider.getName().length()), querier);
            }
        }
        
        return null;

    }
}
