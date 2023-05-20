package core.paper.gui;

import core.paper.item.ItemBuilder;
import core.paper.plugin.CorePlugin;
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
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;

@Getter(AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class GUI implements Listener {
    private final HashMap<Integer, GUIItem> items = new HashMap<>();
    private final Inventory inventory;
    private final CorePlugin plugin;
    @Getter(AccessLevel.PROTECTED)
    private boolean disposed;

    public GUI(CorePlugin plugin, @Nullable Player owner, Component title, int rows) {
        this(Bukkit.createInventory(owner, rows * 9, title), plugin);
        getPlugin().registerListener(this);
    }

    public GUI(CorePlugin plugin, Component title, int rows) {
        this(plugin, null, title, rows);
    }

    /**
     * clears the whole gui
     */
    public void clear() {
        getInventory().clear();
        getItems().clear();
    }

    /**
     * @return The size of the inventory
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
     * Stores the GUIItem at the given slot of the inventory
     *
     * @param slot The slot where to put the item
     * @param item The GUIItem to set
     */
    public void setSlot(int slot, GUIItem item) {
        checkDisposed();
        getItems().put(slot, item);
        getInventory().setItem(slot, item.stack());
    }

    /**
     * Stores the GUIItem at the given slot of the inventory when empty
     *
     * @param slot The slot where to put the item
     * @param item The GUIItem to set
     */
    public void setSlotIfAbsent(int slot, GUIItem item) {
        if (isEmpty(slot)) setSlot(slot, item);
    }

    public void open(Player player) {
        checkDisposed();
        player.openInventory(getInventory());
    }

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
        ItemStack placeholder = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name(Component.text("§7-§8/§7-"));
        for (int i = 0; i < getSize(); i++) {
            if (isEmpty(i)) getInventory().setItem(i, placeholder);
        }
    }

    protected void checkDisposed() throws IllegalStateException {
        if (isDisposed()) throw new IllegalStateException("Trying to access disposed GUI");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (getInventory().equals(event.getView().getTopInventory())) try {
            if (event.getView().getBottomInventory().equals(event.getClickedInventory())) return;
            GUIItem item = getItems().get(event.getSlot());
            if (item != null) item.action().click(event.getClick(), player);
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
