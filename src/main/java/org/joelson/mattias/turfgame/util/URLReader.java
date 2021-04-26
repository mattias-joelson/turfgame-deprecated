package org.joelson.mattias.turfgame.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public final class URLReader {

    private URLReader() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }

    public static String getRequest(String request) throws IOException {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(new URI(request))
                    .GET()
                    .build();
            HttpResponse<String> httpResponse = HttpClient.newBuilder()
                    .build()
                    .send(httpRequest, BodyHandlers.ofString());
            return httpResponse.body();
        } catch (URISyntaxException | InterruptedException e) {
            throw new IOException(e);
        }
    }

    public static String postRequest(String request, String json) throws IOException {
        URL url = new URL(request);
        URLConnection connection = url.openConnection();
        HttpURLConnection httpConnection = (HttpURLConnection) connection;
        httpConnection.setRequestMethod("POST");
        httpConnection.setDoOutput(true);

        byte[] out = json.getBytes();

        httpConnection.setFixedLengthStreamingMode(out.length);
        httpConnection.setRequestProperty("Content-Type", "application/json");
        httpConnection.connect();
        try(OutputStream os = httpConnection.getOutputStream()) {
            os.write(out);
        }

        try (InputStream input = connection.getInputStream()) {
            return readStream(input);
        } catch (IOException ioe) {
            try (InputStream error = httpConnection.getErrorStream()) {
                if (error != null) {
                    String errorMessage = readStream(error);
                    throw new IOException(errorMessage, ioe);
                }
                throw new IOException("Unknown error!", ioe);
            }
        }
    }

    static String readStream(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            StringBuilder builder = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                builder.append(line).append('\n');
                line = reader.readLine();
            }
            return builder.toString();
        }
    }
}
