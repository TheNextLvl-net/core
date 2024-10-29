package core.paper.item;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

@NullMarked
public class ItemBuilder extends ItemStack {
    /**
     * Defaults stack size to 1, with no extra data
     *
     * @param type item material
     * @see ItemStack#ItemStack(Material)
     */
    public ItemBuilder(Material type) {
        this(type, 1);
    }

    /**
     * An item builder with no extra data
     *
     * @param type   item material
     * @param amount stack size
     * @see ItemStack#ItemStack(Material, int)
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
     * @see ItemStack#ItemStack(ItemStack)
     */
    public ItemBuilder(ItemStack stack) throws IllegalArgumentException {
        super(stack);
    }

    /**
     * Creates a new item builder with the provided player's head.
     *
     * @param player the player whose head to use
     * @see #head(OfflinePlayer)
     */
    public ItemBuilder(OfflinePlayer player) {
        super(Material.PLAYER_HEAD);
        head(player);
    }

    /**
     * Changes the name of the item
     *
     * @param name the new item name
     * @return the modified item builder
     * @see ItemMeta#itemName(Component)
     */
    public ItemBuilder itemName(Component name) {
        return modify(meta -> meta.itemName(name));
    }

    /**
     * Changes the display name of the item
     *
     * @param name the new display name
     * @return the modified item builder
     * @see ItemMeta#displayName(Component)
     */
    public ItemBuilder displayName(Component name) {
        return modify(meta -> meta.displayName(name));
    }

    /**
     * Sets the food component of the item.
     *
     * @param food the food component to set
     * @return the modified item builder
     * @see ItemMeta#setFood(FoodComponent)
     */
    @SuppressWarnings("UnstableApiUsage")
    public ItemBuilder food(@Nullable FoodComponent food) {
        return modify(meta -> meta.setFood(food));
    }

    /**
     * Changes the rarity of the item.
     *
     * @param rarity the new rarity of the item
     * @return the modified item builder
     * @see ItemMeta#setRarity(ItemRarity)
     */
    public ItemBuilder rarity(@Nullable ItemRarity rarity) {
        return modify(meta -> meta.setRarity(rarity));
    }

    /**
     * Sets the maximum stack size of the item.
     *
     * @param max the maximum stack size, or null to use the default stack size
     * @return the modified ItemBuilder object
     * @see ItemMeta#setMaxStackSize(Integer)
     */
    public ItemBuilder maxStackSize(@Nullable Integer max) {
        return modify(meta -> meta.setMaxStackSize(max));
    }

    /**
     * Sets the amount of the item.
     *
     * @param amount the amount to set
     * @return the modified item builder
     * @see ItemStack#setAmount(int)
     */
    public ItemBuilder amount(int amount) {
        setAmount(Math.clamp(amount, 0, getMaxStackSize()));
        return this;
    }

    /**
     * Increases the amount of the item by the specified value.
     *
     * @param amount the amount to add
     * @return the modified item builder
     * @see ItemStack#add(int)
     * @see #amount(int)
     */
    public ItemBuilder addAmount(int amount) {
        return amount(getAmount() + amount);
    }

    /**
     * Decreases the amount of the item by the specified value.
     *
     * @param amount the amount to subtract
     * @return the modified item builder
     * @see ItemStack#subtract(int)
     * @see #amount(int)
     */
    public ItemBuilder subtractAmount(int amount) {
        return amount(getAmount() - amount);
    }

    /**
     * Makes this item unbreakable.
     *
     * @param unbreakable true if the item should be unbreakable, false otherwise
     * @return the modified item builder
     * @see ItemMeta#setUnbreakable(boolean)
     */
    public ItemBuilder unbreakable(boolean unbreakable) {
        return modify(meta -> meta.setUnbreakable(unbreakable));
    }

    /**
     * Modifies the custom model data of the item.
     *
     * @param data the custom model data value to set
     * @return the modified item builder
     * @see ItemMeta#setCustomModelData(Integer)
     */
    public ItemBuilder customModelData(@Nullable Integer data) {
        return modify(meta -> meta.setCustomModelData(data));
    }

    /**
     * Sets the enchantment glint of the item.
     *
     * @param override true to make it glint, false to hide it, null to reset
     * @return the modified item builder
     * @see ItemMeta#setEnchantmentGlintOverride(Boolean)
     */
    public ItemBuilder enchantmentGlintOverride(@Nullable Boolean override) {
        return modify(meta -> meta.setEnchantmentGlintOverride(override));
    }

