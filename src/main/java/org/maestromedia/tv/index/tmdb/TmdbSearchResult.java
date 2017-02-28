package org.maestromedia.tv.index.tmdb;

import java.util.Collection;
import java.util.LinkedList;

public class TmdbSearchResult {
    LinkedList<TmdbShow> results;

    public LinkedList<TmdbShow> getResults() {
        return results;
    }

    public void setResults(LinkedList<TmdbShow> results) {
        this.results = results;
    }
}
