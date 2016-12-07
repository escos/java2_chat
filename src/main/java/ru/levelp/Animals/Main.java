package ru.levelp.Animals;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AnimalService animalService = new AnimalService();
        AnimalOperations animalOperations = new AnimalOperations();

        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        if (N > 0)
            for (int i = 0; i < N; i++) {
                animalService.add(animalOperations.createAnimal());
            }

        System.out.println("All dark animals:");
        animalOperations.printListAnimals(animalService.getAllDark());
//        System.out.println("All light animals:");
//        animalService.printAnimals(animalService.getAllLight());
        System.out.println("All boys:");
        animalOperations.printListAnimals(animalService.getAllBoys());
//        System.out.println("All girls:");
//        animalService.printAnimals(animalService.getAllGirls());
        System.out.println("Younger than 30:");
        animalOperations.printListAnimals(animalService.getYounger(30));
//        System.out.println("Older than 30:");
//        animalService.printAnimals(animalService.getOlder(30));
        System.out.println("Youngest boy:");
        animalOperations.printAnimal(animalService.getYoungestBoy());
        System.out.println("Oldest girl:");
        animalOperations.printAnimal(animalService.getOldestGirl());
    }
}
