package core.util;

import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Construct a new properties object
 *
 * @param map      the key - value map
 * @param comments the comments
 */
public record Properties(Map<String, Object> map, Collection<String> comments) {
    /**
     * Construct a new ordered properties object
     *
     * @return the ordered properties object
     */
    public static Properties ordered() {
        return new Properties(new TreeMap<>(), new TreeSet<>());
    }

    /**
     * Construct a new unordered properties object
     *
     * @return the unordered properties object
     */
    public static Properties unordered() {
        return new Properties(new LinkedHashMap<>(), new LinkedList<>());
    }

    /**
     * Reads the content of the input stream directly into the properties
     *
     * @param inputStream the input stream
     * @param charset     the charset
     * @return the properties
     * @throws IOException thrown if something goes wrong
     */
    public Properties read(InputStream inputStream, Charset charset) throws IOException {
        try (var inputStreamReader = new InputStreamReader(inputStream);
             var bufferedReader = new BufferedReader(inputStreamReader)) {
            return read(bufferedReader);
        }
    }

    /**
     * Reads the content of the buffered reader directly into the properties
     *
     * @param bufferedReader the buffered reader
     * @return the properties
     */
    public Properties read(BufferedReader bufferedReader) {
        bufferedReader.lines().forEach(line -> {
            if (!line.strip().startsWith("#")) {
                var split = Arrays.asList(line.split("="));
                if (split.isEmpty() || split.get(0).isEmpty()) return;
                set(split.get(0).stripIndent(), String.join("=", split.subList(1, split.size())));
            } else addComment(line.substring(1).stripIndent());
        });
        return this;
    }

    /**
     * Add a comment
     *
     * @param comment the comment
     */
    public void addComment(String comment) {
        comments().add(comment);
    }

    /**
     * Add a comment if absent
     *
     * @param comment the comment
     * @return whether the comment was added
     */
    public boolean addCommentIfAbsent(String comment) {
        if (hasComment(comment)) return false;
        addComment(comment);
        return true;
    }

    /**
     * Remove a comment
     *
     * @param comment the comment
     */
    public void removeComment(String comment) {
        comments().removeIf((s) -> s.equals(comment));
    }

    /**
     * Get whether a comment does already exist
     *
     * @param comment the comment
     * @return whether the command already exists
     */
    public boolean hasComment(String comment) {
        return comments().contains(comment);
    }

    /**
     * Set a value if the key has none
     *
     * @param key   the key
     * @param value the value to set
     */
    public void setIfAbsent(String key, String value) {
        if (!has(key)) set(key, value);
    }

    /**
     * Set a value if the key has none
     *
     * @param key   the key
     * @param value the value to set
     */
    public void setIfAbsent(String key, Collection<String> value) {
        setIfAbsent(key, String.join(", ", value));
    }

    /**
     * Set a value if the key has none
     *
     * @param key   the key
     * @param value the value to set
     */
    public void setIfAbsent(String key, Boolean value) {
        setIfAbsent(key, value.toString());
    }

    /**
     * Set a value if the key has none
     *
     * @param key   the key
     * @param value the value to set
     */
    public void setIfAbsent(String key, Number value) {
        setIfAbsent(key, value.toString());
    }

    /**
     * Set a value if the key has none
     *
     * @param key   the key
     * @param value the value to set
     */
    public void setIfAbsent(String key, Character value) {
        setIfAbsent(key, value.toString());
    }

    /**
     * Set the key to a value
     *
     * @param key   the key
     * @param value the value to set
     */
    public void set(String key, String value) {
        map().put(key, value);
    }

    /**
     * Set the key to a value
     *
     * @param key   the key
     * @param value the value to set
     */
    public void set(String key, Collection<String> value) {
        map().put(key, String.join(", ", value));
    }

    /**
     * Set the key to a value
     *
     * @param key   the key
     * @param value the value to set
     */
    public void set(String key, Boolean value) {
        map().put(key, String.valueOf(value));
    }

