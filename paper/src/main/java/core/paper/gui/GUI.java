package core.paper.gui;

import core.paper.item.ActionItem;
import core.paper.item.ItemBuilder;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.stream.IntStream;

@EqualsAndHashCode
@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
public class GUI implements Listener, InventoryHolder {
    private final HashMap<Integer, ActionItem.Action> actions = new HashMap<>();
    private final Plugin plugin;
    @Getter(AccessLevel.PUBLIC)
    private Inventory inventory;
    private Component title;
    private boolean disposed;

    /**
     * Construct a new GUI
     *
     * @param plugin the plugin owning this gui
     * @param title  the initial title of this gui
     * @param rows   the amount of rows of this gui
     */
    public GUI(Plugin plugin, Component title, int rows) {
        this.inventory = Bukkit.createInventory(this, rows * 9, title);
        this.plugin = plugin;
        this.title = title;
        formatDefault();
        Bukkit.getPluginManager().registerEvents(this, getPlugin());
    }

    /**
     * Sets the title of this gui
     *
     * @param title the new title
     */
    public void title(Component title) {
        var inventory = Bukkit.createInventory(this, getSize(), title);
        inventory.setContents(getInventory().getContents());
        this.inventory = inventory;
        this.title = title;
    }

    /**
     * Returns the size of the gui
     *
     * @return the size of the gui
     */
    public int getSize() {
        return getInventory().getSize();
    }

    /**
     * Clears the entire gui
     */
    public void clear() {
        getInventory().clear();
        getActions().clear();
    }

    /**
     * Formats the gui with the default style
     */
    @ApiStatus.OverrideOnly
    protected void formatDefault() {
        var placeholder1 = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name("§7-§8/§7-");
        var placeholder2 = new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).name("§7-§8/§7-");
        IntStream.range(0, getSize()).forEach(slot -> setSlot(slot, placeholder1));
        IntStream.of(0, 8, getSize() - 1, getSize() - 9).forEach(slot -> setSlot(slot, placeholder2));
    }

    /**
     * Checks if a certain slot contains an item
     *
     * @param slot the slot
     * @return whether the slot is empty or not
     */
    public boolean isEmpty(int slot) {
        return getInventory().getItem(slot) == null;
    }

    /**
     * Stores the item at the given slot of the inventory
     *
     * @param slot The slot where to put the item
     * @param item The item to set
     */
    public void setSlot(int slot, ActionItem item) {
        getActions().put(slot, item.action());
        getInventory().setItem(slot, item.stack());
    }

    /**
     * Stores the item at the given slot of the inventory
     *
     * @param slot The slot where to put the item
     * @param item The item to set
     */
    public void setSlot(int slot, ItemStack item) {
        getInventory().setItem(slot, item);
        getActions().remove(slot);
    }

    /**
     * Stores the item at the given slot of the inventory when empty
     *
     * @param slot The slot where to put the item
     * @param item The item to set
     */
    public void setSlotIfAbsent(int slot, ActionItem item) {
        if (isEmpty(slot)) setSlot(slot, item);
    }

    /**
     * Stores the item at the given slot of the inventory when empty
     *
     * @param slot The slot where to put the item
     * @param item The item to set
     */
    public void setSlotIfAbsent(int slot, ItemStack item) {
        if (isEmpty(slot)) setSlot(slot, item);
    }

    /**
     * Removes the item at the given slot of the inventory
     *
     * @param slot the slot to clear
     */
    public void remove(int slot) {
        getActions().remove(slot);
        getInventory().setItem(slot, null);
    }

    /**
     * Opens the gui for a certain player
     */
    public void open(HumanEntity player) {
        player.openInventory(getInventory());
    }

    @SuppressWarnings("CallToPrintStackTrace")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!equals(event.getInventory().getHolder())) return;
        if (event.getInventory().equals(getInventory())) try {
            if (!event.getInventory().equals(event.getClickedInventory())) return;
            if (!(event.getWhoClicked() instanceof Player player)) return;
            var action = getActions().get(event.getSlot());
            if (action != null) action.click(event.getClick(), event.getHotbarButton(), player);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            event.setCancelled(true);
        }
    }
}
