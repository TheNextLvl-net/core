package core.paper.item;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import io.papermc.paper.datacomponent.item.Unbreakable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@NullMarked
public class ItemBuilder implements Cloneable {
    private final ItemStack itemStack;

    private ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public static ItemBuilder of(ItemStack itemStack) {
        return new ItemBuilder(itemStack);
    }

    public static ItemBuilder of(Material material) {
        return new ItemBuilder(ItemStack.of(material));
    }

    public static ItemBuilder of(Material material, int amount) {
        return new ItemBuilder(ItemStack.of(material, amount));
    }

    public <T> @Nullable T data(DataComponentType.Valued<T> type) {
        return itemStack.getData(type);
    }

    public <T> Optional<T> optional(DataComponentType.Valued<T> type) {
        return Optional.ofNullable(itemStack.getData(type));
    }

    public <T> ItemBuilder data(DataComponentType.Valued<T> type, T value) {
        itemStack.setData(type, value);
        return this;
    }

    public ItemBuilder data(DataComponentType.NonValued type) {
        itemStack.setData(type);
        return this;
    }

    public ItemBuilder resetData(DataComponentType type) {
        itemStack.resetData(type);
        return this;
    }

    public ItemBuilder unsetData(DataComponentType type) {
        itemStack.unsetData(type);
        return this;
    }

    public ItemBuilder itemName(Component name) {
        return data(DataComponentTypes.ITEM_NAME, name);
    }

    public ItemBuilder resetItemName() {
        return resetData(DataComponentTypes.ITEM_NAME);
    }

    public ItemBuilder customName(Component name) {
        return data(DataComponentTypes.CUSTOM_NAME, name);
    }

    public ItemBuilder resetCustomName() {
        return resetData(DataComponentTypes.CUSTOM_NAME);
    }

    public ItemBuilder lore(List<? extends ComponentLike> lines) {
        return data(DataComponentTypes.LORE, ItemLore.lore(lines));
    }

    public ItemBuilder lore(Component... lore) {
        return lore(Arrays.asList(lore));
    }

    public ItemBuilder prependLore(List<? extends ComponentLike> lines) {
        var lore = new ArrayList<ComponentLike>(lines);
        optional(DataComponentTypes.LORE)
                .map(ItemLore::lines)
                .ifPresent(lore::addAll);
        return lore(lore);
    }

    public ItemBuilder prependLore(Component... lines) {
        return prependLore(Arrays.asList(lines));
    }

    public ItemBuilder appendLore(List<? extends ComponentLike> lines) {
        var lore = new ArrayList<ComponentLike>();
        optional(DataComponentTypes.LORE)
                .map(ItemLore::lines)
                .ifPresent(lore::addAll);
        lore.addAll(lines);
        return lore(lore);
    }

    public ItemBuilder appendLore(Component... lines) {
        return appendLore(Arrays.asList(lines));
    }

    public ItemBuilder resetLore() {
        return resetData(DataComponentTypes.LORE);
    }

    public ItemBuilder rarity(ItemRarity rarity) {
        return data(DataComponentTypes.RARITY, rarity);
    }

    public ItemBuilder resetRarity() {
        return resetData(DataComponentTypes.RARITY);
    }

    public ItemBuilder maxStackSize(int maxStackSize) {
        return data(DataComponentTypes.MAX_STACK_SIZE, maxStackSize);
    }

    public ItemBuilder amount(int amount) {
        itemStack.setAmount(Math.clamp(amount, 0, itemStack.getMaxStackSize()));
        return this;
    }

    public ItemBuilder add(int amount) {
        itemStack.add(amount);
        return this;
    }

    public ItemBuilder subtract(int amount) {
        itemStack.subtract(amount);
        return this;
    }

    public ItemBuilder unbreakable() {
        return unbreakable(false);
    }

    public ItemBuilder unbreakable(boolean showInTooltip) {
        return data(DataComponentTypes.UNBREAKABLE, Unbreakable.unbreakable(showInTooltip));
    }

    public ItemBuilder resetUnbreakable() {
        return resetData(DataComponentTypes.UNBREAKABLE);
    }

    public ItemBuilder customModelData(CustomModelData data) {
        return data(DataComponentTypes.CUSTOM_MODEL_DATA, data);
    }

    public ItemBuilder resetCustomModelData() {
        return resetData(DataComponentTypes.CUSTOM_MODEL_DATA);
    }

    public ItemBuilder enchantmentGlint(boolean glint) {
        return data(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, glint);
    }

    public ItemBuilder addEnchantmentGlint() {
        return enchantmentGlint(true);
    }

    public ItemBuilder removeEnchantmentGlint() {
        return enchantmentGlint(false);
    }

    public ItemBuilder resetEnchantmentGlint() {
        return resetData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE);
    }

    public ItemBuilder hideTooltip() {
        return data(DataComponentTypes.HIDE_TOOLTIP);
    }

    public ItemBuilder showTooltip() {
        return resetData(DataComponentTypes.HIDE_TOOLTIP);
    }

    public ItemBuilder profile(ResolvableProfile profile) {
        return data(DataComponentTypes.PROFILE, profile);
    }

    public ItemBuilder profile(PlayerProfile profile) {
        return profile(ResolvableProfile.resolvableProfile(profile));
    }

    public ItemBuilder profile(OfflinePlayer player) {
        return profile(player.getPlayerProfile());
    }

    public ItemBuilder profile(@Nullable UUID uuid) {
        return data(DataComponentTypes.PROFILE, ResolvableProfile.resolvableProfile()
                .uuid(uuid)
                .build());
    }

    @SuppressWarnings("PatternValidation")
    public ItemBuilder profile(@Nullable String name) {
        return data(DataComponentTypes.PROFILE, ResolvableProfile.resolvableProfile()
                .name(name)
                .build());
    }

    public ItemBuilder profileValue(String base64) {
        return data(DataComponentTypes.PROFILE, ResolvableProfile.resolvableProfile()
                .addProperty(new ProfileProperty("textures", base64))
                .build());
    }

    public ItemBuilder profileUrl(String url) {
        var texture = "{\"textures\":{\"SKIN\":{\"url\":\"" + url + "\"}}}";
        var base64 = Base64.getEncoder().encodeToString(texture.getBytes());
        return profileValue(base64);
    }

    public ActionItem withAction(ActionItem.Action action) {
        return new ActionItem(itemStack, action);
    }

    public ActionItem withAction(ActionItem.ClickAction action) {
        return withAction((ActionItem.Action) action);
    }

    public ActionItem withAction(ActionItem.PlayerAction action) {
        return withAction((ActionItem.Action) action);
    }

    public ActionItem withAction(ActionItem.RunAction action) {
        return withAction((ActionItem.Action) action);
    }

    public ActionItem withAction() {
        return withAction(() -> {
        });
    }

    public ItemStack item() {
        return itemStack;
    }

    @Override
    public ItemBuilder clone() {
        try {
            return (ItemBuilder) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
