import org.jspecify.annotations.NullMarked;

@NullMarked
module core.paper {
    requires com.google.common;
    requires net.kyori.adventure.key;
    requires net.kyori.adventure;
    requires net.kyori.examination.api;
    requires org.bukkit;
    requires org.slf4j;

    requires static com.google.errorprone.annotations;
    requires static org.jetbrains.annotations;
    requires static org.jspecify;

    exports core.paper.brigadier.arguments.codecs;
    exports core.paper.brigadier.arguments;
    exports core.paper.brigadier.exceptions;
    exports core.paper.cache;
    exports core.paper.gui;
    exports core.paper.item;
    exports core.paper.scoreboard;
}