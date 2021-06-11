package org.joelson.mattias.turfgame.apiv4;

import org.joelson.mattias.turfgame.util.JSONArray;
import org.joelson.mattias.turfgame.util.JSONObject;
import org.joelson.mattias.turfgame.util.JSONValue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FeedsReader {

    public static void main(String[] args) throws IOException, ParseException {
        if (args.length < 1) {
            System.out.println(String.format("Usage:\n\t%s feed_file1.json ...", ZonesCompare.class.getName()));
            return;
        }
        for (String filename : args) {
            System.out.println("*** Reading " + filename);
            readFeedFile(Path.of(filename));
        }
    }

    private static void readFeedFile(Path feedPath) throws IOException, ParseException {
        JSONArray array = JSONArray.parseArray(Files.readString(feedPath));
//        System.out.println(array);
        for (JSONValue value : array.getElements()) {
            JSONObject object = (JSONObject) value;
            System.out.println("    " + object.getValue("time"));
        }
    }
}
