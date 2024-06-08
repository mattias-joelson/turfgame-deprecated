package org.joelson.turf.turfgame.apiv5;

import org.joelson.turf.util.URLReaderTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ZonesTest {

    public static List<Zone> getAllZones() throws IOException {
        return URLReaderTest.readProperties("zones-all.v5.json", Zones::fromJSON);
    }
}
