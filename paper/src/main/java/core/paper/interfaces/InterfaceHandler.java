package core.paper.interfaces;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryView;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

final class InterfaceHandler implements Listener {
    public static final InterfaceHandler INSTANCE = new InterfaceHandler();

    private final Map<Player, Map.Entry<InventoryView, Interface>> views = new HashMap<>();

    private InterfaceHandler() {
    }

    public Map.@Nullable Entry<InventoryView, Interface> getView(Player player) {
        return views.get(player);
    }

    public void removeView(Player player) {
        views.remove(player);
    }

    public void setView(Player player, InventoryView view, Interface interface_) {
        views.put(player, Map.entry(view, interface_));
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onOpen(InventoryOpenEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;
        var view = getView(player);
        if (view == null || !event.getView().equals(view.getKey())) return;

        var consumer = view.getValue().onOpen();
        if (consumer != null) consumer.accept(player);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;
        var view = getView(player);
        if (view == null || !event.getView().equals(view.getKey())) return;

        var consumer = view.getValue().onClose();
        if (consumer != null) consumer.accept(player, event.getReason());
        removeView(player);
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        var view = getView(player);
        if (view == null || !event.getView().equals(view.getKey())) return;

        // todo: handle click
        event.setCancelled(true);
    }
}
