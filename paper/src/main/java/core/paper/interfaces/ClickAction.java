package core.paper.interfaces;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.Contract;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@FunctionalInterface
public interface ClickAction {
    /**
     * Called when an item inside an interface is clicked.
     *
     * @param type   the click type
     * @param index  the clicked slot index
     * @param player the player who clicked
     */
    void click(Player player, ClickType type, int index);

    @Contract(value = "_ -> new", pure = true)
    default ClickAction andThen(ClickAction other) {
        return (player, type, index) -> {
            click(player, type, index);
            other.click(player, type, index);
        };
    }

    @Contract(value = "_ -> new", pure = true)
    static ClickAction of(BiConsumer<Player, ClickType> action) {
        return (player, type, index) -> action.accept(player, type);
    }

    @Contract(value = "_ -> new", pure = true)
    static ClickAction of(Consumer<Player> action) {
        return (player, type, index) -> action.accept(player);
    }

    @Contract(value = "_ -> new", pure = true)
    static ClickAction of(Runnable action) {
        return (player, type, index) -> action.run();
    }
}
