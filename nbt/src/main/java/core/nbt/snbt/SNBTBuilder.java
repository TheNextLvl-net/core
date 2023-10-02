package core.nbt.snbt;

import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import core.nbt.snbt.adapter.*;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.lang.reflect.Type;

@ToString
@EqualsAndHashCode
public class SNBTBuilder {
    static final GsonBuilder DEFAULT_GSON_BUILDER = new GsonBuilder()
            .registerTypeAdapter(Byte.class, new ByteAdapter())
            .registerTypeAdapter(Long.class, new LongAdapter())
            .registerTypeAdapter(Short.class, new ShortAdapter())
            .registerTypeAdapter(Float.class, new FloatAdapter())
            .registerTypeAdapter(Integer.class, new IntegerAdapter())
            .registerTypeAdapter(Double.class, new DoubleAdapter());
    private final GsonBuilder gsonBuilder = DEFAULT_GSON_BUILDER;
    private boolean serializeBooleans = false;

    public SNBTBuilder serializeBooleans() {
        this.serializeBooleans = true;
        return this;
    }

    public SNBTBuilder enableComplexMapKeySerialization() {
        gsonBuilder.enableComplexMapKeySerialization();
        return this;
    }

    public SNBTBuilder registerTypeAdapter(Type type, Object typeAdapter) {
        gsonBuilder.registerTypeAdapter(type, typeAdapter);
        return this;
    }

    public SNBTBuilder registerTypeAdapterFactory(TypeAdapterFactory factory) {
        gsonBuilder.registerTypeAdapterFactory(factory);
        return this;
    }

    public SNBTBuilder registerTypeHierarchyAdapter(Class<?> baseType, Object typeAdapter) {
        gsonBuilder.registerTypeHierarchyAdapter(baseType, typeAdapter);
        return this;
    }

    public SNBT create() {
        return new SNBT(
                this.gsonBuilder.create(),
                this.serializeBooleans
        );
    }
}
