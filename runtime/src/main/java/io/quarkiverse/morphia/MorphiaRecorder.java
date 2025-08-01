package io.quarkiverse.morphia;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import com.mongodb.client.MongoClient;

import dev.morphia.Datastore;
import dev.morphia.Morphia;
import io.quarkiverse.morphia.runtime.MapperConfig;
import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class MorphiaRecorder {
    public Supplier<Datastore> datastoreSupplier(Supplier<MongoClient> mongoClientSupplier,
            MorphiaConfig morphiaConfig, List<String> entities, String clientName) {
        return () -> {
            MapperConfig config = morphiaConfig.getMapperConfig(clientName);
            Datastore datastore = Morphia.createDatastore(mongoClientSupplier.get(), config.database(),
                    config.toMapperOptions());
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            config.packages().ifPresent(packages -> {
                try {
                    for (String mapPackage : packages) {
                        Pattern pattern = Pattern.compile(mapPackage.endsWith(".*") ? mapPackage : mapPackage + ".[A-Z]+");
                        for (String type : entities) {
                            if (pattern.matcher(type).lookingAt()) {
                                datastore.getMapper().map(contextClassLoader.loadClass(type));
                            }
                        }
                    }
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
                if (config.createValidators()) {
                    datastore.enableDocumentValidation();
                }
                if (config.createCaps()) {
                    //datastore.ensureCaps();
                }
                if (config.createIndexes()) {
                    datastore.ensureIndexes();
                }
            });
            return datastore;
        };
    }

    public Supplier<DatastoreFactory> datastoreFactorySupplier(Supplier<MongoClient> mongoClientSupplier,
            MorphiaConfig morphiaConfig, List<String> entities, String clientName) {
        return () -> {
            MapperConfig config = morphiaConfig.getMapperConfig(clientName);
            List<String> classNames = new ArrayList<>();
            config.packages().ifPresent(packages -> {
                for (String mapPackage : packages) {
                    Pattern pattern = Pattern.compile(mapPackage.endsWith(".*") ? mapPackage : mapPackage + ".[A-Z]+");
                    for (String type : entities) {
                        if (pattern.matcher(type).lookingAt()) {
                            classNames.add(type);
                        }
                    }
                }
            });
            return new DatastoreFactory(classNames, mongoClientSupplier.get(), config);
        };
    }
}
