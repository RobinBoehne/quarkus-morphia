package io.quarkiverse.morphia;

import static io.quarkus.runtime.annotations.ConfigPhase.RUN_TIME;

import java.util.Map;

import io.quarkiverse.morphia.runtime.MapperConfig;
import io.quarkus.mongodb.runtime.MongoClientBeanUtil;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithParentName;

@ConfigMapping(prefix = "quarkus.morphia")
@ConfigRoot(phase = RUN_TIME)
public interface MorphiaConfig {
    /**
     * The default Mapper configuration.
     */
    @WithParentName
    MapperConfig defaultMapperConfig();

    /**
     * Configures additional {@code @Mapper} configurations.
     * <p>
     * each cluster have a unique identifier witch must be identified to select the right connection.
     * example:
     *
     * <pre>
     * {@code
     * quarkus.morphia.mapper-configs.cluster1.discriminatorKey = className
     * quarkus.morphia.mapper-configs.cluster2.discriminatorKey = _type
     * }
     * </pre>
     *
     * And then use annotations above the instances of {@code @MongoClient} to indicate which instance we are going to use
     *
     * <pre>
     * {
     *     &#64;code
     *     &#64;Inject
     *     &#64;MongoClientName("cluster1")
     *     Datastore datastore;
     * }
     * </pre>
     */
    Map<String, MapperConfig> mapperConfigs();

    default MapperConfig getMapperConfig(String clientName) {
        return MongoClientBeanUtil.isDefault(clientName)
                ? defaultMapperConfig()
                : mapperConfigs().get(clientName);
    }
}
