module i18n {
    requires core.files;
    requires net.kyori.adventure.key;
    requires net.kyori.adventure.text.minimessage;
    requires net.kyori.adventure;
    requires net.kyori.examination.api;
    requires org.slf4j;
    
    requires static org.jetbrains.annotations;
    requires static org.jspecify;

    exports core.i18n.file;
}