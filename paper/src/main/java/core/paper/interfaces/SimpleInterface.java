package core.paper.interfaces;

import com.google.common.base.Preconditions;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

final class SimpleInterface implements Interface {
    private final @Nullable BiConsumer<Player, InventoryCloseEvent.Reason> onClose;
    private final @Nullable Component title;
    private final @Nullable Consumer<Player> onOpen;

    private final Layout layout;
    private final Map<Character, ActionItem> slots;
    private final MenuType type;

    private final @Nullable Item[] items;

    private SimpleInterface(
            MenuType type, @Nullable Component title, Layout layout,
            @Nullable Consumer<Player> onOpen,
            @Nullable BiConsumer<Player, InventoryCloseEvent.Reason> onClose,
            Map<Character, ActionItem> slots
    ) {
        this.type = type;
        this.title = title;
        this.layout = layout;
        this.onOpen = onOpen;
        this.onClose = onClose;
        this.slots = Map.copyOf(slots);

        var column = 1;
        var row = 1;
        var slot = 0;

        var chars = layout.pattern().toCharArray();
        this.items = new Item[chars.length];
        var indices = new HashMap<Character, Integer>();

        for (var c : chars) {
            if (c == '\n') {
                column = 1;
                row++;
                continue;
            }
            indices.compute(c, (k, v) -> v == null ? 0 : v + 1);

            var actionItem = slots.get(c);
            var item = actionItem != null
                    ? actionItem.renderer()
                    : layout.renderer(c);

            if (item != null) {
                var action = actionItem != null ? actionItem.action() : null;
                this.items[slot] = new Item(item, action, indices.get(c), row, column, slot);
            } else {
                this.items[slot] = null;
            }

            column++;
            slot++;
        }
    }

    @Override
    public Layout layout() {
        return layout;
    }

    @Override
    public @Nullable Component title() {
        return title;
    }

    @Override
    public @Nullable Consumer<Player> onOpen() {
        return onOpen;
    }

    @Override
    public @Nullable BiConsumer<Player, InventoryCloseEvent.Reason> onClose() {
        return onClose;
    }

    @Override
    public Map<Character, ActionItem> slots() {
        return Map.copyOf(slots);
    }

    @Override
    public void open(Player player) {
        var view = type.create(player, title);
        var size = view.getTopInventory().getSize();

        for (var item : items) {
            if (item == null) continue;
            var slot = item.slot();
            Preconditions.checkPositionIndex(slot, size, "Inventory slot");
            var context = new SimpleRenderContext(player, item.index(), item.row(), item.column(), slot);
            view.setItem(slot, item.renderer().render(context));
        }

        InterfaceHandler.INSTANCE.setView(player, view, this);
        player.openInventory(view);
    }

    @Override
    public Interface.Builder toBuilder() {
        var builder = new Builder();
        builder.slots.putAll(slots);
        return builder.type(type)
                .title(title)
                .layout(layout)
                .onOpen(onOpen)
                .onClose(onClose);
    }

    public void handleClick(Player player, InventoryClickEvent event) {
        if (!event.getView().getTopInventory().equals(event.getClickedInventory())) return;
        var slot = event.getSlot();
        if (slot < 0 || slot >= items.length) return;
        var item = items[slot];
        if (item == null || item.action() == null) return;
        item.action().click(player, event.getClick(), event.getHotbarButton());
    }

    public record Item(Renderer renderer, @Nullable ClickAction action, int index, int row, int column, int slot) {
    }

    public static final class Builder implements Interface.Builder {
        private @Nullable BiConsumer<Player, InventoryCloseEvent.Reason> onClose = null;
        private @Nullable Component title = null;
        private @Nullable Consumer<Player> onOpen = null;

        private Layout layout = Layout.empty();
        private MenuType type = MenuType.GENERIC_9X3;

        private final Map<Character, ActionItem> slots = new HashMap<>();

        @Override
        public Interface.Builder type(InventoryType type) throws IllegalArgumentException {
            Preconditions.checkArgument(type.getMenuType() != null, "Inventory type %s is not creatable", type);
            this.type = type.getMenuType();
            return this;
        }

        @Override
        public Interface.Builder type(MenuType type) throws IllegalArgumentException {
            this.type = type;
            return this;
        }

        @Override
        public Interface.Builder title(@Nullable Component title) {
            this.title = title;
            return this;
        }

        @Override
        public Interface.Builder layout(Layout layout) {
            this.layout = layout;
            return this;
        }

        @Override
        public Interface.Builder slot(char c, ItemStack item, ClickAction action) {
            var clone = item.clone();
            this.slots.put(c, new ActionItem(context -> clone, action));
            return this;
        }

        @Override
        public Interface.Builder slot(char c, Renderer renderer, ClickAction action) {
            this.slots.put(c, new ActionItem(renderer, action));
            return this;
        }

        @Override
        public Interface.Builder onOpen(@Nullable Consumer<Player> handler) {
            this.onOpen = handler;
            return this;
        }

