/**
 * @deprecated This module has been moved to {@code net.thenextlvl:version-checker}.
 */
@Deprecated
module core.version {
    requires com.google.gson;
    requires java.net.http;

    requires static org.jspecify;

    exports core.version.github;
    exports core.version.hangar;
    exports core.version.modrinth;
    exports core.version;
}