package com.eazyK;

import java.util.HashMap;

public class CustomThread implements Runnable {

    private int threadId;
    private int counter = 0;
    private String sentence;

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public static HashMap<String, Integer> map;

    {
        map = new HashMap<>();
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public CustomThread(int threadId) {
        this.threadId = threadId;


    }

    public int getThreadId() {
        return threadId;
    }

    @Override
    public void run() {
        this.counter++;
        for (String word : sentence.split("\\W")) {
            if (word.isEmpty()) {
                continue;
            }
            if (map.containsKey(word)) {
                map.put(word, map.get(word) + 1);
            } else {
                map.put(word, 1);
            }
        }
    }
}
