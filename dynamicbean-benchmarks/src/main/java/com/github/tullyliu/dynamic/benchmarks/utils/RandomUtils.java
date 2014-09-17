package com.github.tullyliu.dynamic.benchmarks.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

/**
 * Created by tully on 2014/8/28.
 */
public class RandomUtils {
    public static Random getRandom() {
        return new Random();
    }

    public static boolean randomBoolean() {
        return getRandom().nextBoolean();
    }

    public static byte randomByte() {
        return (byte) getRandom().nextInt();
    }

    public static short randomShort() {
        return (short) getRandom().nextInt();
    }

    public static int randomInt() {
        return getRandom().nextInt();
    }

    public static float randomFloat() {
        return getRandom().nextFloat();
    }

    public static double randomDouble() {
        return getRandom().nextDouble();
    }

    public static long randomLong() {
        return getRandom().nextLong();
    }

    public static String randomString(){
        return RandomStringUtils.randomAlphabetic(10);
    }
}
