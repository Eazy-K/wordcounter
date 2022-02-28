package com.eazyK;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainThread implements Runnable {
    private int threadNumber;
    List<CustomThread> threadList = new ArrayList<>();

    public MainThread(int threadNumber) {
        this.threadNumber = threadNumber;
    }

    @Override
    public void run() {
        setThreadNumber();  //determining thread count
        String context = readFile();    //reading input file
        String[] sentences = getSentences(context).toArray(new String[0]); //defines sentences in the file.
        getWords(sentences);   //separating sentences into words
        subThreadsTasks(sentences); //generating sub threads and related calculation
        groupingWords();    // grouping and showing words

    }

    private void groupingWords() {
        Map<String, Integer> finalMap = new LinkedHashMap<>();
        CustomThread.map.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue()
                        .reversed()).forEachOrdered(e -> finalMap.put(e.getKey(), e.getValue()));
        System.out.println("Thread counts:");
        for (CustomThread customThread : threadList) {
            System.out.println("ThreadId= " + customThread.getThreadId() + ", Count=" + customThread.getCounter());
        }
        finalMap.entrySet().stream().forEach(System.out::println);
    }

    private void subThreadsTasks(String[] sentences) {
        try {
            generateThread(threadNumber, sentences);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void getWords(String[] sentences) {
        int sentenceCount = 0;
        int tempLength = 0;

        for (String sentence : sentences) {
            String[] words = sentence.split(" ");
            tempLength += words.length;
        }

        sentenceCount = tempLength / sentences.length;
        System.out.println("Avg. Word Count : " + sentenceCount);

    }

    private List<String> getSentences(String context) {

        int sentenceCount = 0;

        List<String> fullSentence = List.of(context.split("(?<!\\w\\.\\w.)(?<![A-Z][a-z]\\.)(?<=\\.|\\?)\\s"));
        sentenceCount = fullSentence.size();

        System.out.println("Sentence Count : " + sentenceCount);

        return fullSentence;
    }


    private String readFile() {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            File file = new File("src/com/eazyK/source/example.txt");
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                stringBuilder.append(data);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    private void generateThread(int threadNumber, String[] clause) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(threadNumber);
        List<String> sentenceList = List.of(clause);
        for (int source = 0; source < threadNumber; source++) {
            threadList.add(new CustomThread(source));
        }

        for (int source = 0; source < clause.length; source++) {
            int runningThread = (source + 1) % threadList.size();
            threadList.get(runningThread).setSentence(sentenceList.get(source));
            pool.execute(threadList.get(runningThread));
            Thread.sleep(250);

        }

        pool.shutdown();

    }

    private void setThreadNumber() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Press y to enter your thread count, press any key to continue without entering thread count. : ");
        String entry = scanner.nextLine();
        entry = entry.toLowerCase(Locale.ROOT);

        switch (entry) {
            case "y":
                threadNumber = scanner.nextInt();
                break;
            default:
                threadNumber = 5;
                break;
        }
    }
}
