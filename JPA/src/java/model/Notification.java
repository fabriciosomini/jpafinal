/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author fabri
 */
@Entity
public class Notification implements Serializable {

    @Id
    private int id;
    private User hirer;
    private User hiree;
    private String description;
    private NotificationType notificationType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getHirer() {
        return hirer;
    }

    public void setHirer(User hirer) {
        this.hirer = hirer;
    }

    public User getHiree() {
        return hiree;
    }

    public void setHiree(User hiree) {
        this.hiree = hiree;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }
}