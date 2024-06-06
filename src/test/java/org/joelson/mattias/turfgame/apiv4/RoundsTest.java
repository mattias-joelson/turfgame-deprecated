package org.joelson.mattias.turfgame.apiv4;

import org.joelson.mattias.turfgame.util.URLReaderTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoundsTest {
    @Test
    public void parseRounds() throws Exception {
        List<Round> rounds = getRounds();
        assertEquals(7, rounds.size());
    }

    private static List<Round> getRounds() throws Exception {
        return URLReaderTest.readProperties("rounds.json", Rounds::fromJSON);
    }
}
