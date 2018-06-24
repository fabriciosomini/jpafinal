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
public enum NotificationType {
    REQUEST_ACCEPTED(0),
    REQUEST_ADDED(1);

    private final int value;

    private NotificationType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
