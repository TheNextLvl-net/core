package core.paper.item;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

final class SimpleItemBuilder implements ItemBuilder {
    private final ItemStack itemStack;

    public SimpleItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack.clone();
    }

    public SimpleItemBuilder(ItemType item) {
        this.itemStack = item.createItemStack();
    }

    public SimpleItemBuilder(Material material, int amount) {
        this.itemStack = ItemStack.of(material, amount);
    }

    @Override
    public ItemStack build() {
        return itemStack.clone();
    }

    @Override
    public <T> @Nullable T data(DataComponentType.Valued<T> type) {
        return itemStack.getData(type);
    }

    @Override
    public <T> Optional<T> optional(DataComponentType.Valued<T> type) {
        return Optional.ofNullable(itemStack.getData(type));
    }

    @Override
    public <T> ItemBuilder data(DataComponentType.Valued<T> type, T value) {
        itemStack.setData(type, value);
        return this;
    }

    @Override
    public ItemBuilder data(DataComponentType.NonValued type) {
        itemStack.setData(type);
        return this;
    }

    @Override
    public ItemBuilder resetData(DataComponentType type) {
        itemStack.resetData(type);
        return this;
    }

    @Override
    public ItemBuilder unsetData(DataComponentType type) {
        itemStack.unsetData(type);
        return this;
    }

    @Override
    public ItemBuilder itemName(Component name) {
        return data(DataComponentTypes.ITEM_NAME, name);
    }

    @Override
    public ItemBuilder resetItemName() {
        return resetData(DataComponentTypes.ITEM_NAME);
    }

    @Override
    public ItemBuilder customName(Component name) {
        return data(DataComponentTypes.CUSTOM_NAME, name);
    }

    @Override
    public ItemBuilder resetCustomName() {
        return resetData(DataComponentTypes.CUSTOM_NAME);
    }

    @Override
    public ItemBuilder lore(List<? extends ComponentLike> lines) {
        return data(DataComponentTypes.LORE, ItemLore.lore(lines));
    }

    @Override
    public ItemBuilder lore(ItemLore lore) {
        return data(DataComponentTypes.LORE, lore);
    }

    @Override
    public ItemBuilder lore(Component... lore) {
        return lore(Arrays.asList(lore));
    }

    @Override
    public ItemBuilder prependLore(List<? extends ComponentLike> lines) {
        var lore = new ArrayList<ComponentLike>(lines);
        optional(DataComponentTypes.LORE)
                .map(ItemLore::lines)
                .ifPresent(lore::addAll);
        return lore(lore);
    }

    @Override
    public ItemBuilder prependLore(Component... lines) {
        return prependLore(Arrays.asList(lines));
    }

    @Override
    public ItemBuilder appendLore(List<? extends ComponentLike> lines) {
        var lore = new ArrayList<ComponentLike>();
        optional(DataComponentTypes.LORE)
                .map(ItemLore::lines)
                .ifPresent(lore::addAll);
        lore.addAll(lines);
        return lore(lore);
    }

    @Override
    public ItemBuilder appendLore(Component... lines) {
        return appendLore(Arrays.asList(lines));
    }

    @Override
    public ItemBuilder resetLore() {
        return resetData(DataComponentTypes.LORE);
    }

    @Override
    public ItemBuilder rarity(ItemRarity rarity) {
        return data(DataComponentTypes.RARITY, rarity);
    }

    @Override
    public ItemBuilder resetRarity() {
        return resetData(DataComponentTypes.RARITY);
    }

    @Override
    public ItemBuilder maxStackSize(int maxStackSize) {
        return data(DataComponentTypes.MAX_STACK_SIZE, maxStackSize);
    }

    @Override
    public ItemBuilder amount(int amount) {
        itemStack.setAmount(Math.clamp(amount, 0, itemStack.getMaxStackSize()));
        return this;
    }

    @Override
    public ItemBuilder add(int amount) {
        itemStack.add(amount);
        return this;
    }

    @Override
    public ItemBuilder subtract(int amount) {
        itemStack.subtract(amount);
        return this;
    }

    @Override
    public ItemBuilder unbreakable() {
        return data(DataComponentTypes.UNBREAKABLE);
    }

    @Override
    public ItemBuilder resetUnbreakable() {
        return resetData(DataComponentTypes.UNBREAKABLE);
    }

    @Override
    public ItemBuilder customModelData(CustomModelData data) {
        return data(DataComponentTypes.CUSTOM_MODEL_DATA, data);
    }

    @Override
    public ItemBuilder resetCustomModelData() {
        return resetData(DataComponentTypes.CUSTOM_MODEL_DATA);
    }

    @Override
    public ItemBuilder enchantmentGlint(boolean glint) {
        return data(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, glint);
    }

    @Override
    public ItemBuilder resetEnchantmentGlint() {
        return resetData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE);
    }

    @Override
    public ItemBuilder hideTooltip() {
        return data(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay().hideTooltip(true).build());
    }

    @Override
    public ItemBuilder showTooltip() {
        return resetData(DataComponentTypes.TOOLTIP_DISPLAY);
    }

    @Override
    public ItemBuilder profile(ResolvableProfile profile) {
        return data(DataComponentTypes.PROFILE, profile);
    }

    @Override
    public ItemBuilder profile(PlayerProfile profile) {
        return profile(ResolvableProfile.resolvableProfile(profile));
    }

    @Override
    public ItemBuilder profile(OfflinePlayer player) {
        return profile(player.getPlayerProfile());
    }

    @Override
    public ItemBuilder profile(@Nullable UUID uuid) {
        return data(DataComponentTypes.PROFILE, ResolvableProfile.resolvableProfile()
                .uuid(uuid)
                .build());
    }

    @SuppressWarnings("PatternValidation")
    @Override
    public ItemBuilder profile(@Nullable String name) {
        return data(DataComponentTypes.PROFILE, ResolvableProfile.resolvableProfile()
                .name(name)
                .build());
    }

    @Override
    public ItemBuilder profileValue(String base64) {
        return data(DataComponentTypes.PROFILE, ResolvableProfile.resolvableProfile()
                .addProperty(new ProfileProperty("textures", base64))
                .build());
    }

    @Override
    public ItemBuilder profileUrl(String url) {
        var texture = "{\"textures\":{\"SKIN\":{\"url\":\"" + url + "\"}}}";
        var base64 = Base64.getEncoder().encodeToString(texture.getBytes());
        return profileValue(base64);
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
