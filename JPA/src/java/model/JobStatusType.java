/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author fabri
 */
public enum JobStatusType {
    UNASSIGNED (-1),
    NOT_STARTED(0),
    STARTED(1),
    CANCELED_BY_HIREE(2),
    CANCELED_BY_HIRER(3),
    DONE(4);

    private final int value;

    private JobStatusType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

