package org.maestromedia.tv.index.tmdb;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.apache.commons.io.IOUtils;
import org.maestromedia.tv.index.EpisodeInfo;

public class TmdbReader {
    String apiKey = "7804cdacc8cf41f2c8b73a8b6b54ad96";
    String rootUrl = "https://api.themoviedb.org/3";
    Gson gson = new Gson();
    
    public String getId(String showName) {
        try {
            String encoded = URLEncoder.encode(showName, "UTF-8");
            String url = rootUrl +"/search/tv?api_key="+apiKey+"&language=en-US&query="+encoded;
            String response = getResponse(url);
            
            TmdbSearchResult result = gson.fromJson(response, TmdbSearchResult.class);
            
            if(result.getResults().size()==1) {
                return ""+result.getResults().get(0).getId();
            }
            
            Stream<TmdbShow> filter = result.getResults().stream().filter(x -> x.getTitle().equalsIgnoreCase(showName));
            if(filter.count() == 0) {
                return ""+filter.findFirst().get().getId();
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TmdbReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public EpisodeInfo getInfo(String showId, int season, int episode) {
        String url = rootUrl +String.format("/tv/%s/season/%s/episode/%s?api_key="+apiKey, showId, season, episode);
        String response = getResponse(url);

        if(response == null) {
            return null;
        }
        
        TmdbEpisodeInfo result = gson.fromJson(response, TmdbEpisodeInfo.class);
        EpisodeInfo episodeInfo = new EpisodeInfo();
        episodeInfo.setDescription(result.overview);
        episodeInfo.setTitle(result.name);
        episodeInfo.setSeason(season);
        episodeInfo.setShowId(showId);
        episodeInfo.setEpisode(episode);
        
        return episodeInfo;
    }
    
    private String getResponse(String url) {
        try {
            URL u = new URL(url);
            HttpURLConnection connection = (HttpURLConnection)u.openConnection();
            String rate = connection.getHeaderField("X-RateLimit-Remaining");
               
                try {
                    int count = Integer.parseInt(rate);
                    if(count <= 1) {
                        Thread.sleep(10*1000);
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(TmdbReader.class.getName()).log(Level.SEVERE, null, ex);
                }
                 catch (NumberFormatException ex) {
                    Logger.getLogger(TmdbReader.class.getName()).log(Level.SEVERE, null, ex);
                }
            
            return IOUtils.toString(connection.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(TmdbReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
}
