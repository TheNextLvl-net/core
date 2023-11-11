package core.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Range;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringUtil {

    /**
     * The default characters for random strings
     */
    private static final char[] chars = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890".toCharArray();

    /**
     * The mapping between numbers and roman numerals
     */
    private static final Map<Integer, String> symbolMap = new HashMap<>(13);

    static {
        symbolMap.put(1000, "M");
        symbolMap.put(900, "CM");
        symbolMap.put(500, "D");
        symbolMap.put(400, "CD");
        symbolMap.put(100, "C");
        symbolMap.put(90, "XC");
        symbolMap.put(50, "L");
        symbolMap.put(40, "XL");
        symbolMap.put(10, "X");
        symbolMap.put(9, "IX");
        symbolMap.put(5, "V");
        symbolMap.put(4, "IV");
        symbolMap.put(1, "I");
    }

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
     * Get an integer as a roman numeral
     *
     * @param input the integer
     * @return the roman numeral
     */
    public static String roman(@Range(from = 1, to = 3999) int input) {
        var roman = new StringBuilder();
        for (var entry : symbolMap.entrySet()) {
            var value = entry.getKey();
            var symbol = entry.getValue();
            while (input >= value) {
                roman.append(symbol);
                input -= value;
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
        var reversed = new StringBuilder();
        for (var index = string.length() - 1; index >= 0; index--) reversed.append(string.charAt(index));
        return string.contentEquals(reversed);
    }
}
