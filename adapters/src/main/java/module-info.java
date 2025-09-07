module core.adapters {
    requires com.google.gson;
    requires net.kyori.adventure.key;
    requires net.kyori.examination.api;
    requires org.bukkit;

    requires static org.jspecify;

    exports core.paper.adapters.api;
    exports core.paper.adapters.inventory;
    exports core.paper.adapters.key;
    exports core.paper.adapters.player;
    exports core.paper.adapters.plugin;
    exports core.paper.adapters.world;
}