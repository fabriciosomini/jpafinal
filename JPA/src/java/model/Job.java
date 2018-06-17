/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author fabri
 */
@Entity
public class Job implements Serializable {

    @Id
    private int id;
    private String title;
    private String description;
    private float amountPerHour;
    private User hirer;
    private List<User> hirees;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

}
