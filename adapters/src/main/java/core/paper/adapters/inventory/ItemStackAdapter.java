package core.paper.adapters.inventory;

import com.google.gson.*;
import core.paper.adapters.api.PaperAdapter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

@NullMarked
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemStackAdapter {
    public static Component component() {
        return new Component();
    }

    public static DFU dataFixerUpper() {
        return new DFU();
    }

    /**
     * This adapter uses the user-friendly vanilla format<br>
     * <i>Example: minecraft:stone[custom_name='["example"]'] 2</i>
     * <p>
     * <b>Only ever use this if you really need user configurable items</b>
     *
     * @see org.bukkit.inventory.ItemFactory#createItemStack(String) ItemFactory.createItemStack(String)
     * @see DFU
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Component implements PaperAdapter<ItemStack> {
        @Override
        public @Nullable ItemStack deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            if (!element.isJsonPrimitive()) return null;
            var string = element.getAsString();
            var spaceIndex = string.lastIndexOf(' ');
            var amount = Integer.parseInt(string.substring(spaceIndex + 1));
            var itemStack = Bukkit.getItemFactory().createItemStack(string.substring(0, spaceIndex));
            itemStack.setAmount(amount);
            return itemStack;
        }

        @Override
        public JsonElement serialize(@Nullable ItemStack source, Type type, JsonSerializationContext context) {
            if (source == null) return JsonNull.INSTANCE;
            var material = source.getType().getKey().toString();
            var meta = source.getItemMeta().getAsComponentString();
            return new JsonPrimitive(material + meta + " " + source.getAmount());
        }
    }

    /**
     * This adapter uses proper de/serialization which is backed by the
     * <a href=https://github.com/Mojang/DataFixerUpper>DataFixerUpper</a>
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class DFU implements PaperAdapter<ItemStack> {
        @Override
        public @Nullable ItemStack deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            return element.isJsonPrimitive() ? ItemStack.deserializeBytes(element.getAsString().getBytes(StandardCharsets.UTF_8)) : null;
        }

        @Override
        public JsonElement serialize(@Nullable ItemStack source, Type type, JsonSerializationContext context) {
            return source != null ? new JsonPrimitive(new String(source.serializeAsBytes())) : JsonNull.INSTANCE;
        }
    }
}
