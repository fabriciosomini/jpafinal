/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author fabri
 */

@Entity
public class TestModel {
    
    @Id
    private int codigo; 
    private String nome;
    //private List<ChildTestModel> children;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    /*public List<ChildTestModel> getChildren() {
        return children;
    }

    public void setChildren(List<ChildTestModel> children) {
        this.children = children;
    }*/
    
    
}
