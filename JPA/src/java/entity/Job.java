/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author fabri
 */
@Entity
public class Job extends BaseModel{

    @OneToMany(mappedBy = "job")
    private List<Notification> notifications;

    private String title;
    private String description;
    private float amountPerHour;
    @ManyToOne
    private User hirer;
    @ManyToOne
    private User acceptedHiree;

    public User getAcceptedHiree() {
        return acceptedHiree;
    }

    public void setAcceptedHiree(User acceptedHiree) {
        this.acceptedHiree = acceptedHiree;
    }
    private List<User> hirees;

    
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

}
