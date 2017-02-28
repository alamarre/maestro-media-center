package org.maestromedia.tv.index.tmdb;

import ca.omny.db.IDocumentQuerier;
import java.util.Collection;

public class TmdbShowMapper {
    private static final String TMDB_SHOW_PREFIX = "TMDB_SHOW_MAPPING";
    private static final String TMDB_TV_SHOW_PREFIX = "TMDB_TV_SHOW_MAPPING";
    
    public void recordTvShow(TmdbShow show, IDocumentQuerier querier) {
        querier.set(querier.getKey(TMDB_TV_SHOW_PREFIX, show.getTitle()), show);
    }
    
    public void recordMovie(TmdbShowToLocalMapping show, IDocumentQuerier querier) {
        querier.set(querier.getKey(TMDB_SHOW_PREFIX, show.getLocalPath()), show);
    }
    
    public Collection<TmdbShow> getAllTvShows(IDocumentQuerier querier) {
        return querier.getRange(TMDB_TV_SHOW_PREFIX, TmdbShow.class);
    }
    
    public Collection<TmdbShowToLocalMapping> getAllMovies(IDocumentQuerier querier) {
        return querier.getRange(TMDB_SHOW_PREFIX, TmdbShowToLocalMapping.class);
    }
}
