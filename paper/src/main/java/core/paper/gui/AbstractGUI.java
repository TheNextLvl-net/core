package core.paper.gui;

import core.paper.item.ActionItem;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
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
import java.util.Map;

/**
 * Represents an abstract GUI that can be used as a base class for creating GUIs.
 *
 * @param <P> the type of the plugin that owns this GUI
 */
@Getter
@EqualsAndHashCode
public abstract class AbstractGUI<P extends Plugin> implements Listener, InventoryHolder {
    private final Map<Integer, ActionItem.Action> actions = new HashMap<>();
    private @Setter(AccessLevel.PROTECTED) Inventory inventory;
    private @Accessors(fluent = true) Component title;
    private final P plugin;

    /**
     * Construct a new AbstractGUI
     *
     * @param plugin the plugin owning this gui
     * @param title  the initial title of this gui
     */
    protected AbstractGUI(P plugin, Component title) {
        this.plugin = plugin;
        this.title = title;
        this.inventory = createInventory();
        Bukkit.getPluginManager().registerEvents(this, getPlugin());
    }

    /**
     * Creates a new inventory.
     *
     * @return the created inventory
     */
    @ApiStatus.OverrideOnly
    protected abstract Inventory createInventory();

    /**
     * Sets the title of this gui
     *
     * @param title the new title
     */
    public void title(Component title) {
        this.title = title;
        var inventory = createInventory();
        inventory.setContents(getInventory().getContents());
        setInventory(inventory);
    }

    /**
     * Clears the entire gui
     */
    public void clear() {
        getInventory().clear();
        actions.clear();
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
