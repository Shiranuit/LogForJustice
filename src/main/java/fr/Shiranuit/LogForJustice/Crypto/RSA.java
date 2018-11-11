package fr.Shiranuit.LogForJustice.Crypto;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

public class RSA {

    public enum TYPE {
        SHORT,
        INT,
        LONG
    }

    public static Keys Genkeys(int size) {
        BigInteger e = BigInteger.probablePrime(size, new SecureRandom());
        BigInteger p = BigInteger.probablePrime(size, new SecureRandom());
        BigInteger q = BigInteger.probablePrime(size, new SecureRandom());
        BigInteger n = p.multiply(q);
        BigInteger PhiN = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        BigInteger d = e.modInverse(PhiN);
        return new Keys(Hex(e), Hex(d), Hex(n));
    }

    public static String Hex(BigInteger v) {
        return v.toString(16);
    }

    public static String Hex(int v) {
        return new BigInteger(String.valueOf(v)).toString(16);
    }

    public static String Ciffer(int val, Keys key) {
        BigInteger e = new BigInteger(key.pub,  16);
        BigInteger n = new BigInteger(key.n,  16);
        BigInteger v = new BigInteger(val+"");
        v = v.modPow(e, n);
        return v.toString(16);
    }

    public static String Ciffer(short val, Keys key) {
        BigInteger e = new BigInteger(key.pub,  16);
        BigInteger n = new BigInteger(key.n,  16);
        BigInteger v = new BigInteger(val+"");
        v = v.modPow(e, n);
        return v.toString(16);
    }

    public static String Ciffer(long val, Keys key) {
        BigInteger e = new BigInteger(key.pub,  16);
        BigInteger n = new BigInteger(key.n,  16);
        BigInteger v = new BigInteger(val+"");
        v = v.modPow(e, n);
        return v.toString(16);
    }

    public static long Unciffer(String val, Keys key) {
        BigInteger d = new BigInteger(key.priv,  16);
        BigInteger n = new BigInteger(key.n,  16);
        BigInteger v = new BigInteger(val, 16);
        v = v.modPow(d, n);
        return v.longValue();
    }

    public static String Crypt(String text, Keys key) {
        String result = "";
        BigInteger e = new BigInteger(key.pub,  16);
        BigInteger n = new BigInteger(key.n,  16);
        ArrayList<String> blocs = new ArrayList<String>();
        int count = (int)Math.ceil(text.length()/4d);
        int[] data = new int[count];
        int[] buf = new int[4];
        int pos = 0;
        int posdata = 0;
        for (int i=0; i<text.length();  i++) {
            char c = text.charAt(i);
            int ascii = (int)c;
            if (pos != 4) {
                buf[pos] = ascii;
            } else {
                pos=0;
                data[posdata] = encode(buf);
                posdata++;
                buf = new int[4];
                buf[0] = ascii;
            }
            pos++;
        }
        data[posdata] = encode(buf);

        for (int v : data) {
            BigInteger b = new BigInteger(v+"");
            b = b.modPow(e, n);
            blocs.add(b.toString(16));
        }

        String[] blocS = new String[blocs.size()];
        blocS = blocs.toArray(blocS);

        return String.join(" ", blocS);
    }

    public static String Decrypt(String ciffer, Keys key) {
        String result = "";
        BigInteger d = new BigInteger(key.priv,  16);
        BigInteger n = new BigInteger(key.n,  16);
        String[] text = ciffer.split(" ");

        for (int i=0; i<text.length;  i++) {
            BigInteger b = new BigInteger(text[i],16);
            int v = b.modPow(d, n).intValue();
            int[] data = decode(v, 4);
            for (int j=0; j<data.length; j++) {
                result += (char)data[j];
            }
        }

        return result;
    }

    public  static int encode(int[] vals) {
        int f = 0;
        for (int i=0; i<vals.length; i++) {
            f = (f<<8)|vals[i];
        }
        return f;
    }

    public static int[] decode(int val, int size) {
        int[] vals = new int[size];
        for (int i=0; i<size; i++) {
            vals[i] = (val >> ((size-i-1)*8)) & 255;
        }
        return vals;
    }

}

