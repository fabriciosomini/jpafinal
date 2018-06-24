/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.util.Random;

/**
 *
 * @author fabri
 */
public class IdHelper {

    public static int generateId() {
        Random rand = new Random();
        int n = rand.nextInt(9999999) + 1;
        return n;
    }

}
