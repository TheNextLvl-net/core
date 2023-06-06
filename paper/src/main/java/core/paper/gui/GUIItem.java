package core.paper.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public record GUIItem(ItemStack stack, Action action) {
    @FunctionalInterface
    public interface Action {
        /**
         * @param type the click type
         * @param index the clicked slot index
         * @param player the player who clicked
         */
        void click(ClickType type, int index, Player player);
    }

    @FunctionalInterface
    public interface ClickAction extends Action {
        /**
         * @param type the click type
         * @param player the player who clicked
         */
        void click(ClickType type, Player player);

        @Override
        default void click(ClickType type, int index, Player player) {
            click(type, player);
        }
    }

    @FunctionalInterface
    public interface PlayerAction extends ClickAction {

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
    public interface RunAction extends ClickAction {

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
