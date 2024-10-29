package core.util;

import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Construct a new file downloader
 *
 * @param url         the url to download the file from
 * @param destination the destination to save the file to
 */
@NullMarked
public record FileDownloader(URL url, Path destination) {
    /**
     * Download a file from an url to the given path
     *
     * @throws IOException thrown if something goes wrong
     */
    public void download() throws IOException {
        URLConnection urlConnection = url.openConnection();
        try (InputStream inputStream = urlConnection.getInputStream()) {
            Files.write(destination(), inputStream.readAllBytes());
        }
    }
}
