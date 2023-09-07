package core.util;

import core.annotation.FieldsAreNotNullByDefault;
import core.annotation.MethodsReturnNotNullByDefault;
import core.annotation.ParametersAreNotNullByDefault;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;

@FieldsAreNotNullByDefault
@ParametersAreNotNullByDefault
@MethodsReturnNotNullByDefault
public record FileDownloader(URL url, Path destination) {
    public void download() throws IOException {
        URLConnection urlConnection = url.openConnection();
        try (InputStream inputStream = urlConnection.getInputStream()) {
            Files.write(destination(), inputStream.readAllBytes());
        }
    }
}
