package org.joelson.mattias.turfgame.apiv4;

import org.joelson.mattias.turfgame.util.URLReader;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RegionsTest {

    @Test
    public void parseRegions() throws IOException {
        List<Region> regions = getRegions();
        assertEquals(304, regions.size());
    }

    private List<Region> getRegions() throws IOException {
        File file = new File(getClass().getResource("/regions.json").getFile());
        FileInputStream input = new FileInputStream(file);
        List<Region> regions = Regions.fromHTML(URLReader.asString(input));
        input.close();
        return regions;
    }

}