    /**
     * Set the key to a value
     *
     * @param key   the key
     * @param value the value to set
     */
    public void set(String key, Number value) {
        map().put(key, String.valueOf(value));
    }

    /**
     * Set the key to a value
     *
     * @param key   the key
     * @param value the value to set
     */
    public void set(String key, Character value) {
        map().put(key, String.valueOf(value));
    }

    /**
     * Add all properties and comments to this object<br>
     * <i>Duplicate keys will be overridden and duplicate comments may occur</i>
     *
     * @param properties the properties and comments to add to this object
     * @return whether anything was changed
     */
    public boolean addAll(Properties properties) {
        var added1 = comments().addAll(properties.comments());
        var added2 = !properties.map().isEmpty();
        map().putAll(properties.map());
        return added1 && added2;
    }

    /**
     * Merge all properties and comments into this object<br>
     * <i>Duplicate keys and comments will be skipped</i>
     *
     * @param properties the properties and comments to merge into this object
     * @return whether anything was merged
     */
    public boolean merge(Properties properties) {
        var merged = new AtomicBoolean();
        properties.comments().forEach(comment -> {
            if (addCommentIfAbsent(comment)) merged.set(true);
        });
        properties.forEach((key, value) -> {
            if (has(key)) return;
            map.put(key, value);
            merged.set(true);
        });
        return merged.get();
    }

    /**
     * Remove a value
     *
     * @param key the key to remove
     */
    public void removeValue(String key) {
        map().remove(key);
    }

    /**
     * Get whether a key exists
     *
     * @param key the key
     * @return whether the key already exists
     */
    public boolean has(String key) {
        return map().containsKey(key);
    }

    /**
     * Get the value from a key
     *
     * @param key the key
     * @return the value
     */
    @Nullable
    public Object get(String key) {
        return get(key, null);
    }

    @Nullable
    public Object get(String key, @Nullable Object defaultValue) {
        Object o = map().get(key);
        return o != null ? o : defaultValue;
    }

    /**
     * Get the value from a key
     *
     * @param key the key
     * @return the value
     */
    public String getString(String key) {
        return getString(key, "");
    }

    public String getString(String key, String defaultValue) {
        Object o = get(key);
        return o != null ? o.toString() : defaultValue;
    }

    /**
     * Get the value from a key
     *
     * @param key the key
     * @return the value
     */
    public List<String> getStringList(String key) {
        return getStringList(key, new ArrayList<>());
    }

    public List<String> getStringList(String key, List<String> defaultValue) {
        Object o = get(key);
        if (o == null) return defaultValue;
        String s = o.toString();
        return s.isBlank() ? new ArrayList<>() : Arrays.asList(s.split(", "));
    }

    /**
     * Get the value from a key
     *
     * @param key the key
     * @return the value
     */
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

    /**
     * Get the value from a key
     *
     * @param key the key
     * @return the value
     */
    public int getInt(String key) {
        return getInt(key, 0);
    }

    /**
     * Get the value from a key or the default value
     *
     * @param defaultValue the default value
     * @param key          the key
     * @return the value
     */
    public int getInt(String key, int defaultValue) {
        Number number = getNumber(key);
        return number != null ? number.intValue() : defaultValue;
    }

    /**
     * Get the value from a key
     *
     * @param key the key
     * @return the value
     */
    public double getDouble(String key) {
        return getDouble(key, 0);
    }

    /**
     * Get the value from a key or the default value
     *
     * @param defaultValue the default value
     * @param key          the key
     * @return the value
     */
    public double getDouble(String key, double defaultValue) {
        Number number = getNumber(key);
        return number != null ? number.doubleValue() : defaultValue;
    }

    /**
     * Get the value from a key
     *
     * @param key the key
     * @return the value
     */
    public float getFloat(String key) {
        return getFloat(key, 0);
    }

