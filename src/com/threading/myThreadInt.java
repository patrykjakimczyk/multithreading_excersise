package com.threading;

public class myThreadInt extends Thread{
    public int[] threadArray;
    public int[] threadMinMax;

    myThreadInt(int[] threadArray) {
        this.threadArray = threadArray;
    } // tablica intow dla tego watku

    @Override
    public void run() { // funkcja ktora wykona sie przy wystartowaniu watku czyli gdy wlaczymy .start() dla danego watku
        threadMinMax = Functions.minAndMax(threadArray);
    }
}
