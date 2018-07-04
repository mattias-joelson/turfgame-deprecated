package org.joelson.mattias.turfgame.apiv4;

import org.joelson.mattias.turfgame.util.URLReaderTest;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RoundsTest {
    @Test
    public void parseRounds() throws IOException {
        List<Round> rounds = getRounds();
        assertEquals(7, rounds.size());
    }

    private static List<Round> getRounds() throws IOException {
        return URLReaderTest.readProperties("/rounds.json", Rounds::fromHTML);
    }
}
