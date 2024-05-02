package core.paper.gui;

import core.paper.item.ActionItem;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
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
    private @Accessors(fluent = true) Component title;
    protected final @Getter(AccessLevel.NONE) P plugin;
    private final Player owner;

    /**
     * Construct a new AbstractGUI
     *
     * @param plugin the plugin owning this gui
     * @param owner  the player owning this gui
     * @param title  the initial title of this gui
     */
    protected AbstractGUI(P plugin, Player owner, Component title) {
        this.plugin = plugin;
        this.title = title;
        this.owner = owner;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    /**
     * This method should be overridden in subclasses to provide custom behavior when the GUI is closed.
     * It is invoked when the player closes the GUI.
     * <p>
     * Note: Do not call this method directly, it is automatically called by the system.
     */
    @ApiStatus.OverrideOnly
    protected void onClose() {
    }

    /**
     * This method should be overridden in subclasses to provide custom behavior when the GUI is opened.
     * It is invoked when the player opens the GUI.
     * <p>
     * Note: Do not call this method directly, it is automatically called by the system.
     */
    @ApiStatus.OverrideOnly
    protected void onOpen() {
    }

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
     * Opens the gui
     */
    public void open() {
        owner.openInventory(getInventory());
    }

    /**
     * Closes the gui
     */
    public void close() {
        getInventory().close();
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

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (!equals(event.getInventory().getHolder())) return;
        if (!event.getInventory().equals(getInventory())) return;
        if (!event.getPlayer().equals(getOwner())) {
            plugin.getComponentLogger().warn("Tried to open GUI for unauthorized player");
            event.setCancelled(true);
        } else {
            onOpen();
            event.titleOverride(title());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!equals(event.getInventory().getHolder())) return;
        if (!event.getInventory().equals(getInventory())) return;
        HandlerList.unregisterAll(this);
    }
}
