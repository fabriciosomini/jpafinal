/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author fabri
 */

@Entity
public class ChildTestModel {
    @Id
    private int childCodigo; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_testmodel")
    private TestModel testModel; 

    public TestModel getTestModel() {
        return testModel;
    }

    public void setTestModel(TestModel testModel) {
        this.testModel = testModel;
    }
    
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
