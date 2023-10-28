package core.paper.gui;

import core.paper.item.ActionItem;
import core.paper.item.ItemBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;

@Getter(AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class GUI implements Listener {
    private final HashMap<Integer, ActionItem.Action> actions = new HashMap<>();
    private final Plugin plugin;
    @Getter(AccessLevel.PROTECTED)
    private boolean disposed;

    public GUI(Plugin plugin, @Nullable Player owner, Component title, int rows) {
        this(Bukkit.createInventory(owner, rows * 9, title), plugin);
    /**
     * Construct a new GUI
     *
     * @param plugin the plugin owning this gui
     * @param owner the player who owns this gui
     * @param title  the title of this gui
     * @param rows   the amount of rows of this gui
     */
        Bukkit.getPluginManager().registerEvents(this, getPlugin());
    }

    public GUI(Plugin plugin, Component title, int rows) {
        this(plugin, null, title, rows);
    }

    /**
     * clears the whole gui

    /**
     * Returns the rows of the gui
     *
     * @return the rows of the gui
     */
    public int getRows() {
        return getSize() / 9;
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
    public int getSize() {
        return getInventory().getSize();
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

    public void open(Player player) {
        checkDisposed();
        player.openInventory(getInventory());
    /**
     * Opens the gui for its owner
     */
    }

    /**
     * Disposes this gui
     */
    public void dispose() {
        dispose(true);
    }

    public void dispose(boolean close) {
        checkDisposed();
        disposed = true;
        if (close) List.copyOf(getInventory().getViewers()).forEach(HumanEntity::closeInventory);
        HandlerList.unregisterAll(this);
    }

    protected void formatDefault() {
        checkDisposed();
        var placeholder = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name("§7-§8/§7-").toGUIItem();
        for (int i = 0; i < getSize(); i++) setSlotIfAbsent(i, placeholder);
    }

    protected void checkDisposed() throws IllegalStateException {
        if (isDisposed()) throw new IllegalStateException("Trying to access disposed GUI");
    }

    @SuppressWarnings("CallToPrintStackTrace")
    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (getInventory().equals(event.getView().getTopInventory())) try {
            if (event.getView().getBottomInventory().equals(event.getClickedInventory())) return;
            var action = getActions().get(event.getSlot());
            if (action != null) action.click(event.getClick(), event.getHotbarButton(), getOwner());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!getInventory().equals(event.getView().getTopInventory())) return;
        if (getInventory().getHolder() != null && !isDisposed()) dispose(false);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPluginDisable(PluginDisableEvent event) {
        if (event.getPlugin().equals(getPlugin()) && !isDisposed()) dispose();
    }
}
