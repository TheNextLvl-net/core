package core.paper.adapters.inventory;

import com.google.gson.*;
import core.paper.adapters.api.PaperAdapter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Type;

public class ItemStackAdapter extends PaperAdapter<ItemStack> {
    @Override
    public ItemStack deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        var object = element.getAsJsonObject();
        var material = context.<Material>deserialize(object.get("type"), Material.class);
        var meta = context.<ItemMeta>deserialize(object.get("meta"), ItemMeta.class);
        var amount = object.get("type").getAsInt();
        var itemStack = new ItemStack(material, amount);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Override
    public JsonElement serialize(ItemStack source, Type type, JsonSerializationContext context) {
        var object = new JsonObject();
        object.add("type", context.serialize(source.getType()));
        object.add("meta", context.serialize(source.getItemMeta()));
        object.addProperty("type", source.getAmount());
        return object;
    }
}
