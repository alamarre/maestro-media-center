package org.maestromedia.tv.index;

import java.util.Comparator;

public class StringNumberSorter implements Comparator<String> {

    @Override
    public int compare(String o1, String o2) {
        return EpisodeHelper.getEpisodeNumber(o1) - EpisodeHelper.getEpisodeNumber(o2);
    }
    
}
