package org.maestromedia.tv.index.lucene;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.maestromedia.tv.index.TvShowMapper;
import org.maestromedia.tv.index.api.ISearchIndexer;

public class LuceneIndexer implements ISearchIndexer {
    String indexRoot;

    public LuceneIndexer(String indexRoot) {
        this.indexRoot = indexRoot;
    }
    
    @Override
    public void indexShowLocation(String path, String showName) {
        try {
            Document doc = new Document();
            Field pathField = new StringField("path", path, Field.Store.YES);
            doc.add(pathField);

            Field showField = new StringField("showName", showName, Field.Store.YES);
            doc.add(showField);
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            Directory dir = FSDirectory.open(Paths.get(this.indexRoot));
            IndexWriter writer = new IndexWriter(dir, iwc);
            writer.updateDocument(new Term("path", path), doc);
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(TvShowMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
