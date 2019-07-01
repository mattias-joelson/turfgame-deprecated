package org.joelson.mattias.turfgame.zundin;

import org.joelson.mattias.turfgame.util.URLReaderTest;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MissionTest {
    
    @Test
    public void jarfallaSnurrTest() throws IOException {
        List<Integer> zones = readJarfallaSnurrZones();
        assertEquals(302, zones.size());
    }

    @Test
    public void soderSnurrTest() throws IOException {
        List<Integer> zones = readSoderSnurrZones();
        assertEquals(517, zones.size());
    }

    @Test
    public void solnaSnurrTest() throws IOException {
        List<Integer> zones = readSolnaSnurrZones();
        assertEquals(256, zones.size());
    }
    
    public static List<Integer> readJarfallaSnurrZones() throws IOException {
        return URLReaderTest.readProperties("/mission_37_jarfalla.html", Mission::fromHTML);
    
    }
    public static List<Integer> readSoderSnurrZones() throws IOException {
        return URLReaderTest.readProperties("/mission_34_sodersnurr.html", Mission::fromHTML);
        
    }
    public static List<Integer> readSolnaSnurrZones() throws IOException {
        return URLReaderTest.readProperties("/mission_33_solna.html", Mission::fromHTML);
        
    }
}
