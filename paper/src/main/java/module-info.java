module core.paper {
    requires com.google.common;
    requires net.kyori.adventure.key;
    requires net.kyori.adventure;
    requires net.kyori.examination.api;
    requires org.bukkit;
    requires org.slf4j;

    requires static org.jetbrains.annotations;
    requires static org.jspecify;
    requires core.version;

    exports core.paper.cache;
    exports core.paper.command;
    exports core.paper.command.argument;
    exports core.paper.command.argument.codec;
    exports core.paper.gui;
    exports core.paper.item;
    exports core.paper.messenger;
    exports core.paper.scoreboard;
    exports core.paper.version;
}