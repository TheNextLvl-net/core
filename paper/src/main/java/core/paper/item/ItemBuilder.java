package core.paper.item;

import com.destroystokyo.paper.profile.PlayerProfile;
import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.datacomponent.item.CustomModelData;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public sealed interface ItemBuilder permits SimpleItemBuilder {
    @Contract(value = "_ -> new", pure = true)
    static ItemBuilder of(ItemStack itemStack) {
        return new SimpleItemBuilder(itemStack);
    }
    
    @Contract(value = "_ -> new", pure = true)
    static ItemBuilder of(ItemType itemType) {
        return new SimpleItemBuilder(itemType);
    }

    @Contract(value = "_ -> new", pure = true)
    static ItemBuilder of(Material material) {
        return of(material, 1);
    }

    @Contract(value = "_, _ -> new", pure = true)
    static ItemBuilder of(Material material, int amount) {
        return new SimpleItemBuilder(material, amount);
    }

    @Contract(pure = true)
    <T> @Nullable T data(DataComponentType.Valued<T> type);

    @Contract(pure = true)
    <T> Optional<T> optional(DataComponentType.Valued<T> type);

    @Contract(value = "_, _ -> this", mutates = "this")
    <T> ItemBuilder data(DataComponentType.Valued<T> type, T value);

    @Contract(value = "_ -> this", mutates = "this")
    ItemBuilder data(DataComponentType.NonValued type);

    @Contract(value = "_ -> this", mutates = "this")
    ItemBuilder resetData(DataComponentType type);

    @Contract(value = "_ -> this", mutates = "this")
    ItemBuilder unsetData(DataComponentType type);

    @Contract(value = "_ -> this", mutates = "this")
    ItemBuilder itemName(Component name);

    @Contract(value = " -> this", mutates = "this")
    ItemBuilder resetItemName();

    @Contract(value = "_ -> this", mutates = "this")
    ItemBuilder customName(Component name);

    @Contract(value = " -> this", mutates = "this")
    ItemBuilder resetCustomName();

    @Contract(value = "_ -> this", mutates = "this")
    ItemBuilder lore(List<? extends ComponentLike> lines);

    @Contract(value = "_ -> this", mutates = "this")
    ItemBuilder lore(Component... lines);

    @Contract(value = "_ -> this", mutates = "this")
    ItemBuilder prependLore(List<? extends ComponentLike> lines);

    @Contract(value = "_ -> this", mutates = "this")
    ItemBuilder prependLore(Component... lines);

    @Contract(value = "_ -> this", mutates = "this")
    ItemBuilder appendLore(List<? extends ComponentLike> lines);

    @Contract(value = "_ -> this", mutates = "this")
    ItemBuilder appendLore(Component... lines);

    @Contract(value = "_ -> this", mutates = "this")
    ItemBuilder lore(ItemLore lore);

    @Contract(value = " -> this", mutates = "this")
    ItemBuilder resetLore();

    @Contract(value = "_ -> this", mutates = "this")
    ItemBuilder rarity(ItemRarity rarity);

    @Contract(value = " -> this", mutates = "this")
    ItemBuilder resetRarity();

    @Contract(value = "_ -> this", mutates = "this")
    ItemBuilder maxStackSize(int maxStackSize);

    @Contract(value = "_ -> this", mutates = "this")
    ItemBuilder amount(int amount);

    @Contract(value = "_ -> this", mutates = "this")
    ItemBuilder add(int amount);

    @Contract(value = "_ -> this", mutates = "this")
    ItemBuilder subtract(int amount);

    @Contract(value = " -> this", mutates = "this")
    ItemBuilder unbreakable();

    @Contract(value = " -> this", mutates = "this")
    ItemBuilder resetUnbreakable();

    @Contract(value = "_ -> this", mutates = "this")
    ItemBuilder customModelData(CustomModelData data);

    @Contract(value = " -> this", mutates = "this")
    ItemBuilder resetCustomModelData();

    @Contract(value = "_ -> this", mutates = "this")
    ItemBuilder enchantmentGlint(boolean glint);

    @Contract(value = " -> this", mutates = "this")
    ItemBuilder resetEnchantmentGlint();

    @Contract(value = " -> this", mutates = "this")
    ItemBuilder hideTooltip();

    @Contract(value = " -> this", mutates = "this")
    ItemBuilder showTooltip();

    @Contract(value = "_ -> this", mutates = "this")
    ItemBuilder profile(ResolvableProfile profile);

    @Contract(value = "_ -> this", mutates = "this")
    ItemBuilder profile(PlayerProfile profile);

    @Contract(value = "_ -> this", mutates = "this")
    ItemBuilder profile(OfflinePlayer player);

    @Contract(value = "_ -> this", mutates = "this")
    ItemBuilder profile(@Nullable UUID uuid);

    @Contract(value = "_ -> this", mutates = "this")
    ItemBuilder profile(@Nullable String name);

    @Contract(value = "_ -> this", mutates = "this")
    ItemBuilder profileValue(String base64);

    @Contract(value = "_ -> this", mutates = "this")
    ItemBuilder profileUrl(String url);

    @Deprecated
    ActionItem withAction(ActionItem.Action action);

    @Deprecated
    ActionItem withAction(ActionItem.ClickAction action);

    @Deprecated
    ActionItem withAction(ActionItem.PlayerAction action);

    @Deprecated
    ActionItem withAction(ActionItem.RunAction action);

    @Deprecated
    ActionItem withAction();

    @Contract(value = " -> new", pure = true)
    ItemStack build();
}
