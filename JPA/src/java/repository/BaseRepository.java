/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import app.JPA;
import entity.BaseModel;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import test.TestModel;

/**
 *
 * @author fabri
 * @param <T>
 */
public class BaseRepository<T extends BaseModel> {

    private List<T> items;
    private String tableName = "";
    private Class<T> type;

    protected void init(Class<T> type) {
        this.type = type;
        tableName = type.getSimpleName();
        items = new ArrayList<>();
    }

    public int insert(T object) {
        /*EntityManager em = JPA.getEM();
        EntityTransaction t = em.getTransaction();
        t.begin();
        em.merge(object);
        t.commit();*/

        int index = items.indexOf(object);
        if (index == -1) {
            items.add(object);
            object = ((T)new BaseModel());
            return 0;
        } else {
            items.set(index, object);
            object = ((T)new BaseModel());
            return 1;
        }
        
        
    }

    public int delete(T object) {
        /*EntityManager em = JPA.getEM();
        EntityTransaction t = em.getTransaction();
        t.begin();
        em.remove(em.find(TestModel.class, object.getId()));
        t.commit();*/

        items.remove(object);
        return 1;
    }

    public List<T> get(String key, String value) {
        /* EntityManager em = JPA.getEM();
        String queryString = "select x from " + tableName + " x where x." + key + " like :" + value;
        TypedQuery<T> query = em.createQuery(queryString, type);
        return query.getResultList();*/

        List<T> queryResult = new ArrayList<>();
        for (T item : items) {
            for (Field field : type.getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    if (field.getName().equals(key)) {
                        Object v = (Object) field.get(item);
                        if (v.equals(value)) {
                            queryResult.add(item);
                            break;
                        }
                    }

                } catch (IllegalArgumentException ex) {

                } catch (IllegalAccessException ex) {

                }
            }
        }

        return queryResult;
    }

    public List<T> get(Map<String, Object> params) {

        /*String queryString = "select x from " + tableName;

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
        return query.getResultList();*/
        int paramsCount = params.size();
        int matchedParams = 0;
        List<T> queryResult = new ArrayList<>();
        T result = null;
        for (T item : items) {

            try {
                for (Map.Entry<String, Object> entry : params.entrySet()) {

                    String key = entry.getKey();
                    String value = (String) entry.getValue();
                    for (Field field : type.getDeclaredFields()) {
                        field.setAccessible(true);
                        if (field.getName().equals(key)) {
                            Object v = (Object) field.get(item);
                            if (v.equals(value)) {
                                matchedParams++; 
                                result = item;
                                break;
                            }
                        }
                    }
                }

            } catch (IllegalArgumentException ex) {

            } catch (IllegalAccessException ex) {

            }

        }
        
        if(matchedParams == paramsCount){
            queryResult.add(result);
        }

        return queryResult;

    }

    public List<T> getAll() {
        /*EntityManager em = JPA.getEM();
        String queryString = "select X from " + tableName + " x";
        TypedQuery<T> result = em.createQuery(queryString, type);
        return result.getResultList();*/

        return items;
    }
}
