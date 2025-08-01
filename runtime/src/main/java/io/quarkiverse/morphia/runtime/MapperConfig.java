package io.quarkiverse.morphia.runtime;

import java.util.List;
import java.util.Optional;

import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Property;
import dev.morphia.mapping.DateStorage;
import dev.morphia.mapping.DiscriminatorFunction;
import dev.morphia.mapping.MapperOptions;
import dev.morphia.mapping.MapperOptions.PropertyDiscovery;
import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithDefault;

@ConfigGroup
public interface MapperConfig {
    /**
     * If critter is present, auto import the generated model information created by
     * <a href="https://morphia.dev/critter/4.4/index.html">critter</a>.
     */
    @WithDefault("false")
    boolean autoImportModels();

    /**
     * The strategy to use when calculating collection names for entities without an explicitly mapped collection name.
     *
     * @see Entity
     */
    @WithDefault("camelCase")
    NamingStrategy collectionNaming();

    /**
     * Create collection caps.
     */
    @WithDefault("false")
    boolean createCaps();

    /**
     * Create mapped indexes.
     */
    @WithDefault("false")
    boolean createIndexes();

    /**
     * Enable mapped document validation.
     */
    @WithDefault("false")
    boolean createValidators();

    /**
     * The database to use
     */

    String database();

    /**
     * Specifies how dates should be stored in the database. This value should only be changed to support legacy systems which
     * use the {@link DateStorage#SYSTEM_DEFAULT} setting. New projects should use the default value.
     *
     * @see DateStorage
     */
    @WithDefault("utc")
    DateStorage dateStorage();

    /**
     * The function to use when calculating an entity's discriminator value. Possible values include:
     * <ul>
     * <ol>
     * built-in functions defined on {@link DiscriminatorFunction}
     * </ol>
     * <ol>
     * the class names of a type extending {@link DiscriminatorFunction}
     * </ol>
     * </ul>
     *
     * @see DiscriminatorFunction
     */
    @WithDefault("simpleName")
    Discriminator discriminator();

    /**
     * The key to use when storing an entity's discriminator value
     */
    @WithDefault("_t")
    String discriminatorKey();

    /**
     * Should queries be updated to include subtypes when querying for a specific type
     */
    @WithDefault("true")
    boolean enablePolymorphicQueries();

    /**
     * Should final properties be serialized
     */
    @WithDefault("true")
    boolean ignoreFinals();

    /**
     * List the packages to automatically map. To map any subpackages, simply include {@code .*} on the end of the name. e.g.
     * otherwise the package name will be matched exactly against the declared package for a class. If this item is
     * missing/empty, no automatic mapping will be performed.
     *
     * @see Entity
     * @see Embedded
     */
    Optional<List<String>> packages();

    /**
     * Should "subpackages" also be mapped when mapping a specific package
     */
    @WithDefault("false")
    boolean mapSubPackages();

    /**
     * Specifies how properties of an entity are discovered.
     *
     * @see PropertyDiscovery
     */
    @WithDefault("fields")
    PropertyDiscovery propertyDiscovery();

    /**
     * The strategy to use when calculating collection names for entities without an explicitly mapped property name.
     *
     * @see Property
     */
    @WithDefault("identity")
    NamingStrategy propertyNaming();

    /**
     * Should empty Lists/Maps/Sets be serialized
     */
    @WithDefault("false")
    boolean storeEmpties();

    /**
     * Should null properties be serialized
     */
    @WithDefault("false")
    boolean storeNulls();

    default MapperOptions toMapperOptions() {
        return MapperOptions.builder()
                .autoImportModels(autoImportModels())
                .collectionNaming(collectionNaming().convert())
                .dateStorage(dateStorage())
                .discriminator(discriminator().convert())
                .discriminatorKey(discriminatorKey())
                .enablePolymorphicQueries(enablePolymorphicQueries())
                .ignoreFinals(ignoreFinals())
                .mapSubPackages(mapSubPackages())
                .propertyDiscovery(propertyDiscovery())
                .propertyNaming(propertyNaming().convert())
                .storeEmpties(storeEmpties())
                .storeNulls(storeNulls())
                .build();
    }
}
