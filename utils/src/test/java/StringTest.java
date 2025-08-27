import core.util.StringUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StringTest {
    @Test
    public void testIsPalindrome() {
        assertTrue(StringUtil.isPalindrome("racecar"));
        assertFalse(StringUtil.isPalindrome("hello"));
    }

    @Test
    public void testRoman() {
        assertEquals("I", StringUtil.roman(1));
        assertEquals("III", StringUtil.roman(3));
        assertEquals("IV", StringUtil.roman(4));
        assertEquals("IX", StringUtil.roman(9));
        assertEquals("LVIII", StringUtil.roman(58));
        assertEquals("MCMXCIV", StringUtil.roman(1994));
        assertEquals("MMMCMXCIX", StringUtil.roman(3999));
    }
}
