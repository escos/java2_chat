package ru.levelp.dao;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

public abstract class BaseMongoService<T> {
    private Datastore db;
    private Class<T> entityType;

    public BaseMongoService(Class<T> entityType) {
        this.entityType = entityType;
        Morphia morphia = new Morphia();
        db = morphia.createDatastore(
                new MongoClient("localhost"), "chatDB");
        db.ensureIndexes();
    }

    public Datastore request() {
        return db;
    }

    public void add(T entity) {
        db.save(entity);
    }

    public T get(long id) {
        return db.get(entityType, id);
    }

    public T delete(long id) {
        T entity = get(id);
        db.delete(entityType, id);
        return entity;
    }
}
