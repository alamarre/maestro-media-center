package org.maestromedia.db.extended;

import org.maestromedia.documentdb.IDocumentQuerier;
import org.maestromedia.documentdb.IDocumentQuerierFactory;
import org.maestromedia.storage.IStorage;
import javax.inject.Inject;

public class StorageDatabaseFactory implements IDocumentQuerierFactory {

    StorageBasedDatabase db;
    
    @Inject
    IStorage storage;
    
    @Override
    public IDocumentQuerier getInstance() {
        String dbType = System.getenv("maestro_db_type");
        if(dbType==null || dbType.equals("storage")) {
            if(db==null) {
                db = new StorageBasedDatabase();
                db.setStorage(storage);
            }
            return db;
        }
        return null;
    }
    
}
