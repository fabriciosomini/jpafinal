/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import helper.IdHelper;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import model.NotificationType;

/**
 *
 * @author fabri
 */
@Entity
@Table(name = "NOTIFICATION")
public class Notification implements Serializable {

    @Id
    @GeneratedValue
    private int id;
    private String description;
    private int notificationType;
    
    //Trabalho da notificação
    @ManyToOne
    private Job job;
    
    //Contratador da notificação
    @ManyToOne
    private User hirer;
    
    //Contratado da notificação
    @ManyToOne
    private User hiree;

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public User getHirer() {
        return hirer;
    }

    public User getHiree() {
        return hiree;
    }

    public void setHirer(User hirer) {
        this.hirer = hirer;
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

    public int getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType.getValue();
    }

    public boolean equals(Notification baseModel) {
        return this.id == baseModel.getId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public final int hashCode() {
        return IdHelper.generateId();
    }

}
