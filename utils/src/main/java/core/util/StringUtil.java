package core.util;

import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@NullMarked
public final class StringUtil {

    /**
     * The default characters for random strings
     */
    private static final char[] chars = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890".toCharArray();

    /**
     * The mapping between numbers and roman numerals
     */
    private static final Map<Integer, String> symbolMap = new LinkedHashMap<>(13);

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
     * Generates a random string based on the given characters and length
     *
     * @param chars  the characters to use
     * @param length the length
     * @return the random string
     * @throws IllegalArgumentException if {@code chars} is empty, or {@code length} is negative
     */
    public static String random(char[] chars, @Range(from = 0, to = Integer.MAX_VALUE) int length) {
        if (chars.length == 0) throw new IllegalArgumentException("Chars must not be empty");
        if (length < 0) throw new IllegalArgumentException("Length must be non-negative");

        if (chars.length == 1) return String.valueOf(chars[0]).repeat(length);

        var random = ThreadLocalRandom.current();
        var builder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            builder.append(chars[random.nextInt(0, chars.length)]);
        }

        return builder.toString();
    }

    /**
     * Generates a random alphanumeric string
     *
     * @param length the length
     * @return the random string
     */
    public static String random(int length) {
        return random(chars, length);
    }

    /**
     * Converts an integer to its Roman numeral representation.
     *
     * @param input the integer to be converted must be between 1 and 3999 inclusive
     * @return the Roman numeral representation of the integer
     * @throws IllegalArgumentException if the input is not between 1 and 3999
     */
    @SuppressWarnings("WrapperTypeMayBePrimitive")
    public static String roman(int input) {
        if (input < 1 || input > 3999) throw new IllegalArgumentException("Input must be between 1 and 3999");
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
