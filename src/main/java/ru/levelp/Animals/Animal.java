package ru.levelp.Animals;

import javax.persistence.*;

//morphia
@org.mongodb.morphia.annotations.Entity("Animals")

public class Animal {

    //morphia
    @org.mongodb.morphia.annotations.Id
    private long id;

    private String type;

    private String name;

    private int color;

    private int age;

    private String gender;

    public Animal() {
    }

    public Animal(String type, String name, int color, int age, String gender) {
        this.name = name;
        this.type = type;
        this.color = color;
        this.age = age;
        this.gender = gender;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getColor() {
        return color;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
