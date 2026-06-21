import org.jspecify.annotations.NullMarked;

@NullMarked
module core.paper {
    exports core.paper.brigadier.arguments.codecs;
    exports core.paper.brigadier.arguments;
    exports core.paper.brigadier.exceptions;
    exports core.paper.cache;
    exports core.paper.item;
    exports core.paper.scoreboard;

    requires com.google.common;
    requires net.kyori.adventure.api;
    requires net.kyori.adventure.key;
    requires org.bukkit;
    requires org.slf4j;

    requires static com.google.errorprone.annotations;
    requires static org.jetbrains.annotations;
    requires static org.jspecify;
}