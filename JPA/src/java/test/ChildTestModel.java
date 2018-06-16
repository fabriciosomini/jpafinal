/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import javax.persistence.Id;

/**
 *
 * @author fabri
 */
public class ChildTestModel {
    @Id
    private int childCodigo; 

    public int getChildCodigo() {
        return childCodigo;
    }

    public void setChildCodigo(int childCodigo) {
        this.childCodigo = childCodigo;
    }

    public String getChildNome() {
        return childNome;
    }

    public void setChildNome(String childNome) {
        this.childNome = childNome;
    }
    private String childNome;
}