    /**
     * Get the value from a key or the default value
     *
     * @param defaultValue the default value
     * @param key          the key
     * @return the value
     */
    public float getFloat(String key, float defaultValue) {
        Number number = getNumber(key);
        return number != null ? number.floatValue() : defaultValue;
    }

    /**
     * Get the value from a key
     *
     * @param key the key
     * @return the value
     */
    public short getShort(String key) {
        return getShort(key, (short) 0);
    }

    /**
     * Get the value from a key or the default value
     *
     * @param defaultValue the default value
     * @param key          the key
     * @return the value
     */
    public short getShort(String key, short defaultValue) {
        Number number = getNumber(key);
        return number != null ? number.shortValue() : defaultValue;
    }

    /**
     * Get the value from a key
     *
     * @param key the key
     * @return the value
     */
    public byte getByte(String key) {
        return getByte(key, (byte) 0);
    }

    /**
     * Get the value from a key or the default value
     *
     * @param defaultValue the default value
     * @param key          the key
     * @return the value
     */
    public byte getByte(String key, byte defaultValue) {
        Number number = getNumber(key);
        return number != null ? number.byteValue() : defaultValue;
    }

    /**
     * Get the value from a key
     *
     * @param key the key
     * @return the value
     */
    public long getLong(String key) {
        return getLong(key, 0);
    }

    /**
     * Get the value from a key or the default value
     *
     * @param defaultValue the default value
     * @param key          the key
     * @return the value
     */
    public long getLong(String key, long defaultValue) {
        Number number = getNumber(key);
        return number != null ? number.longValue() : defaultValue;
    }

    /**
     * Get the value from a key
     *
     * @param key the key
     * @return the value
     */
    @Nullable
    public Character getCharacter(String key) {
        return getCharacter(key, null);
    }

    /**
     * Get the value from a key or the default value
     *
     * @param defaultValue the default value
     * @param key          the key
     * @return the value
     */
    @Nullable
    public Character getCharacter(String key, @Nullable Character defaultValue) {
        Object o = get(key);
        if (o == null) return defaultValue;
        String s = o.toString();
        return s.isBlank() ? defaultValue : Character.valueOf(s.charAt(0));
    }

    /**
     * Get the value from a key
     *
     * @param key the key
     * @return the value
     */
    public char getChar(String key) {
        return getChar(key, (char) 0);
    }

    /**
     * Get the value from a key or the default value
     *
     * @param defaultValue the default value
     * @param key          the key
     * @return the value
     */
    public char getChar(String key, char defaultValue) {
        Character character = getCharacter(key);
        return character != null ? character : defaultValue;
    }

    /**
     * Get the value from a key
     *
     * @param key the key
     * @return the value
     */
    public char[] getCharArray(String key) {
        return getCharArray(key, new char[0]);
    }

    /**
     * Get the value from a key or the default value
     *
     * @param defaultValue the default value
     * @param key          the key
     * @return the value
     */
    public char[] getCharArray(String key, char[] defaultValue) {
        Object o = get(key);
        return o != null ? o.toString().toCharArray() : defaultValue;
    }

    /**
     * Get the value from a key
     *
     * @param key the key
     * @return the value
     */
    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    /**
     * Get the value from a key or the default value
     *
     * @param defaultValue the default value
     * @param key          the key
     * @return the value
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        Object o = get(key);
        return o != null ? Boolean.parseBoolean(o.toString()) : defaultValue;
    }

    public void forEach(BiConsumer<? super String, ? super Object> action) {
        map().forEach(action);
    }

    /**
     * Loop over all comments
     *
     * @param action the action to apply
     */
    public void forEach(Consumer<? super String> action) {
        comments().forEach(action);
    }

    public boolean removeIf(BiPredicate<String, Object> filter) {
        var removed = false;
        var properties = map().entrySet().iterator();
        while (properties.hasNext()) {
            var next = properties.next();
            if (!filter.test(next.getKey(), next.getValue())) continue;
            properties.remove();
            removed = true;
        }
        return removed;
    }
}
