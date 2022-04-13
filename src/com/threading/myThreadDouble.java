package com.threading;

public class myThreadDouble extends Thread{
    public double[] threadArray;
    public double[] threadMinMax;

    myThreadDouble(double[] threadArray) {
        this.threadArray = threadArray;
    }

    @Override
    public void run() {
        threadMinMax = Functions.minAndMax(threadArray);
    }
}
