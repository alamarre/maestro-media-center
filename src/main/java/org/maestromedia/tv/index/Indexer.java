package org.maestromedia.tv.index;

import org.maestromedia.tv.index.tmdb.TmdbReader;
import ca.omny.db.IDocumentQuerier;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.maestromedia.folders.FolderMapperProvider;
import org.maestromedia.tv.index.api.ITvShowInfoProvider;
import org.maestromedia.tv.index.api.ITvShowTracker;

/***
 * Finds titles (movies & tv show episodes), adds new ones, gets Tmdb info if it can be determined
 * @author al
 */
public class Indexer implements Runnable {
    FolderMapperProvider folderMapperProvider;
    int scanInterval;
    IDocumentQuerier querier;
    ITvShowTracker tvShowTracker;
    ITvShowInfoProvider tvShowInfoProvider;
    TmdbReader tmdbReader;
    final static Pattern seasonNumberPattern = Pattern.compile("[0-9]+");
    final static Pattern prefixPattern = Pattern.compile("^([0-9]+) -");
    final static Pattern seasonNumberEpisodeNumberPattern = Pattern.compile("[sS][0-9]+[eE]([0-9]+)");
    Thread thread;
    boolean fetchInfo;
    boolean keepRunning;
    

    public Indexer(FolderMapperProvider folderMapperProvider, ITvShowTracker tvShowTracker, ITvShowInfoProvider tvShowInfoProvider, int scanInterval, IDocumentQuerier querier) {
        this.folderMapperProvider = folderMapperProvider;
        this.scanInterval = scanInterval;
        this.querier = querier;
        this.tvShowTracker = tvShowTracker;
        this.tvShowInfoProvider = tvShowInfoProvider;
        this.fetchInfo = false;
        tmdbReader = new TmdbReader();
        keepRunning = false;
    }
    
    public void search() {
        if(thread != null) {
            return;
        }
        
        thread = new Thread(this);
        thread.start();
    }  
    
    private void searchRootFolders() {
        Map<String, Collection<String>> files = folderMapperProvider.getFiles(null, querier);
        Collection<String> folders = files.get("folders");
        
        for(String folder: folders) {
            if(folder.toLowerCase().startsWith("tv shows")) {
                searchTvShowFolder(folder);
            } else {
                for(String childFolder: folderMapperProvider.getFiles("/"+folder, querier).get("folders")) {
                    if(folder.toLowerCase().startsWith("tv shows")) {
                        searchTvShowFolder(folder+"/"+childFolder);
                    }
                }
            }
        }
    }
    
    private void searchTvShowFolder(String folder) {
        Collection<String> folders = folderMapperProvider.getFiles(folder, querier).get("folders");
        for(String show: folders) {
            String path = folder+"/"+show;
            tvShowTracker.recordShowLocation(path, show);
            //go through episodes and index
            for(String season: folderMapperProvider.getFiles(folder+"/"+show, querier).get("folders")) {
                Matcher m = seasonNumberPattern.matcher(season);
                if(m.find()) {
                    int seasonNumber = Integer.parseInt(m.group());
                    if(!m.find()) {
                        for(String episode: folderMapperProvider.getFiles(folder+"/"+show+"/"+season, querier).get("files")) {
                            int episodeNumber = EpisodeHelper.getEpisodeNumber(episode);

                            if(episodeNumber != -1) {
                                tvShowTracker.recordEpisode(show, seasonNumber, episodeNumber, folder+"/"+show+"/"+season+"/"+episode);
                                if(fetchInfo && tvShowInfoProvider.getInfo(show, seasonNumber, episodeNumber) == null)  {
                                    EpisodeInfo info = tmdbReader.getInfo(show, seasonNumber, episodeNumber);
                                    if(info != null) {
                                        tvShowInfoProvider.saveEpisodeInfo(info);
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void run() {
        do {
            try {
                searchRootFolders();    
                if(keepRunning) {
                    Thread.sleep(scanInterval);
                }
            } catch (Exception ex) {
                Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while(keepRunning);
    }
}
