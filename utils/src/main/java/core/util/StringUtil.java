package core.util;

import org.jetbrains.annotations.Range;

import java.util.concurrent.ThreadLocalRandom;

public class StringUtil {

    /**
     * The default characters for random strings
     */
    private static final char[] chars = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890".toCharArray();

    /**
     * Generate a random string
     *
     * @param chars  the characters to use
     * @param length the length
     * @return the random string
     */
    public static String random(char[] chars, int length) {
        StringBuilder builder = new StringBuilder();
        if (chars.length == 0) return builder.toString();
        for (int i = 0; i < length; i++) builder.append(chars[ThreadLocalRandom.current().nextInt(0, chars.length)]);
        return builder.toString();
    }

    /**
     * Generate a random string
     *
     * @param length the length
     * @return the random string
     */
    public static String random(int length) {
        return random(chars, length);
    }

    /**
     * Get an integer as a roman number
     *
     * @param input the integer
     * @return the roman number
     */
    public static String roman(@Range(from = 1, to = 3999) int input) {
        var roman = new StringBuilder();
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

    /**
     * Whether a string is a palindrome<br/>
     * <i>same forwards and backwards</i>
     *
     * @param string the string
     * @return whether the string is a palindrome
     */
    public static boolean isPalindrome(String string) {
        StringBuilder reversed = new StringBuilder();
        for (int index = string.length() - 1; index >= 0; index--) reversed.append(string.charAt(index));
        return string.contentEquals(reversed);
    }
}
