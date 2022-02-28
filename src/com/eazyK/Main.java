package com.eazyK;

public class Main {

    public static void main(String[] args) {

        //generating main thread
        MainThread mainThread = new MainThread(1);
        Thread thread = new Thread(mainThread);
        thread.run();
    }
}
