package org.joelson.mattias.turfgame.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;

public final class URLReaderTest {

    private URLReaderTest() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }

    public static <R> R readProperties(String resource, ThrowingFunction<String, R, ParseException> function) throws IOException, ParseException {
        File file = new File(URLReaderTest.class.getResource(File.separator + resource).getFile());
        try (FileInputStream input = new FileInputStream(file)) {
            return function.apply(URLReader.readStream(input));
        }
    }
}
