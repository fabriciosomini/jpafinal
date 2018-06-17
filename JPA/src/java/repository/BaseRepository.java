/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import app.JPA;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import model.Authentication;
import model.BaseModel;
import model.User;
import test.TestModel;

/**
 *
 * @author fabri
 * @param <T>
 */
public class BaseRepository<T extends BaseModel> {

    private String tableName = "";
    private Class<T> type;

    protected void init(Class<T> type) {
        this.type = type;
        tableName = type.getSimpleName();

    }

    public void insert(T object) {
        EntityManager em = JPA.getEM();
        EntityTransaction t = em.getTransaction();
        t.begin();
        em.merge(object);
        t.commit();
    }

    public void delete(T object) {
        EntityManager em = JPA.getEM();
        EntityTransaction t = em.getTransaction();
        t.begin();
        em.remove(em.find(TestModel.class, object.getId()));
        t.commit();
    }

    public List<T> get(String key, String value) {
        EntityManager em = JPA.getEM();
        String queryString = "select x from " + tableName + " x where x." + key + " like :" + value;
        TypedQuery<T> query = em.createQuery(queryString, type);
        return query.getResultList();
    }

    public List<T> get(Map<String, Object> params) {

        String queryString = "select x from " + tableName;

        int i = 0;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            String operator = value instanceof String ? "like" : "=";
            queryString += " x where x." + key + " " + operator + " :" + value;
            i++;
            boolean isLast = params.entrySet().size() == i;
            if (!isLast) {
                queryString += " AND ";
            }

        }

        EntityManager em = JPA.getEM();
        TypedQuery<T> query = em.createQuery(queryString, type);
        return query.getResultList();
    }

    public List<T> getAll() {
        EntityManager em = JPA.getEM();
        String queryString = "select X from " + tableName + " x";
        TypedQuery<T> result = em.createQuery(queryString, type);
        return result.getResultList();
    }
}
