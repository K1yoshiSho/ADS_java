package com;

import java.util.*;

public class Main {
    public static void main(String[] args) {

        Set<String> setTextBook = new LinkedHashSet<>(Arrays.asList(
                "Chemistry", "Mathematics", "Biology", "English"));
        Set<String> setTextBook1 = new LinkedHashSet<>(Arrays.asList(
                "Biology", "English", "Geography", "Physics"));

        Set<String> union = new LinkedHashSet<>(setTextBook); // The union of the two sets
        union.addAll(setTextBook1);
        System.out.println("Union of the two sets: " + union);

        Set<String> difference = new LinkedHashSet<>(setTextBook); // The difference of the two sets
        difference.removeAll(setTextBook1);
        System.out.println("Difference of the two sets: " + difference);

        Set<String> intersection = new LinkedHashSet<>(); // The intersection of the two sets
        for (String temp: setTextBook1) {
            if (setTextBook.contains(temp))
                intersection.add(temp);
        }
        System.out.println("Intersection of the two sets: " + intersection);
    }
}
