package core.paper.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public record GUIItem(ItemStack stack, Action action) {
    @FunctionalInterface
    public interface Action {
        /**
         * @param type the click type
         * @param player the player who clicked
         */
        void click(ClickType type, Player player);
    }

    @FunctionalInterface
    public interface PlayerAction extends Action {

        /**
         * This method does only provide a player
         *
         * @param player the player who clicked
         */
        void click(Player player);

        @Override
        default void click(ClickType type, Player player) {
            click(player);
        }
    }

    @FunctionalInterface
    public interface RunAction extends Action {

        /**
         * This method serves as a runnable
         */
        void click();

        @Override
        default void click(ClickType type, Player player) {
            click();
        }
    }
}
