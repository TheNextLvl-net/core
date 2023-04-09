package core.util;

import java.util.Random;

public class MathUtil {

    public static <N extends Number> boolean isInLine(N line, N number) {
        return number.doubleValue() % line.doubleValue() == 0;
    }

    public static boolean chance(double chance) {
        return randomDouble(0d, 99d) < chance;
    }

    public static boolean chance(long chance) {
        return randomLong(0L, 99L) < chance;
    }

    public static boolean chance(float chance) {
        return randomFloat(0F, 99F) < chance;
    }

    public static boolean chance(int chance) {
        return randomInteger(0, 99) < chance;
    }

    public static int randomInteger(int from, int to) {
        return (new Random().nextInt(to + 1 - from) + from);
    }

    public static double randomDouble(double from, double to) {
        return from + Math.random() * to;
    }

    public static float randomFloat(float from, float to) {
        return ((from + (to - from)) * (new Random().nextFloat()));
    }

    public static long randomLong(long from, long to) {
        return ((from + (to - from)) * (new Random().nextLong()));
    }
}
