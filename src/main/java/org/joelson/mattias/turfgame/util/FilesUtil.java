package org.joelson.mattias.turfgame.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public final class FilesUtil {
    
    private static final String DEFAULT_OUTPUT_DIRECTORY = "output"; //NON-NLS
    private static final OpenOption[] DEFAULT_OPEN_OPTIONS = {
            StandardOpenOption.WRITE,
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING
    };
    
    private FilesUtil() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }
    
    private static Path filenamePath(String... filenameParts) {
        if (filenameParts.length == 0) {
            throw new IllegalArgumentException("At least one filename part must be specified.");
        }
        return Paths.get(DEFAULT_OUTPUT_DIRECTORY, filenameParts);
    }
    
    private static Path createParentDirectory(Path path) throws IOException {
        if (!Files.exists(path.getParent())) {
            Files.createDirectory(path.getParent());
        }
        return path;
    }

    public static OutputStream newDefaultOutputStream(String... filenameParts) throws IOException {
        return Files.newOutputStream(createParentDirectory(filenamePath(filenameParts)), DEFAULT_OPEN_OPTIONS);
    }
    
    public static BufferedWriter newDefaultWriter(String... filenameParts) throws IOException {
        return Files.newBufferedWriter(createParentDirectory(filenamePath(filenameParts)), DEFAULT_OPEN_OPTIONS);
    }
}
