package com.threading;

import java.util.Scanner;

import static com.threading.Functions.*;

public class Main {


    public static void main(String[] args) {
        int myArray[];
        double myArrayD[];
        int thr;
        int threads2[] = {1, 2, 3, 4, 5, 6, 7, 8, 20, 100, 500};
        System.out.println("1. Zad.1");
        System.out.println("2. Zad.1 bonus (double)");
        System.out.println("3. Zad.2 Int");
        System.out.println("4. Zad.2 Double");
        System.out.println("5. Zad.2 Czasy do tabeli Int");
        System.out.println("6. Zad.2 Czasy do tabeli Double");
        Scanner scan = new Scanner(System.in);
        int choice = scan.nextInt();

        while (choice > 6 || choice <= 0) {
            System.out.println("Podano niepoprawną wartość!");
            choice = scan.nextInt();
        }

        switch(choice) {
            case 1:
                intsTest(); //test do zadania 1
                break;

            case 2:
                doublesTest(); // test do zadania 1 ale dla double, nie wiem czy potrzebny
                break;

            case 3:
                myArray = generateArray(); //generowanie tablicy intów
                System.out.println("Podaj liczbe wątków: ");
                thr = scan.nextInt();
                while (thr < 0) {
                    System.out.println("Podano niepoprawną wartość!");
                    thr = scan.nextInt();
                }
                for (int i =0; i < 3; i++) {
                    myIntTest(myArray, thr); // testowanie czasu dla tablicy intów i wybranej liczby watkow
                }
                break;

            case 4:
                myArrayD = generateDoubleArray(); //generowanie tablicy double
                System.out.println("Podaj liczbe wątków: ");
                thr = scan.nextInt();
                while (thr < 0) {
                    System.out.println("Podano niepoprawną wartość!");
                    thr = scan.nextInt();
                }
                for (int i =0; i < 3; i++) {
                    myDoubleTest(myArrayD, thr); // testowanie czasu dla tablicy double i wybranej liczby watkow
                }
                break;

            case 5:
                myArray = generateArray();
                System.out.println("Inty");

                for (int x : threads2) {
                    System.out.print("Ilosc watkow: " + x + " | ");
                    double avg = 0;
                    for (int i = 0; i < 3; i++) {
                        double start = System.nanoTime();
                        minAndMaxWithThreading(myArray, x);
                        double stop = System.nanoTime();
                        double time = (stop - start) / 1000000000;
                        avg += time;
                        System.out.print("Czas " + i + " " + time + " | ");
                    }
                    System.out.print("Sredni czas:" + avg / 3 + "\n");
                }
                break;

            case 6:
                myArrayD = generateDoubleArray();
                System.out.println("Double");

                for (int x : threads2) {
                    System.out.print("Ilosc watkow: " + x + " | ");
                    double avg = 0;
                    for (int i = 0; i < 3; i++) {
                        double start = System.nanoTime();
                        minAndMaxWithThreading(myArrayD, x);
                        double stop = System.nanoTime();
                        double time = (stop - start) / 1000000000;
                        avg += time;
                        System.out.print("Czas " + i + " " + time + " | ");
                    }
                    System.out.print("Sredni czas:" + avg / 3 + "\n");
                }
                break;
        }
    }

}
