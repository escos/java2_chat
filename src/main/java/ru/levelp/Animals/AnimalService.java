package ru.levelp.Animals;


import com.mongodb.*;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class AnimalService {
    private Datastore db;
    private Random gen = new Random();

    public AnimalService() {
        Morphia morphia = new Morphia();
        db = morphia.createDatastore(
                new MongoClient("localhost"), "chatDB");
        db.ensureIndexes();
    }

    public void add() {
        Animal animal = new Animal();
        animal.setId((long) (Math.random() * Long.MAX_VALUE));
        animal.setAge((int) (Math.random() * 100));
        animal.setColor((int) (Math.random() * 255));
        if (gen.nextBoolean())
            animal.setGender("boy");
        else animal.setGender("girl");
        String[] herbalsArray = {"Elephant", "Giraffe", "Antelope", "Zebra", "Rhinoceros", "Elk",
                "Kangaroo", "Monkey", "Koala", "Panda"};
        String[] predatorsArray = {"Tiger", "Wolf", "Fox", "Bear", "Coyote", "Lion",
                "Crocodile", "Jaguar", "Sable", "Raccoon"};
        if (gen.nextBoolean()) {
            animal.setType("herbivore");
            animal.setName(herbalsArray[gen.nextInt(herbalsArray.length)]);
        } else {
            animal.setType("predator");
            animal.setName(predatorsArray[gen.nextInt(predatorsArray.length)]);
        }
        db.save(animal);
    }

    public List<Animal> getAll() {
        return db.createQuery(Animal.class)
                .asList();
    }

    public Animal get(long id) {
        return db.get(Animal.class, id);
    }

    public Animal getByType(String type) {
        return db.createQuery(Animal.class)
                .field("type").equal(type)
                .get();
    }

    public Animal getByName(String name){
        return db.createQuery(Animal.class)
                .field("type").equal(name)
                .get();
    }

    public List<Animal> getAllBoys() {
        return db.createQuery(Animal.class)
                .field("gender").equal("boy")
                .asList();
    }

    public List<Animal> getAllGirls() {
        return db.createQuery(Animal.class)
                .field("gender").equal("girl")
                .asList();
    }

    public List<Animal> getAllDark() {
        return db.createQuery(Animal.class)
                .field("color").lessThan(126)
                .asList();
    }

    public List<Animal> getAllLight() {
        return db.createQuery(Animal.class)
                .field("color").greaterThanOrEq(155)
                .asList();
    }

    public List<Animal> getYounger(int age) {
        return db.createQuery(Animal.class)
                .field("age").lessThan(age)
                .asList();
    }

    public List<Animal> getOlder(int age) {
        return db.createQuery(Animal.class)
                .field("age").greaterThan(age)
                .asList();
    }

    public DBObject getYoungestBoy() {
        DBCursor animal = db.getCollection(Animal.class).find(new BasicDBObject("gender","boy"));
        animal.sort(new BasicDBObject("age",1)).limit(1);
        return animal.next();
    }

    public DBObject getOldestGirl() {
        DBCursor animal = db.getCollection(Animal.class).find(new BasicDBObject("gender","girl"));
        animal.sort(new BasicDBObject("age",-1)).limit(1);
        return animal.next();
    }

    public Animal delete(long id) {
        Animal deletedAnimal = get(id);
        db.delete(Animal.class, id);
        return deletedAnimal;
    }

    public void printAnimals(List<Animal> animals){
        for (Animal a: animals
             ) {
             System.out.printf(" %s %10s %d %10s %d\n ",
                    a.getName(), a.getType(), a.getColor(), a.getGender(), a.getAge());
        }
    }
}

