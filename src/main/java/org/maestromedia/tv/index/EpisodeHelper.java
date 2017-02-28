package org.maestromedia.tv.index;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EpisodeHelper {
    final static Pattern prefixPattern = Pattern.compile("^([0-9]+)\\s*-?");
    final static Pattern seasonNumberEpisodeNumberPattern = Pattern.compile("[sS][0-9]+[eE]([0-9]+)");
    
    public static int getEpisodeNumber(String fileName) {
        int episodeNumber = -1;
        if(fileName !=null) {
            String[] parts = fileName.split("/");
            String episode = parts[parts.length-1];
            Matcher matcher = prefixPattern.matcher(episode);
            
            if(matcher.find()) {
                episodeNumber = Integer.parseInt(matcher.group(1));
            } else {
                matcher = seasonNumberEpisodeNumberPattern.matcher(episode);
                if(matcher.find()) {
                   episodeNumber = Integer.parseInt(matcher.group(1));
                }
            }
        }
        
        return episodeNumber;
    }
    
    public static String getEpisodeName(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }
}
