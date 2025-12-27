package core.paper.interfaces;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.bukkit.plugin.Plugin;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public sealed interface Interface permits SimpleInterface {
    @Contract(pure = true)
    Layout layout();

    @Contract(pure = true)
    @Nullable Component title();

    @Contract(pure = true)
    @Nullable Consumer<Player> onOpen();

    @Contract(pure = true)
    @Nullable BiConsumer<Player, InventoryCloseEvent.Reason> onClose();

    @Unmodifiable
    @Contract(pure = true)
    Map<Character, ActionItem> slots();

    void open(Player player);

    @Contract(value = " -> new", pure = true)
    Builder toBuilder();

    @Contract(value = " -> new", pure = true)
    static Builder builder() {
        return new SimpleInterface.Builder();
    }

    static void registerHandler(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(InterfaceHandler.INSTANCE, plugin);
    }

    sealed interface Builder permits SimpleInterface.Builder {
        // throws if invalid rows
        @Contract(value = "_ -> this", pure = true)
        Builder rows(@Range(from = 1, to = 6) int rows) throws IllegalArgumentException;

        // throws if invalid slots
        @Contract(value = "_ -> this", pure = true)
        Builder slots(@MagicConstant(intValues = {5, 9, 18, 27, 36, 45, 54}) int slots) throws IllegalArgumentException;

        // throws if invalid type
        @Contract(value = "_ -> this", pure = true)
        Builder type(InventoryType type) throws IllegalArgumentException;

        // throws if invalid type
        @Contract(value = "_ -> this", pure = true)
        Builder type(MenuType type) throws IllegalArgumentException;

        @Contract(value = "_ -> this", pure = true)
        Builder title(@Nullable Component title);

        @Contract(value = "_ -> this", pure = true)
        Builder layout(Layout layout);

        @Contract(value = "_, _, _ -> this", pure = true)
        Builder slot(char c, ItemStack item, ClickAction action);

        @Contract(value = "_, _, _ -> this", pure = true)
        Builder slot(char c, Renderer renderer, ClickAction action);

        @Contract(value = "_ -> this", pure = true)
        Builder onOpen(@Nullable Consumer<Player> handler);

        @Contract(value = "_ -> this", pure = true)
        Builder onClose(@Nullable BiConsumer<Player, InventoryCloseEvent.Reason> handler);

        @Contract(value = " -> new", pure = true)
        Interface build() throws IllegalArgumentException;
    }

    // todo: remove
    static Interface example() {
        return Interface.builder()
                .title(Component.text("Example"))
                .layout(Layout.builder()
                        .pattern("#-#-#-#-#",
                                "-       -",
                                "# abcba #",
                                "-       -",
                                "#-#-x-#-#")
                        .mask('a', context -> ItemStack.of(Material.IRON_INGOT, context.slot()))
                        .mask('b', context -> ItemStack.of(Material.GOLD_INGOT, context.index() + 1))
                        .mask('c', context -> ItemStack.of(Material.DIAMOND, context.row()))
                        .mask('#', context -> ItemStack.of(Material.BLACK_STAINED_GLASS_PANE, context.index() + 1))
                        .mask(' ', context -> ItemStack.of(Material.LIGHT_GRAY_STAINED_GLASS_PANE, context.column()))
                        .mask('-', ItemStack.of(Material.RED_STAINED_GLASS_PANE))
                        .build())
                .rows(5)
                .slot('x', ItemStack.of(Material.BARRIER), ClickAction.of(player -> {
                    System.out.println(player.getName() + " clicked the barrier");
                    player.closeInventory();
                }))
                .onOpen(player -> System.out.println(player.getName() + " opened the inventory"))
                .onClose((player, reason) -> System.out.println(player.getName() + " closed the inventory with reason " + reason))
                .build();
    }
}
