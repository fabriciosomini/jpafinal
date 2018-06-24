/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.util.Random;
import java.util.UUID;

/**
 *
 * @author fabri
 */
public class IdHelper {

    public static int generateId() {
        Random rand = new Random();
        int n = rand.nextInt(999999999) + 1;
        return n;
    }

    public static String generateUUID() {
        UUID uuid = UUID.randomUUID();

        return (digits(uuid.getMostSignificantBits() >> 32, 8)
                + digits(uuid.getMostSignificantBits() >> 16, 4)
                + digits(uuid.getMostSignificantBits(), 4)
                + digits(uuid.getLeastSignificantBits() >> 48, 4)
                + digits(uuid.getLeastSignificantBits(), 12));

    }

    private static String digits(long val, int digits) {
        long hi = 1L << (digits * 4);
        return Long.toHexString(hi | (val & (hi - 1))).substring(1);
    }
}
