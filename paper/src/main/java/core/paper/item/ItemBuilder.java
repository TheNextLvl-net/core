package core.paper.item;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import core.annotation.FieldsAreNotNullByDefault;
import core.annotation.MethodsReturnNotNullByDefault;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;
import java.util.function.Consumer;

@FieldsAreNotNullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNotNullByDefault
public class ItemBuilder extends ItemStack {
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
     * Creates a new item builder with the provided player's head.
     *
     * @param player the player whose head to use
     */
    public ItemBuilder(OfflinePlayer player) {
        super(Material.PLAYER_HEAD);
        head(player);
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
     * Hides or shows the tooltip of the item.
     *
     * @param tooltip true to hide the tooltip, false to show it
     * @return the modified item builder
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
        var texture = "{\"textures\":{\"SKIN\":{\"url\":\"" + url + "\"}}}";
        var base64 = Base64.getEncoder().encodeToString(texture.getBytes());
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
