package ru.levelp.Animals;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AnimalService animalService = new AnimalService();
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        if (N > 0)
            for (int i = 0; i < N; i++) {
                animalService.add();
            }
        System.out.println("All dark animals:");
        animalService.printAnimals(animalService.getAllDark());
//        System.out.println("All light animals:");
//        animalService.printAnimals(animalService.getAllLight());
        System.out.println("All boys:");
        animalService.printAnimals(animalService.getAllBoys());
//        System.out.println("All girls:");
//        animalService.printAnimals(animalService.getAllGirls());
        System.out.println("Younger than 30:");
        animalService.printAnimals(animalService.getYounger(30));
//        System.out.println("Older than 30:");
//        animalService.printAnimals(animalService.getOlder(30));
        System.out.println("Youngest boy:");
        System.out.println(animalService.getYoungestBoy());
        System.out.println("Oldest girl:");
        System.out.println(animalService.getOldestGirl());
    }
}
