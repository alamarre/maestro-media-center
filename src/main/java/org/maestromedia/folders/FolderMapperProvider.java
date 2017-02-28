package org.maestromedia.folders;

import ca.omny.db.IDocumentQuerier;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FolderMapperProvider implements IVideoProvider {

    String rootDirectory;

    public FolderMapperProvider(String rootDirectory) {
        this.rootDirectory = rootDirectory;
    }
    
    @Override
    public String getName() {
        return "Browse";
    }

    @Override
    public String getPath(String path, IDocumentQuerier querier) {
        return path;
    }
    
    public File getLocation(String path, IDocumentQuerier querier) {
        String videoDir = rootDirectory + "/videos";
        if (path == null) {
            return new File(videoDir).getAbsoluteFile();
        }
        if(path.startsWith("/")) {
            path = path.substring(1);
        }
        
        String[] pathComponents = path.split("/");
        File lnkFile = new File(videoDir + "/" + pathComponents[0] + ".lnk");
        if (lnkFile.exists()) {
            try {
                WindowsShortcut shortcut = new WindowsShortcut(lnkFile);
                if (shortcut.isDirectory()) {
                    StringBuilder builder = new StringBuilder();
                    builder.append(shortcut.getRealFilename());
                    for (int i = 1; i < pathComponents.length; i++) {
                        builder.append("/" + pathComponents[i]);
                    }
                    path = builder.toString();
                }

            } catch (IOException ex) {
                Logger.getLogger(FolderMapper.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(FolderMapper.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            path = videoDir + "/" + path;
        }

        return new File(path).getAbsoluteFile();
    }

    @Override
    public Map<String, Collection<String>> getFiles(String path, IDocumentQuerier querier) {
        File dir = this.getLocation(path, querier);
        Map<String, Collection<String>> list = new HashMap<String, Collection<String>>();
        List<String> files = new LinkedList<String>();
        List<String> folders = new LinkedList<String>();
        
        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                String fileName = file.getName();
                if (!fileName.equals(".DS_Store")) {
                    if (fileName.endsWith(".lnk")) {
                        folders.add(fileName.substring(0, fileName.lastIndexOf(".lnk")));
                    } else if (file.isDirectory()) {
                        folders.add(fileName);
                    } else {
                        files.add(fileName);
                    }
                }
            }
        }

        TvShowSort sort = new TvShowSort();
        Collections.sort(files, sort);
        Collections.sort(folders, sort);
        list.put("folders", folders);
        list.put("files", files);
        return list;
    }
    
}
