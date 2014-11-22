package org.maestromedia.storage;

import org.maestromedia.configuration.ConfigurationReader;
import org.maestromedia.documentdb.CouchFactory;
import org.maestromedia.logger.MaestroLogger;
import org.maestromedia.storage.implementation.S3Storage;
import org.maestromedia.storage.implementation.LocalStorage;
import org.maestromedia.storage.implementation.DocumentDatabaseStorage;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@ApplicationScoped
public class StorageFactory {
    
    @Inject
    ConfigurationReader configurationReader;
    
    S3Storage s3Storage;
    
    DocumentDatabaseStorage documentDbStorage;
    
    @Produces
    public IStorage getStorage(MaestroLogger logger) {
        String storageSystemName = configurationReader.getSimpleConfigurationString("storageSystem");
        
        if(storageSystemName.equals("local")) {
            String rootFolder = configurationReader.getSimpleConfigurationString("localFolder");
            LocalStorage localStorage = new LocalStorage(rootFolder);
            return localStorage;
        }
        if(storageSystemName.equals("s3")) {
            if(s3Storage==null) {
                s3Storage = new S3Storage(configurationReader);
            }
            return s3Storage;
        }
        if(documentDbStorage==null) {
            documentDbStorage = new DocumentDatabaseStorage();
            documentDbStorage.setConfigurationReader(configurationReader);
            documentDbStorage.setLogger(logger);
            documentDbStorage.setQuerier(CouchFactory.getQuerier());
        }
        
        return documentDbStorage;       
    }
}
