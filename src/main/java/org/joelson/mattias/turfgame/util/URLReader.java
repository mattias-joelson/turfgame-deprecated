package org.joelson.mattias.turfgame.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class URLReader {

    public static String asString(String request) throws IOException {
        URL url = new URL(request);
        URLConnection connection = url.openConnection();

        try (InputStream input = connection.getInputStream()) {
            return asString(input);
        }
    }

    public static String asString(InputStream inputStream) throws IOException {
        StringBuilder htmlBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line = reader.readLine();
        while (line != null) {
            htmlBuilder.append(line).append('\n');
            line = reader.readLine();
        }
        return htmlBuilder.toString();
    }
}