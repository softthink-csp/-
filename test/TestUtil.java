package main.java.com.homework.test;

import java.util.Random;

public class TestUtil {

    private static char[] SEED = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
    private static Random rand = new Random();

    public static String generatorRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(SEED[rand.nextInt(62)]);
        }
        return sb.toString();
    }

}
