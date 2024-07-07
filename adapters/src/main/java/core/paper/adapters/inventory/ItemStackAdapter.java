package core.paper.adapters.inventory;

import com.google.gson.*;
import core.paper.adapters.api.PaperAdapter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;

/**
 * This adapter uses a simple and short nbt format<br>
 * <i>Example: minecraft:stone{display:{Name:'{"text":"example"}'}} 2</i>
 *
 * @see org.bukkit.inventory.ItemFactory#createItemStack(String)
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemStackAdapter extends PaperAdapter<ItemStack> {
    public static final ItemStackAdapter INSTANCE = new ItemStackAdapter();

    @Override
    public ItemStack deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        var string = element.getAsString();
        var spaceIndex = string.lastIndexOf(' ');
        var amount = Integer.parseInt(string.substring(spaceIndex + 1));
        var itemStack = Bukkit.getItemFactory().createItemStack(string.substring(0, spaceIndex));
        itemStack.setAmount(amount);
        return itemStack;
    }

    @Override
    public JsonElement serialize(ItemStack source, Type type, JsonSerializationContext context) {
        var material = source.getType().getKey().toString();
        var meta = source.getItemMeta().getAsString();
        return new JsonPrimitive(material + meta + " " + source.getAmount());
    }
}
