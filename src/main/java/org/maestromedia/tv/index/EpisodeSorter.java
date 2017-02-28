package org.maestromedia.tv.index;

import java.util.Comparator;

public class EpisodeSorter implements Comparator<EpisodeInfo> {

    @Override
    public int compare(EpisodeInfo o1, EpisodeInfo o2) {
        if(o1.getSeason() == o2.getSeason()) {
            return o1.getEpisode() - o2.getEpisode();
        }
        
        return o1.getSeason() - o2.getSeason();
    }
    
}
