/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import app.JPA;
import entity.Authentication;
import entity.Authentication;
import helper.IdHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import model.MultiMap;

/**
 *
 * @author fabri
 */
public class AuthenticationRepository {

    private AuthenticationRepository() {

    }

    public static void insert(Authentication object) {

        EntityManager em = JPA.getEM();
        EntityTransaction t = em.getTransaction();
        try {
            t.begin();
            em.merge(object);
            t.commit();
        } catch (Exception e) {
            t.rollback();
            throw new RuntimeException(e.getMessage());
        }
        object = null;
    }

    public static void delete(Authentication object) {
        EntityManager em = JPA.getEM();
        EntityTransaction t = em.getTransaction();
        t.begin();
        em.remove(em.find(Authentication.class, object.getId()));
        t.commit();
    }

    public static List<Authentication> get(String key, Object value) {
        String operator = value instanceof String ? "like" : "=";
        String singleQuotes = value instanceof String ? "%" : "";
        EntityManager em = JPA.getEM();
        String fsingleQuotes = value instanceof String ? "'%" : "";
        String lsingleQuotes = value instanceof String ? "%'" : "";
        String queryString = "select x from " + Authentication.class.getSimpleName() + " x where x."
                + key + " " + operator + " " + fsingleQuotes + value + lsingleQuotes;

        List<Authentication> result = null;
        try {
            TypedQuery<Authentication> query = em.createQuery(queryString, Authentication.class);
            result = query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            result = result == null ? new ArrayList() : result;
        }

        return result;
    }

    public static List<Authentication> get(MultiMap<String, Object> params) {

        EntityManager em = JPA.getEM();
        String queryString = "select x from " + Authentication.class.getSimpleName() + " x where (";
        int i = 0;
        for (MultiMap.MultiEntry entry : params.entrySet()) {
            String conditionalOperator = "AND";
            String key = (String) entry.getKey();
            Object value = entry.getValue();
            if (!key.equals("$conditionalOperator")) {
                {
                    String operator = value instanceof String ? "like" : "=";
                    String fsingleQuotes = value instanceof String ? "'%" : "";
                    String lsingleQuotes = value instanceof String ? "%'" : "";
                    queryString += "x." + key + " " + operator + " " + fsingleQuotes + value + lsingleQuotes;
                    
                }

            }
            
            i++;
            
            boolean isLast = params.entrySet().size() == i;
            if (!isLast && !key.equals("$conditionalOperator")) {
                MultiMap.MultiEntry nextEntry = (MultiMap.MultiEntry) params.entrySet().toArray()[i];
                String nextKey = (String) nextEntry.getKey();
                Object nextValue = nextEntry.getValue();
                if (nextKey.equals("$conditionalOperator")) {
                    conditionalOperator = String.valueOf(nextValue);
                }
                queryString += " " + conditionalOperator + " ";
            }
        }
        queryString += ")";
        String[] queryParts = queryString.split("AND");
        queryString = String.join(") AND (", queryParts);
        
        List<Authentication> result = null;
        try {
            TypedQuery<Authentication> query = em.createQuery(queryString, Authentication.class);
            result = query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            result = result == null ? new ArrayList() : result;
        }

        return result;

    }

    public static List<Authentication> getAll() {
        EntityManager em = JPA.getEM();
        String queryString = "select x from " + Authentication.class.getSimpleName() + " x";
        TypedQuery<Authentication> result = em.createQuery(queryString, Authentication.class);
        return result.getResultList();
    }
}
