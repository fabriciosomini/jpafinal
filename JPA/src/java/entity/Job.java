/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import helper.IdHelper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author fabri
 */
@Entity
@Table(name = "JOB")
public class Job implements Serializable{

    @Id
    @GeneratedValue
    private int id;
    private String title;
    private String description;
    private float amountPerHour;
    @OneToMany(mappedBy = "job")
    private List<Notification> notifications;
    @ManyToOne
    private User hirer;
    @ManyToOne
    private User acceptedHiree;
    @ManyToMany
    private List<User> hirees;

    public Job() {
        hirees = new ArrayList<>();
    }

    public User getAcceptedHiree() {
        return acceptedHiree;
    }

    public void setAcceptedHiree(User acceptedHiree) {
        this.acceptedHiree = acceptedHiree;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getAmountPerHour() {
        return amountPerHour;
    }

    public void setAmountPerHour(float amountPerHour) {
        this.amountPerHour = amountPerHour;
    }

    public User getHirer() {
        return hirer;
    }

    public void setHirer(User hirer) {
        this.hirer = hirer;
    }

    public List<User> getHirees() {
        return hirees;
    }

    public void addHirees(User hiree) {
        this.hirees.add(hiree);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean equals(Job baseModel) {
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
