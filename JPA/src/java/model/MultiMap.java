/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fabri
 */
public class MultiMap<String, Object> {

    private final List<MultiEntry> map;

    public class MultiEntry {

        private String key;
        private Object value;

        public String getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        private void setKey(String key) {
            this.key = key;
        }

        private void setValue(Object value) {
            this.value = value;
        }

    }

    public MultiMap() {
        map = new ArrayList();
    }

    public void put(String key, Object value) {
        MultiEntry multiEntry = new MultiEntry();
        multiEntry.setKey(key);
        multiEntry.setValue(value);
        map.add(multiEntry);
    }

    public List<MultiEntry> entrySet() {
        return map;
    }
}
