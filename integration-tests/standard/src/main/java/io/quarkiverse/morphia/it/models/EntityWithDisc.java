package io.quarkiverse.morphia.it.models;

import org.bson.types.ObjectId;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

@Entity(value = "entities")
public class EntityWithDisc {
    @Id
    ObjectId id;
}
