package org.joelson.mattias.turfgame.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;

public final class URLReader {

    private URLReader() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }

    public static String getRequest(String request) throws IOException {
        URL url = new URL(request);
        URLConnection connection = url.openConnection();

        return readStream(connection::getInputStream);
    }

    private static String readStream(Callable<InputStream> supplier) throws IOException {
        try (InputStream input = supplier.call()) {
            return readStream(input);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    static String readStream(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder htmlBuilder = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                htmlBuilder.append(line).append('\n');
                line = reader.readLine();
            }
            return htmlBuilder.toString();
        }
    }
}