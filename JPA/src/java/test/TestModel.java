/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author fabri
 */

@Entity
public class TestModel {
    
    @Id
    private int codigo; 
    private String nome;
    
    @OneToMany(fetch = FetchType.LAZY, 
            mappedBy = "TestModel", //nome exato da classe 
            orphanRemoval = true, 
            cascade = CascadeType.ALL)
    private List<ChildTestModel> children;

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

    public List<ChildTestModel> getChildren() {
        return children;
    }

    public void setChildren(List<ChildTestModel> children) {
        this.children = children;
    }
   
    
}
