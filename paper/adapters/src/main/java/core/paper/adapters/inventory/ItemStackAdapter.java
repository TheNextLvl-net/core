package core.paper.adapters.inventory;

import com.google.gson.*;
import core.paper.adapters.api.PaperAdapter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Type;

public abstract class ItemStackAdapter extends PaperAdapter<ItemStack> {

    /**
     * This adapter uses a more complex oop style
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Complex extends ItemStackAdapter {
        public static final ItemStackAdapter INSTANCE = new Complex();

        @Override
        public ItemStack deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            var object = element.getAsJsonObject();
            var material = context.<Material>deserialize(object.get("type"), Material.class);
            var meta = object.has("meta") ? context.<ItemMeta>deserialize(object.get("meta"), ItemMeta.class) : null;
            var amount = object.get("amount").getAsInt();
            var itemStack = new ItemStack(material, amount);
            itemStack.setItemMeta(meta);
            return itemStack;
        }

        @Override
        public JsonElement serialize(ItemStack source, Type type, JsonSerializationContext context) {
            var object = new JsonObject();
            object.add("type", context.serialize(source.getType()));
            if (source.hasItemMeta()) object.add("meta", context.serialize(source.getItemMeta()));
            object.addProperty("amount", source.getAmount());
            return object;
        }
    }

    /**
     * This adapter uses a simple and short nbt format<br>
     * <i>Example: minecraft:stone_sword{display:{Name:"{"text":"Example"}"}} 1</i>
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class NBT extends ItemStackAdapter {
        public static final ItemStackAdapter INSTANCE = new NBT();

        @Override
        public ItemStack deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            var string = element.getAsString();
            var spaceIndex = string.lastIndexOf(' ');
            var amount = Integer.parseInt(string.substring(spaceIndex));
            var itemStack = Bukkit.getItemFactory().createItemStack(string.substring(0, spaceIndex));
            itemStack.setAmount(amount);
            return itemStack;
        }

        @Override
        public JsonElement serialize(ItemStack source, Type type, JsonSerializationContext context) {
            var material = source.getType().getKey().toString();
            var meta = source.getItemMeta().toString();
            return new JsonPrimitive(material + meta + " " + source.getAmount());
        }
    }
}
