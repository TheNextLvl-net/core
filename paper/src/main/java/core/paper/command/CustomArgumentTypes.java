package core.paper.command;

import com.mojang.brigadier.arguments.ArgumentType;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public final class CustomArgumentTypes {

    public static ArgumentType<OfflinePlayer> cachedOfflinePlayer() {
        return CoreArgumentTypes.offlinePlayer;
    }

    public static ArgumentType<Player> playerExact() {
        return CoreArgumentTypes.playerExact;
    }

    public static ArgumentType<Player> player() {
        return CoreArgumentTypes.player;
    }
}
