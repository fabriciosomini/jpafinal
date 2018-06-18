/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Random;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author fabri
 */
@Entity
public class BaseModel implements Serializable {

    @Id
    protected int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean equals(BaseModel baseModel) {
        return this.id == baseModel.getId();
    }

    public BaseModel() {
        id = generateId();
    }

    private int generateId() {
        Random rand = new Random();

        int n = rand.nextInt(9999999) + 1;
        
        return n;
    }

}
