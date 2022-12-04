package ch.fhnw.dist;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

public class BloomFilter {
    private final int n; // Anzahl n an zu erwarteten Elementen
    private final int m; // Filtergrösse (Anzahl Bits)
    private final int k; // Anzahl an Hashfunktionen
    private final double p; // Fehlerwahrscheinlichkeit (für false positive)
    private final boolean[] filter;

    private BloomFilter(int n, int m, int k, double p) {
        this.n = n;
        this.m = m;
        this.k = k;
        this.p = p;
        this.filter = new boolean[m];
    }

    public static BloomFilter create(int n, double p) {

        int m = (int) Math.ceil(-((n * Math.log(p)) / Math.pow(Math.log(2), 2))); // use Math.ceil or Math.round
        int k = (int) Math.round((((double) m / n) * Math.log(2)));
        return new BloomFilter(n, m, k, p);
    }

    public void put(String s) {
        for (int i = 0; i < k; i++) {
            filter[Math.floorMod(Hashing.murmur3_128(i).hashString(s, Charsets.UTF_8).asInt(), m)] = true; //modulo for case m128 >= m and negativ m128
        }
    }

    public boolean mightContain(String s) {
        int i = 0;
        while (i < k && filter[Math.floorMod(Hashing.murmur3_128(i).hashString(s, Charsets.UTF_8).asInt(), m)])
            i++;
        return i == k;
    }


    public int getN() {
        return n;
    }

    public int getM() {
        return m;
    }

    public int getK() {
        return k;
    }

    public double getP() {
        return p;
    }
}

