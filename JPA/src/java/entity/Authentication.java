/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import helper.IdHelper;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author fabri
 */
@Entity
@Table(name = "AUTHENTICATION")
public class Authentication implements Serializable {

    @Id
    @GeneratedValue
    private int id;

    private String sessionId;
    private LocalDate limitDate;

    public LocalDate getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(LocalDate limitDate) {
        this.limitDate = limitDate;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean equals(Authentication baseModel) {
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