    /**
     * Hides or shows the tooltip of the item.
     *
     * @param tooltip true to hide the tooltip, false to show it
     * @return the modified item builder
     * @see ItemMeta#setHideTooltip(boolean)
     */
    public ItemBuilder hideTooltip(boolean tooltip) {
        return modify(meta -> meta.setHideTooltip(tooltip));
    }

    /**
     * Changes the lore of the item
     * Removes lore when given an empty array
     *
     * @param lore the lore that will be set
     * @return the modified item builder
     * @see ItemMeta#lore(List)
     */
    public ItemBuilder lore(Component... lore) {
        return modify(meta -> {
            if (lore.length == 0) meta.lore(null);
            else meta.lore(Arrays.asList(lore));
        });
    }

    /**
     * Appends to the lore of the item
     *
     * @param lore the lore that will be appended
     * @return the modified item builder
     * @see ItemMeta#lore(List)
     * @see #lore(Component...)
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
     * Set item flags which should be ignored when rendering an ItemStack.
     *
     * @param itemFlags The flags which shouldn't be rendered
     * @return the modified item builder
     * @see ItemMeta#addItemFlags(ItemFlag...)
     */
    public ItemBuilder itemFlags(ItemFlag... itemFlags) {
        return modify(meta -> meta.addItemFlags(itemFlags));
    }

    /**
     * Defines the owner of the skull meta
     *
     * @param player the player to set
     * @return the modified item builder
     * @see #head(PlayerProfile)
     */
    public ItemBuilder head(OfflinePlayer player) {
        return head(player.getPlayerProfile());
    }

    /**
     * Defines the owner of the skull meta
     *
     * @param profile the profile to set
     * @return the modified item builder
     * @see SkullMeta#setPlayerProfile(PlayerProfile)
     */
    public ItemBuilder head(PlayerProfile profile) {
        return modify(SkullMeta.class, meta -> meta.setPlayerProfile(profile));
    }

    /**
     * Defines the owner of the skull meta
     * <p>
     * Note: This is a blocking action
     *
     * @param player the player to set
     * @return the modified item builder
     * @see #head(OfflinePlayer)
     * @see Bukkit#getOfflinePlayer(String)
     */
    public ItemBuilder head(String player) {
        return head(Bukkit.getOfflinePlayer(player));
    }

    /**
     * Defines the owner of the skull meta using the head value
     *
     * @param base64 the head value to set
     * @return the modified item builder
     * @see #head(PlayerProfile)
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
     * @see #headValue(String)
     */
    public ItemBuilder headURL(String url) {
        var texture = "{\"textures\":{\"SKIN\":{\"url\":\"" + url + "\"}}}";
        var base64 = Base64.getEncoder().encodeToString(texture.getBytes());
        return headValue(base64);
    }

    /**
     * Modifies the {@link ItemMeta} of this builder
     *
     * @param consumer the meta consumer
     * @return the modified item builder
     * @see ItemStack#editMeta(Consumer)
     * @see #modify(Class, Consumer)
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
     * @see ItemStack#editMeta(Class, Consumer)
     */
    public <M extends ItemMeta> ItemBuilder modify(Class<M> metaClass, Consumer<? super M> consumer) {
        editMeta(metaClass, consumer);
        return this;
    }

    /**
     * Creates a new action item
     *
     * @param action the click action
     * @return the gui item for this builder
     */
    public ActionItem withAction(ActionItem.Action action) {
        return new ActionItem(this, action);
    }

    /**
     * @see ItemBuilder#withAction(ActionItem.Action)
     */
    public ActionItem withAction(ActionItem.ClickAction action) {
        return withAction((ActionItem.Action) action);
    }

    /**
     * @see ItemBuilder#withAction(ActionItem.Action)
     */
    public ActionItem withAction(ActionItem.PlayerAction action) {
        return withAction((ActionItem.Action) action);
    }

    /**
     * @see ItemBuilder#withAction(ActionItem.Action)
     */
    public ActionItem withAction(ActionItem.RunAction action) {
        return withAction((ActionItem.Action) action);
    }

    /**
     * @see ItemBuilder#withAction(ActionItem.Action)
     */
    public ActionItem withAction() {
        return withAction(() -> {
        });
    }

    @Override
    public ItemBuilder clone() {
        return (ItemBuilder) super.clone();
    }
}