        @Override
        public Interface.Builder onClose(@Nullable BiConsumer<Player, InventoryCloseEvent.Reason> handler) {
            this.onClose = handler;
            return this;
        }

        @Override
        @SuppressWarnings("MagicConstant")
        public Interface.Builder rows(int rows) throws IllegalArgumentException {
            return slots(rows * 9);
        }

        @Override
        public Interface.Builder slots(int slots) throws IllegalArgumentException {
            this.type = switch (slots) {
                case 9 -> MenuType.GENERIC_9X1;
                case 18 -> MenuType.GENERIC_9X2;
                case 27 -> MenuType.GENERIC_9X3;
                case 36 -> MenuType.GENERIC_9X4;
                case 45 -> MenuType.GENERIC_9X5;
                case 54 -> MenuType.GENERIC_9X6;
                case 5 -> MenuType.HOPPER;
                default -> throw new IllegalArgumentException("Invalid number of slots: " + slots);
            };
            return this;
        }

        @Override
        public Interface build() throws IllegalArgumentException {
            var dimensions = this.dimensions.get(type);
            Preconditions.checkArgument(dimensions != null, "Unsupported menu type: %s", type);
            if (!layout.pattern().isEmpty() || layout.hasMasks() || !slots.isEmpty()) {
                validatePattern(dimensions.getKey(), dimensions.getValue());
            }
            return new SimpleInterface(type, title, layout, onOpen, onClose, slots);
        }

        private final Map<MenuType, Map.Entry<Integer, Integer>> dimensions = Map.ofEntries(
                Map.entry(MenuType.GENERIC_9X1, Map.entry(1, 9)),
                Map.entry(MenuType.GENERIC_9X2, Map.entry(2, 9)),
                Map.entry(MenuType.GENERIC_9X3, Map.entry(3, 9)),
                Map.entry(MenuType.GENERIC_9X4, Map.entry(4, 9)),
                Map.entry(MenuType.GENERIC_9X5, Map.entry(5, 9)),
                Map.entry(MenuType.GENERIC_9X6, Map.entry(6, 9)),
                Map.entry(MenuType.GENERIC_3X3, Map.entry(3, 3)),
                Map.entry(MenuType.CRAFTER_3X3, Map.entry(3, 3)),
                Map.entry(MenuType.ANVIL, Map.entry(1, 3)),
                Map.entry(MenuType.BEACON, Map.entry(1, 1)),
                Map.entry(MenuType.BLAST_FURNACE, Map.entry(1, 1)),
                Map.entry(MenuType.BREWING_STAND, Map.entry(1, 1)),
                Map.entry(MenuType.CRAFTING, Map.entry(3, 3)),
                Map.entry(MenuType.ENCHANTMENT, Map.entry(1, 2)),
                Map.entry(MenuType.FURNACE, Map.entry(3, 1)),
                Map.entry(MenuType.GRINDSTONE, Map.entry(3, 1)),
                Map.entry(MenuType.HOPPER, Map.entry(1, 5)),
                Map.entry(MenuType.SHULKER_BOX, Map.entry(3, 9)),
                Map.entry(MenuType.SMITHING, Map.entry(1, 4)),
                Map.entry(MenuType.SMOKER, Map.entry(3, 1)),
                Map.entry(MenuType.CARTOGRAPHY_TABLE, Map.entry(3, 1)),
                Map.entry(MenuType.STONECUTTER, Map.entry(1, 2))
        );

        private void validatePattern(int rows, int cols) throws IllegalArgumentException {
            var pattern = layout.pattern().split("\n");
            // validate that the pattern has the correct number of rows
            Preconditions.checkArgument(pattern.length == rows, "Invalid number of rows in pattern: found %s but expected %s", pattern.length, rows);
            for (var i = 0; i < pattern.length; i++) {
                // validate that each row has the correct number of columns
                Preconditions.checkArgument(pattern[i].length() == cols, "Invalid number of columns in row %s of pattern: found %s but expected %s", i, pattern[i].length(), cols);
            }

            // create a set of all characters in the pattern (excluding newlines)
            var patternChars = layout.pattern().chars()
                    .mapToObj(c -> (char) c)
                    .filter(c -> c != '\n')
                    .collect(Collectors.toSet());

            // check that all characters in the pattern have a corresponding mask or slot
            patternChars.forEach(c -> {
                Preconditions.checkArgument(c == ' ' || layout.containsMask(c) || slots.containsKey(c), "Character '%s' in pattern has no corresponding mask or slot", c);
            });

            // create a set of all masks and slots
            var maskChars = new HashSet<>(layout.masks().keySet());
            maskChars.addAll(slots.keySet());

            // check that all masks and slots are defined in the pattern
            maskChars.forEach(c -> {
                Preconditions.checkArgument(patternChars.contains(c), "Mask or slot '%s' is not defined in pattern", c);
            });
        }
    }
}
