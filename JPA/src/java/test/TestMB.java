/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.Dependent;

/**
 *
 * @author fabri
 */
@Named(value = "testMB")
@Dependent
public class TestMB {

    private List<TestModel> lista;

    public List<TestModel> getLista() {
        return lista;
    }

    public void setLista(List<TestModel> lista) {
        this.lista = lista;
    }
   
    @PostConstruct
    public void init(){
        lista = new ArrayList();
        TestRepository.getTestModels();
    }
    
}
