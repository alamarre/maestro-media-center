package org.maestromedia.tv.index;

import ca.omny.db.IDocumentQuerier;
import java.util.Collection;

/**
 * Used for searching for titles. Focus is movies or tv shows
 */
public class TitleInfoMapper {
    
    private static String TITLE_INFO_PREFIX = "TITLE_INFO";
   
    public TitleInfo getTitleInfo(String path, IDocumentQuerier querier) {
        String key = querier.getKey(TITLE_INFO_PREFIX, path);
        return querier.get(key, TitleInfo.class);
    }
    
    public void setInfo(TitleInfo info, String path, IDocumentQuerier querier) {
        String key = querier.getKey(TITLE_INFO_PREFIX, path);
        querier.set(key, info);
    }
    
    public Collection<TitleInfo> getAllTitles(IDocumentQuerier querier) {
        return querier.getRange(TITLE_INFO_PREFIX, TitleInfo.class);
    }
}
