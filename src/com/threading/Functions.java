package com.threading;

import java.util.Arrays;
import java.util.Random;

public class Functions {
    public static final int arrayMaxLength = 320000000; //dlugosc naszej tablicy intów do której będziemy losować liczby
    public static final int arrayMaxLengthD = 160000000; // to samo co wyżej tylko dla double, ilosc jest zmniejszona, poniewaz double zajmuje wiecej pamieci
    public static final int MIN = -100000000; //dolny zakres do losowania intów
    public static final int MAX = 100000000; //górny zakres do losowania intów
    public static final int MIN_D = -100000; //dolny zakres do losowania double'ów
    public static final int MAX_D = 100000; //górny zakres do losowania double'ów
    public static final int threads[] = {2, 4, 6, 8}; // tablica z ilościami watków do zad1

    public static int[] generateArray() { // funkcja zwraacająca tablicę z randomowymi liczbami
        int array[] = new int[arrayMaxLength]; // arrayMaxLength jest podany nad funkcja main
        Random rand = new Random();

        for (int i = 0; i < array.length; i++) {
            array[i] = rand.nextInt(MAX - MIN) + MIN; // losowanie inta z zakresu MIN MAX podanych nad funkcja main
        }

        return array; // zwracanie funkcji
    }

    public static double[] generateDoubleArray() { //identyczna funkcja do tej u góry, tylko że zwraca tablice double
        double array[] = new double[arrayMaxLengthD];
        Random rand = new Random();

        for (int i = 0; i < array.length; i++) {
            array[i] = rand.nextDouble(MAX_D - MIN_D) + MIN_D;
        }

        return array;
    }

    public static int[] minAndMax(int[] array) { // funkcja szukająca min i max dla zadanej tablicy int, uzywana do wyszukiwania min max dla podzielonej juz tablicy
        // w danym wątku z klasy myThreadInt

        int min = Integer.MAX_VALUE; // najwieksza liczba dla inta, gwarantuje nam to ze kazda liczba ktora do niej przyrownamy bedzie mniejsza niz ona
        int max = Integer.MIN_VALUE; // to samo tylko najmniejsza
        int minMax[] = new int[2]; // tablica z wartosciami min i max, 0 pozycja to min, 1 to max

        for (int x : array) { // przejscie przez cala tablice z wylosowanymi intami
            if(x > max) { // sprawdzamy czy nasza liczba jest większa niz max
                max = x; // jesli tak to zastepujemy max nasza nowa wartoscia
            }
            if(x < min) { // sprawdzamy czy nasza liczba jest mniejsza niz minimum
                min = x; // jesli tak to zastepujemy min nasza nowa wartoscia
            }
        }
        minMax[0] = min; // wpisanie naszym min max do tablicy
        minMax[1] = max;

        return minMax;
    }

    public static double[] minAndMax(double[] array) {// identyczna funkcja jak ta powyzej tylko wykorzystywana do watkow dla liczb double z klasy myThreadDouble
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        double minMax[] = new double[2];

        for (double x : array) {
            if(x > max) {
                max = x;
                minMax[1] = x;
            }
            if(x < min) {
                min = x;
                minMax[0] = x;
            }
        }

        minMax[0] = min;
        minMax[1] = max;
        return minMax;
    }

    public static int[] minAndMaxWithThreading(int[] array, int threads) { // funkcja dla intów w w której tworzymy nowe wątki w ktorych szukamy min max
        // podajemy do niej cala tablice i liczbe watkow
        // te funkcje bedziemy wykorzystywac pozniej w testach
        myThreadInt threadsArray[] = new myThreadInt[threads]; // utworzenie tablicy watkow, threads to liczba watkow
        int start, end; // start to poczatek zakresu ktory podamy do watku a end to koniec
        int rangeForThread= array.length / threads; // jest to ilosc liczb jaka przypada na jeden watek
        int minMax[] = new int[2];
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        if ( array.length % threads != 0) { // sprawdzamy tutaj czy uda nam się podzielic po rowno ilosc liczb dla kazdego watku
            for (int i = 0 ; i < threads-1; i++) { // jesli sie nie uda to dla wszystkich oprocz ostatniego watku przydzielamy taka sama ilosc liczb
                start = (i * rangeForThread);
                end = (i + 1) * rangeForThread - 1;
                threadsArray[i] = new myThreadInt(Arrays.copyOfRange(array, start, end)); // utworzenie nowego watku, do konstruktora przekazujemy dany kopie tablicy z danym zakresem liczb
                threadsArray[i].start(); // "wlaczenie" nowego watku, odpala sie tutaj funkcja run dla tego watku
            } // tutaj dla ostatniego watku przydzielamy reszte liczb i wlaczamy go
            start = (threads - 1) * rangeForThread;
            end = array.length - 1;
            threadsArray[threads - 1] = new myThreadInt(Arrays.copyOfRange(array, start, end));
            threadsArray[threads - 1].start();
        }
        else { //jezeli mozna podzielic liczby po rowno dla wszystkich watkow to wykonamy te czesc
            for (int i = 0 ; i < threads; i++) {
                start = (i * rangeForThread);
                end = (i + 1) * rangeForThread - 1;
                threadsArray[i] = new myThreadInt(Arrays.copyOfRange(array, start, end));
                threadsArray[i].start();
            }
        }

        for (int i = 0; i < threads; i++) { //uzywamy tutaj dla kazdego watku funkcji join co powoduje ze  nie przejdziemy do dalszej czesci programu dopoki dzialania
            //we wszystkich watkach nie zostana ukonczone(dopoko funkcja run sie nie skonczy)
            try {
                threadsArray[i].join();
            } catch (InterruptedException e) {
                System.out.println("Error");
            }
        }

        int threadMin;
        int threadMax;

        for (int i = 0 ; i < threads; i++)  {  //tutaj sprawdzamy jaka jest najnizsza wartosc i najwieksza wartosc we wszystkich watkach, identycznie jak w funkcji minAndMax
            threadMin = threadsArray[i].threadMinMax[0];
            threadMax = threadsArray[i].threadMinMax[1];
            if(threadMax > max) {
                minMax[1] = threadsArray[i].threadMinMax[1];
                max = threadsArray[i].threadMinMax[1];
            }

            if(threadMin < min) {
                minMax[0] = threadsArray[i].threadMinMax[0];
                min = threadsArray[i].threadMinMax[0];
            }
        }

        return minMax;
    }

