package org.joelson.mattias.turfgame.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Locale;

public class CSVWriter extends Writer {

    private final PrintWriter out;

    public CSVWriter(String... filenameParts) throws IOException {
        out = new PrintWriter(FilesUtil.newDefaultWriter(filenameParts));
        writeHeader();
    }

    private void writeHeader() {
        out.println("longitude,latitude,name");
    }

    public void writePlacemark(String name, double longitude, double latitude) {
        out.println(String.format(Locale.ENGLISH, "%f,%f,%s", longitude, latitude, name));
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        out.write(cbuf, off, len);
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }

    @Override
    public void close() throws IOException {
        out.close();
    }
}
