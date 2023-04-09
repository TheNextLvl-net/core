package core.util;

import org.jetbrains.annotations.Range;

import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;

public class StringUtil {

    private static final char[] chars = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890".toCharArray();

    public static String random(char[] chars, int length) {
        StringBuilder builder = new StringBuilder();
        if (chars.length == 0) return builder.toString();
        for (int i = 0; i < length; i++) builder.append(chars[ThreadLocalRandom.current().nextInt(0, chars.length)]);
        return builder.toString();
    }

    public static String random(int length) {
        return random(chars, length);
    }

    public static String format(String pattern, double number) {
        return new DecimalFormat(pattern).format(number);
    }

    public static byte[][] toByteArray(String... strings) {
        byte[][] bytes = new byte[strings.length][];
        for (int i = 0; i < strings.length; i++) bytes[i] = strings[i].getBytes(StandardCharsets.UTF_8);
        return bytes;
    }

    public static String roman(@Range(from = 1, to = 3999) int input) {
        StringBuilder roman = new StringBuilder();
        while (input > 0) {
            if (input >= 1000) {
                roman.append("M");
                input -= 1000;
            } else if (input >= 900) {
                roman.append("CM");
                input -= 900;
            } else if (input >= 500) {
                roman.append("D");
                input -= 500;
            } else if (input >= 400) {
                roman.append("CD");
                input -= 400;
            } else if (input >= 100) {
                roman.append("C");
                input -= 100;
            } else if (input >= 90) {
                roman.append("XC");
                input -= 90;
            } else if (input >= 50) {
                roman.append("L");
                input -= 50;
            } else if (input >= 40) {
                roman.append("XL");
                input -= 40;
            } else if (input >= 10) {
                roman.append("X");
                input -= 10;
            } else if (input == 9) {
                roman.append("IX");
                input -= 9;
            } else if (input >= 5) {
                roman.append("V");
                input -= 5;
            } else if (input == 4) {
                roman.append("IV");
                input -= 4;
            } else {
                roman.append("I");
                input -= 1;
            }
        }
        return roman.toString();
    }

    public static boolean isPalindrome(String string) {
        StringBuilder reversed = new StringBuilder();
        for (int index = string.length() - 1; index >= 0; index--) reversed.append(string.charAt(index));
        return string.contentEquals(reversed);
    }
}
