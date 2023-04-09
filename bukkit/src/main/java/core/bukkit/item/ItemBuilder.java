package core.bukkit.item;

import core.annotation.FieldsAreNullableByDefault;
import core.annotation.MethodsReturnNonnullByDefault;
import core.bukkit.gui.GUIItem;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

@FieldsAreNullableByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ItemBuilder extends ItemStack {
    private GUIItem guiItem;

    /**
     * Defaults stack size to 1, with no extra data
     *
     * @param type item material
     */
    public ItemBuilder(Material type) {
        this(type, 1);
    }

    /**
     * An item builder with no extra data
     *
     * @param type   item material
     * @param amount stack size
     */
    public ItemBuilder(Material type, int amount) {
        this(new ItemStack(type, amount));
    }

    /**
     * Creates a new item builder derived from the specified stack
     *
     * @param stack the stack to copy
     * @throws IllegalArgumentException if the specified stack is null, not an item or
     *                                  returns an item meta not created by the item factory
     */
    public ItemBuilder(ItemStack stack) throws IllegalArgumentException {
        super(stack);
    }

    /**
     * Changes the display name of the item
     *
     * @param name the new display name
     * @return the modified item builder
     */
    public ItemBuilder name(String name) {
        return modify(meta -> meta.setDisplayName(name));
    }

    /**
     * Changes the lore of the item
     * Removes lore when given an empty array
     *
     * @param lore the lore that will be set
     * @return the modified item builder
     */
    public ItemBuilder lore(String... lore) {
        return modify(meta -> {
            if (lore.length == 0) meta.setLore(null);
            else meta.setLore(Arrays.asList(lore));
        });
    }

    /**
     * Appends to the lore of the item
     *
     * @param lore the lore that will be appended
     * @return the modified item builder
     */
    public ItemBuilder appendLore(String... lore) {
        return modify(meta -> {
            var list = new ArrayList<String>();
            if (meta.getLore() != null) list.addAll(meta.getLore());
            list.addAll(Arrays.asList(lore));
            meta.setLore(list);
        });
    }

    /**
     * Defines the owner of the skull meta
     *
     * @param player the player to set
     * @return the modified item builder
     */
    public ItemBuilder head(OfflinePlayer player) {
        return head(player.getPlayerProfile());
    }

    /**
     * Defines the owner of the skull meta
     *
     * @param profile the profile to set
     * @return the modified item builder
     */
    public ItemBuilder head(PlayerProfile profile) {
        return profile.isComplete() ? modify(meta -> {
            if (meta instanceof SkullMeta skull) skull.setOwnerProfile(profile);
        }) : this;
    }

    /**
     * Modifies the {@link ItemMeta} of this builder
     *
     * @param consumer the meta consumer
     * @return the modified item builder
     * @see ItemBuilder#modify(Class, Consumer)
     */
    public ItemBuilder modify(Consumer<? super ItemMeta> consumer) {
        return modify(ItemMeta.class, consumer);
    }

    /**
     * Modifies the {@link ItemMeta} of this builder if the meta is of the specified type
     *
     * @param metaClass the type of meta to edit
     * @param consumer  the meta consumer
     * @param <M>       the meta type
     * @return the modified item builder
     */
    public <M extends ItemMeta> ItemBuilder modify(Class<M> metaClass, Consumer<? super M> consumer) {
        ItemMeta meta = getItemMeta();
        if (!metaClass.isInstance(meta)) return this;
        consumer.accept((M) meta);
        setItemMeta(meta);
        return this;
    }

    /**
     * Creates a new gui item or the existing one if present
     *
     * @param action the gui action
     * @return the gui item for this builder
     */
    public GUIItem toGUIItem(GUIItem.Action action) {
        return guiItem == null ? guiItem = new GUIItem(this, action) : guiItem;
    }

    /**
     * @see ItemBuilder#toGUIItem(GUIItem.Action)
     */
    public GUIItem toGUIItem(GUIItem.PlayerAction action) {
        return toGUIItem((GUIItem.Action) action);
    }

    /**
     * @see ItemBuilder#toGUIItem(GUIItem.Action)
     */
    public GUIItem toGUIItem(GUIItem.RunAction action) {
        return toGUIItem((GUIItem.Action) action);
    }
}
