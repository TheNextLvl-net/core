package core.util;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;

@Getter
public record Properties(Map<String, Object> map, Collection<String> comments) {
    public static Properties ordered() {
        return new Properties(new TreeMap<>(), new TreeSet<>());
    }

    public static Properties unordered() {
        return new Properties(new HashMap<>(), new ArrayList<>());
    }

    public void addComment(String comment) {
        comments().add(comment);
    }

    public void addCommentIfAbsent(String comment) {
        if (!hasComment(comment)) addComment(comment);
    }

    public void removeComment(String comment) {
        comments().removeIf((s) -> s.equals(comment));
    }

    public boolean hasComment(String comment) {
        return comments().contains(comment);
    }

    public void setIfAbsent(String key, String value) {
        if (!has(key)) set(key, value);
    }

    public void setIfAbsent(String key, Collection<String> value) {
        setIfAbsent(key, String.join(", ", value));
    }

    public void setIfAbsent(String key, Boolean value) {
        setIfAbsent(key, value.toString());
    }

    public void setIfAbsent(String key, Number value) {
        setIfAbsent(key, value.toString());
    }

    public void setIfAbsent(String key, Character value) {
        setIfAbsent(key, value.toString());
    }

    public void set(String key, String value) {
        map().put(key, value);
    }

    public void set(String key, Collection<String> value) {
        map().put(key, String.join(", ", value));
    }

    public void set(String key, Boolean value) {
        map().put(key, String.valueOf(value));
    }

    public void set(String key, Number value) {
        map().put(key, String.valueOf(value));
    }

    public void set(String key, Character value) {
        map().put(key, String.valueOf(value));
    }

    public void removeValue(String key) {
        map().remove(key);
    }

    public boolean has(String key) {
        return map().containsKey(key);
    }

    @Nullable
    public Object get(String key) {
        return get(key, null);
    }

    @Nullable
    public Object get(String key, @Nullable Object defaultValue) {
        Object o = map().get(key);
        return o != null ? o : defaultValue;
    }

    public String getString(String key) {
        return getString(key, "");
    }

    public String getString(String key, String defaultValue) {
        Object o = get(key);
        return o != null ? o.toString() : defaultValue;
    }

    public List<String> getStringList(String key) {
        return getStringList(key, new ArrayList<>());
    }

    public List<String> getStringList(String key, List<String> defaultValue) {
        Object o = get(key);
        if (o == null) return defaultValue;
        String s = o.toString();
        return s.isBlank() ? new ArrayList<>() : Arrays.asList(s.split(", "));
    }

    @Nullable
    public Number getNumber(String key) {
        return getNumber(key, null);
    }

    @Nullable
    public Number getNumber(String key, @Nullable Number defaultValue) {
        try {
            Object o = get(key);
            if (o == null) return defaultValue;
            return Double.parseDouble(o.toString());
        } catch (NumberFormatException ignored) {
            return defaultValue;
        }
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        Number number = getNumber(key);
        return number != null ? number.intValue() : defaultValue;
    }

    public double getDouble(String key) {
        return getDouble(key, 0);
    }

    public double getDouble(String key, double defaultValue) {
        Number number = getNumber(key);
        return number != null ? number.doubleValue() : defaultValue;
    }

    public float getFloat(String key) {
        return getFloat(key, 0);
    }

    public float getFloat(String key, float defaultValue) {
        Number number = getNumber(key);
        return number != null ? number.floatValue() : defaultValue;
    }

    public short getShort(String key) {
        return getShort(key, (short) 0);
    }

    public short getShort(String key, short defaultValue) {
        Number number = getNumber(key);
        return number != null ? number.shortValue() : defaultValue;
    }

    public byte getByte(String key) {
        return getByte(key, (byte) 0);
    }

    public byte getByte(String key, byte defaultValue) {
        Number number = getNumber(key);
        return number != null ? number.byteValue() : defaultValue;
    }

    public long getLong(String key) {
        return getLong(key, 0);
    }

    public long getLong(String key, long defaultValue) {
        Number number = getNumber(key);
        return number != null ? number.longValue() : defaultValue;
    }

    @Nullable
    public Character getCharacter(String key) {
        return getCharacter(key, null);
    }

    @Nullable
    public Character getCharacter(String key, @Nullable Character defaultValue) {
        Object o = get(key);
        if (o == null) return defaultValue;
        String s = o.toString();
        return s.isBlank() ? defaultValue : Character.valueOf(s.charAt(0));
    }

    public char getChar(String key) {
        return getChar(key, (char) 0);
    }

    public char getChar(String key, char defaultValue) {
        Character character = getCharacter(key);
        return character != null ? character : defaultValue;
    }

    public char[] getCharArray(String key) {
        return getCharArray(key, new char[0]);
    }

    public char[] getCharArray(String key, char[] defaultValue) {
        Object o = get(key);
        return o != null ? o.toString().toCharArray() : defaultValue;
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        Object o = get(key);
        return o != null ? Boolean.parseBoolean(o.toString()) : defaultValue;
    }

    public void forEach(BiConsumer<? super String, ? super Object> action) {
        map().forEach(action);
    }
}
