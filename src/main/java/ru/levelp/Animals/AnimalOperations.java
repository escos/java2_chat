package ru.levelp.Animals;

import com.mongodb.DBObject;

import java.util.List;
import java.util.Random;

public class AnimalOperations {

    private static Random gen = new Random();

    public Animal createAnimal() {
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
        return animal;
    }

    public void printListAnimals(List<Animal> animals) {
        for (Animal a : animals
                ) {
            System.out.printf("%-12s %10s %10s %5s %d year(s)\n ",
                    a.getName(), a.getType(), a.getColor(), a.getGender(), a.getAge());
        }
    }

    public void printDBObject(DBObject dbObject) {
        System.out.printf("%12s %10s %10s %5s %d year(s)\n ",
                dbObject.get("name"), dbObject.get("type")
                , dbObject.get("color"), dbObject.get("gender"), dbObject.get("age"));
    }
}
