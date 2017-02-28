package org.maestromedia.folders;

import ca.omny.db.IDocumentQuerier;
import java.io.File;
import java.util.Collection;
import java.util.Map;

public interface IVideoProvider {
    public String getName();
    public String getPath(String path, IDocumentQuerier querier);
    public Map<String, Collection<String>> getFiles(String path, IDocumentQuerier querier);
}
