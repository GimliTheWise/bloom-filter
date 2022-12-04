package ch.fhnw.dist;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("words.txt wird in Resources Ordner gesucht...");
        InputStream inputStream = Main.class.getResourceAsStream("/words.txt");
        System.out.println("Wörter werden eingelesen...");
        Set<String> wordsToAdd = new HashSet<>();
        try (InputStreamReader instream = new InputStreamReader(inputStream);
             BufferedReader buffer = new BufferedReader(instream)) {
            String line;
            while ((line = buffer.readLine()) != null) {
                wordsToAdd.add(line);
            }
        } catch (Exception e) {
            System.out.println("file not found.");
            return;
        }
        System.out.println(wordsToAdd.size() + " Zeilen in File gefunden.");
        System.out.print("Fehlerwahrscheinlichkeit: ");
        double p = scanner.nextDouble();
        BloomFilter filter = BloomFilter.create(wordsToAdd.size(), p);
        wordsToAdd.forEach(filter::put);

        inputStream = Main.class.getResourceAsStream("/test_words.txt");
        System.out.println("Test Wörter werden eingelesen...");
        int countMightContain = 0;
        int testSize = 0;
        try (InputStreamReader instream = new InputStreamReader(inputStream);
             BufferedReader buffer = new BufferedReader(instream)) {
            String line;
            while ((line = buffer.readLine()) != null) {
                if (!wordsToAdd.contains(line) && filter.mightContain(line)) countMightContain++;
                testSize++;
            }
        } catch (Exception e) {
            System.out.println("file not found.");
            return;
        }
        double calcP = Math.pow(1 - Math.exp(-(double) filter.getK() * filter.getN() / filter.getM()), filter.getK());
        System.out.println("p: " + filter.getP());
        System.out.println("n: " + filter.getN());
        System.out.println("m: " + filter.getM());
        System.out.println("k: " + filter.getK());
        System.out.println("Kalkulierte Fehlerwahrscheinlichkeit anhand gesetzten Params: " + calcP);
        System.out.println("Experimentelle Fehlerwahrscheinlichkeit anhand von Testdataset: " + (double) countMightContain / testSize);
    }
}