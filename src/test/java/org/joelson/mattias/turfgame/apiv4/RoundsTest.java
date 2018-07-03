package org.joelson.mattias.turfgame.apiv4;

import org.joelson.mattias.turfgame.util.URLReader;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RoundsTest {
    @Test
    public void parseRounds() throws IOException {
        List<Round> rounds = getRounds();
        assertEquals(7, rounds.size());
    }

    private List<Round> getRounds() throws IOException {
        File file = new File(getClass().getResource("/rounds.json").getFile());
        FileInputStream input = new FileInputStream(file);
        List<Round> rounds = Rounds.fromHTML(URLReader.asString(input));
        input.close();
        return rounds;
    }
}
