package core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

public class Properties extends java.util.Properties {
    public Properties() {
    }

    public Properties(int initialCapacity) {
        super(initialCapacity);
    }

    public Properties(java.util.Properties defaults) {
        super(defaults);
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
        try (var inputStreamReader = new InputStreamReader(inputStream, charset);
             var bufferedReader = new BufferedReader(inputStreamReader)) {
            load(bufferedReader);
            return this;
        }
    }

    /**
     * Add all properties and comments to this object<br>
     * <i>Duplicate keys will be overridden and duplicate comments may occur</i>
     *
     * @param properties the properties and comments to add to this object
     * @return whether anything was changed
     */
    public boolean addAll(Properties properties) {
        var added = !properties.isEmpty();
        putAll(properties);
        return added;
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
        properties.forEach((key, value) -> {
            if (containsKey(key)) return;
            put(key, value);
            merged.set(true);
        });
        return merged.get();
    }

    /**
     * Remove all properties that satisfy the filter
     *
     * @param filter the filter to apply
     * @return whether anything was removed
     */
    public boolean removeIf(Predicate<Map.Entry<Object, Object>> filter) {
        return entrySet().removeIf(filter);
    }
}
