package fr.Shiranuit.LogForJustice.Crypto;

import java.util.ArrayList;
import java.util.Random;

public class XCiffer {

    private long seed;
    private Random rnd;
    private long offset = 0;

    public XCiffer(long seed) {
        this.seed = seed;
        rnd = new Random(seed);
    }

    public XCiffer(long seed, long offset) {
        this.seed = seed;
        rnd = new Random(seed);
        this.offset = offset;
        for (int i=0; i<offset; i++) {
            rnd.nextInt(255);
        }
    }

    public void setOffset(long offset) {
        this.reset();
        for (int i=0; i<offset; i++) {
            rnd.nextInt(255);
        }
        this.offset = offset;
    }

    public void setSeed(long seed) {
        rnd.setSeed(seed);
    }

    public void reset() {
        rnd.setSeed(this.seed);

    }

    public String ciffer(String text) {
        String result = "";
        for (int i=0; i<text.length(); i++) {
            char c = text.charAt(i);
            int ascii = (int)c;
            int v = rnd.nextInt(255);
            int f = ascii ^ v;
            result += (char)f;
            this.offset++;
        }
        return result;
    }

    public long getOffset() {
        return this.offset;
    }

}

