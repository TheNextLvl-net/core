package core.nbt.snbt.adapter.factory;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class BooleanByteAdapterFactory implements TypeAdapterFactory {

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        if (typeToken.getRawType() != Boolean.class && typeToken.getRawType() != boolean.class) return null;
        var defaultAdapter = (TypeAdapter<Boolean>) gson.getDelegateAdapter(this, typeToken);
        return (TypeAdapter<T>) new BooleanByteAdapter(defaultAdapter);
    }

    private static class BooleanByteAdapter extends TypeAdapter<Boolean> {
        private final TypeAdapter<Boolean> defaultAdapter;

        private BooleanByteAdapter(TypeAdapter<Boolean> defaultAdapter) {
            this.defaultAdapter = defaultAdapter;
        }

        @Override
        public void write(JsonWriter out, Boolean value) throws IOException {
            defaultAdapter.write(out, value);
        }

        @Override
        public Boolean read(JsonReader in) throws IOException {
            return JsonToken.NUMBER.equals(in.peek()) ? in.nextInt() == 1 : defaultAdapter.read(in);
        }
    }
}
