package core.util;

import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Construct a new file downloader
 *
 * @param url         the url to download the file from
 * @param destination the destination to save the file to
 * @since 1.0.0
 */
@NullMarked
public record FileDownloader(URL url, Path destination) {
    /**
     * Download a file from a URL to the given path
     *
     * @throws IOException thrown if something goes wrong
     */
    public void download() throws IOException {
        try (var input = url.openConnection().getInputStream()) {
            Files.write(destination(), input.readAllBytes());
        }
    }
}
