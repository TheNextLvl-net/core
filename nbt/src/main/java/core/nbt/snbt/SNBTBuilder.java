package core.nbt.snbt;

import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import core.nbt.snbt.adapter.factory.BooleanByteAdapterFactory;
import core.nbt.snbt.adapter.primitive.*;
import core.nbt.snbt.adapter.tag.*;
import core.nbt.tag.*;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.lang.reflect.Type;

@ToString
@EqualsAndHashCode
@Deprecated(forRemoval = true)
public class SNBTBuilder {
    static final GsonBuilder DEFAULT_GSON_BUILDER = new GsonBuilder()
            .registerTypeAdapterFactory(new BooleanByteAdapterFactory())

            .registerTypeAdapter(Byte.class, new ByteAdapter())
            .registerTypeAdapter(Double.class, new DoubleAdapter())
            .registerTypeAdapter(Float.class, new FloatAdapter())
            .registerTypeAdapter(Integer.class, new IntegerAdapter())
            .registerTypeAdapter(Long.class, new LongAdapter())
            .registerTypeAdapter(Short.class, new ShortAdapter())

            .registerTypeAdapter(Tag.class, new TagAdapter())

            .registerTypeAdapter(BooleanTag.class, new BooleanTagAdapter())
            .registerTypeAdapter(ByteArrayTag.class, new ByteArrayTagAdapter())
            .registerTypeAdapter(ByteTag.class, new ByteTagAdapter())
            .registerTypeAdapter(CompoundTag.class, new CompoundTagAdapter())
            .registerTypeAdapter(DoubleTag.class, new DoubleTagAdapter())
            .registerTypeAdapter(FloatTag.class, new FloatTagAdapter())
            .registerTypeAdapter(IntArrayTag.class, new IntArrayTagAdapter())
            .registerTypeAdapter(IntTag.class, new IntTagAdapter())
            .registerTypeAdapter(ListTag.class, new ListTagAdapter())
            .registerTypeAdapter(LongArrayTag.class, new LongArrayTagAdapter())
            .registerTypeAdapter(LongTag.class, new LongTagAdapter())
            .registerTypeAdapter(ShortTag.class, new ShortTagAdapter())
            .registerTypeAdapter(StringTag.class, new StringTagAdapter())
            ;
    private final GsonBuilder gsonBuilder = DEFAULT_GSON_BUILDER;

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
        return new SNBT(this.gsonBuilder.create());
    }
}
