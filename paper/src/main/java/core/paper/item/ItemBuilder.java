package core.paper.item;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import core.annotation.FieldsAreNullableByDefault;
import core.annotation.MethodsReturnNotNullByDefault;
import core.paper.gui.GUIItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;
import java.util.function.Consumer;

@FieldsAreNullableByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNotNullByDefault
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
    public ItemBuilder name(Component name) {
        return modify(meta -> meta.displayName(name));
    }

    /**
     * Changes the display name of the item
     *
     * @param name the new display name
     * @return the modified item builder
     */
    public ItemBuilder name(String name) {
        return name(Component.text(name));
    }

    /**
     * Changes the lore of the item
     * Removes lore when given an empty array
     *
     * @param lore the lore that will be set
     * @return the modified item builder
     */
    public ItemBuilder lore(Component... lore) {
        return modify(meta -> {
            if (lore.length == 0) meta.lore(null);
            else meta.lore(Arrays.asList(lore));
        });
    }

    /**
     * Changes the lore of the item
     * Removes lore when given an empty text
     * Only for textblocks
     *
     * @param text the textblock that will be set as lore
     * @return the modified item builder
     */
    public ItemBuilder lore(String text) {
        var lines = text.split("\n");
        return lore(Arrays.stream(lines)
                .map(Component::text)
                .toList()
                .toArray(new Component[]{})
        );
    }

    /**
     * Appends to the lore of the item
     *
     * @param lore the lore that will be appended
     * @return the modified item builder
     */
    public ItemBuilder appendLore(Component... lore) {
        return modify(meta -> {
            var list = new ArrayList<Component>();
            if (meta.lore() != null) list.addAll(meta.lore());
            list.addAll(Arrays.asList(lore));
            meta.lore(list);
        });
    }

    /**
     * Appends to the lore of the item
     * Only for textblocks
     *
     * @param text the textblock that will be appended
     * @return the modified item builder
     */
    public ItemBuilder appendLore(String text) {
        var lines = text.split("\n");
        return appendLore(Arrays.stream(lines)
                .map(Component::text)
                .toList()
                .toArray(new Component[]{})
        );
    }

    /**
     * Set item flags which should be ignored when rendering an ItemStack.
     *
     * @param itemFlags The flags which shouldn't be rendered
     * @return the modified item builder
     */
    public ItemBuilder itemFlags(ItemFlag... itemFlags) {
        return modify(meta -> meta.addItemFlags(itemFlags));
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
        return modify(SkullMeta.class, meta -> meta.setPlayerProfile(profile));
    }

    /**
     * Defines the owner of the skull meta
     *
     * @param player the player to set
     * @return the modified item builder
     */
    public ItemBuilder head(String player) {
        return head(Bukkit.getOfflinePlayer(player));
    }

    /**
     * Defines the owner of the skull meta using the head value
     *
     * @param base64 the head value to set
     * @return the modified item builder
     */
    public ItemBuilder headValue(String base64) {
        var id = new UUID(base64.hashCode(), base64.hashCode());
        var profile = Bukkit.createProfile(id);
        profile.getProperties().add(new ProfileProperty("textures", base64));
        return head(profile);
    }

    /**
     * Defines the owner of the skull meta using the url
     *
     * @param url the url to set
     * @return the modified item builder
     */
    public ItemBuilder headURL(String url) {
        var nbt = "{\"textures\":{\"SKIN\":{\"url\":\"" + url + "\"}}}";
        var base64 = Base64.getEncoder().encodeToString(nbt.getBytes());
        return headValue(base64);
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
    public GUIItem toGUIItem(GUIItem.ClickAction action) {
        return toGUIItem((GUIItem.Action) action);
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

    /**
     * @see ItemBuilder#toGUIItem(GUIItem.Action)
     */
    public GUIItem toGUIItem() {
        return toGUIItem(() -> {
        });
    }

    @Override
    public @NotNull ItemBuilder clone() {
        var clone = (ItemBuilder) super.clone();
        if (guiItem != null) clone.guiItem = new GUIItem(clone, guiItem.action());
        return clone;
    }
}