    public static double[] minAndMaxWithThreading(double[] array, int threads) { // funkcja identyczna do funkcji u gory ale robi to dla tablic typu doouble i tworzy watki z klasy
        // myThreadDouble
        myThreadDouble threadsArray[] = new myThreadDouble[threads];
        int start, end;
        int rangeForThread= array.length / threads;
        double minMax[] = new double[2];
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        if ( array.length % threads != 0) {
            for (int i = 0 ; i < threads-1; i++) {
                start = (i * rangeForThread);
                end = (i + 1) * rangeForThread - 1;
                threadsArray[i] = new myThreadDouble(Arrays.copyOfRange(array, start, end));
                threadsArray[i].start();
            }
            start = (threads - 1) * rangeForThread;
            end = array.length - 1;
            threadsArray[threads - 1] = new myThreadDouble(Arrays.copyOfRange(array, start, end));
            threadsArray[threads - 1].start();
        }
        else {
            for (int i = 0 ; i < threads; i++) {
                start = (i * rangeForThread);
                end = (i + 1) * rangeForThread - 1;
                threadsArray[i] = new myThreadDouble(Arrays.copyOfRange(array, start, end));
                threadsArray[i].start();
            }
        }

        for (int i = 0; i < threads; i++) {
            try {
                threadsArray[i].join();
            } catch (InterruptedException e) {
                System.out.println("Error");
            }
        }

        double threadMin;
        double threadMax;

        for (int i = 0 ; i < threads; i++)  {
            threadMin = threadsArray[i].threadMinMax[0];
            threadMax = threadsArray[i].threadMinMax[1];
            if(threadMax > max) {
                minMax[1] = threadsArray[i].threadMinMax[1];
                max = threadsArray[i].threadMinMax[1];
            }

            if(threadMin < min) {
                minMax[0] = threadsArray[i].threadMinMax[0];
                min = threadsArray[i].threadMinMax[0];
            }
        }

        return minMax;
    }

    public static void intsTest() { // test do zadania 1
        System.out.println("Ints:");
        int myArray[] = generateArray();

        double start=System.nanoTime();
        int minMax[] = minAndMaxWithThreading(myArray, 1); // sprawdzamy czas dla 1 watku
        double stop=System.nanoTime();
        System.out.println("Czas wykonania:"+(stop-start)/1000000000); // dzielimy przez 10^9 zeby uzyskac czas w sekundach
        System.out.println(Arrays.toString(minMax)); // wypisanie wartosci min i max

        for (int i : threads) { // sprawdzamy czas dla watkow 2, 4, 6, 8
            start = System.nanoTime();
            minMax = minAndMaxWithThreading(myArray, i);
            stop = System.nanoTime();
            System.out.println("Czas wykonania:"+(stop-start)/1000000000);
            System.out.println(Arrays.toString(minMax));
        }
    }

    public static void doublesTest() { // identyczna funkcja jak powyzej tylko dla typu double
        System.out.println("Doubles:");
        double myDoubleArray[] = generateDoubleArray();

        double start = System.nanoTime();
        double minMaxDouble[] = minAndMaxWithThreading(myDoubleArray, 1);
        double stop = System.nanoTime();
        System.out.println("Czas wykonania: " + (stop-start) / 1000000000);
        System.out.println(Arrays.toString(minMaxDouble));

        for (int i : threads) {
            start = System.nanoTime();
            minMaxDouble = minAndMaxWithThreading(myDoubleArray, i);
            stop = System.nanoTime();
            System.out.println("Czas wykonania: " + (stop - start) / 1000000000);
            System.out.println(Arrays.toString(minMaxDouble));
        }
    }

    public static void myIntTest(int[] array, int threads) { // funkcja mierzaca czas dla zadanej tablicy int i zadanej liczby watkow
        double start = System.nanoTime();
        int minMax[] = minAndMaxWithThreading(array, threads);
        double stop = System.nanoTime();
        System.out.println("Czas wykonania: " + (stop - start) / 1000000000);
        System.out.println(Arrays.toString(minMax));
    }

    public static void myDoubleTest(double[] array, int threads) { // funkcja mierzaca czas dla zadanej tablicy double i zadanej liczby watkow
        double start = System.nanoTime();
        double minMaxDouble[] = minAndMaxWithThreading(array, threads);
        double stop = System.nanoTime();
        System.out.println("Czas wykonania: " + (stop - start) / 1000000000);
        System.out.println(Arrays.toString(minMaxDouble));
    }
}
