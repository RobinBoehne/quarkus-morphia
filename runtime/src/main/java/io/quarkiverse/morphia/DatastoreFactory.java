package io.quarkiverse.morphia;

import java.util.List;

import com.mongodb.client.MongoClient;

import dev.morphia.Datastore;
import dev.morphia.Morphia;
import io.quarkiverse.morphia.runtime.MapperConfig;

public class DatastoreFactory {
    private final MapperConfig mapperConfig;

    private final MongoClient mongoClient;

    private final List<String> classNames;

    public DatastoreFactory(List<String> classNames, MongoClient mongoClient, MapperConfig mapperConfig) {
        this.classNames = classNames;
        this.mongoClient = mongoClient;
        this.mapperConfig = mapperConfig;
    }

    public Datastore createDatastore(String dbName) {
        Datastore datastore = Morphia.createDatastore(this.mongoClient, dbName, this.mapperConfig.toMapperOptions());
        for (String className : classNames) {
            try {
                datastore.getMapper().map(Class.forName(className, true, Thread.currentThread().getContextClassLoader()));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Failed to load class: " + className, e);
            }
        }
        if (this.mapperConfig.createValidators()) {
            datastore.enableDocumentValidation();
        }
        if (this.mapperConfig.createCaps()) {
            //datastore.ensureCaps();
        }
        if (this.mapperConfig.createIndexes()) {
            datastore.ensureIndexes();
        }
        return datastore;
    }
}
