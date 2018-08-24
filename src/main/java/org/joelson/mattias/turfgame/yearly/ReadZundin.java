package org.joelson.mattias.turfgame.yearly;

import org.joelson.mattias.turfgame.zundin.Monthly;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ReadZundin {

    public static void main(String[] args) throws IOException {
        // for user
        if (args.length <= 0) {
            System.out.printf("Usage:\n"
                            + "\t%s user [rounds ...]",
                    ReadZundin.class);
            return;
        }
        String userName = args[0];
        System.out.println("Retrieving statistics for '" + userName + "'...");
        // for turns
        int[] rounds = new int[] { 96 };
        // read frut
        Set<Monthly> months = new HashSet<>();
        for (int round : rounds) {
            months.add(Monthly.fromZundin(userName, round));
        }
        System.out.println(months);



        // add
        // write user.html

    }
}
