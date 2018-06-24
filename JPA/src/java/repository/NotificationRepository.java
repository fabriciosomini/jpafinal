/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import app.JPA;
import entity.Notification;
import entity.Notification;
import helper.IdHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import model.MultiMap;
import model.MultiMap.MultiEntry;

/**
 *
 * @author fabri
 */
public class NotificationRepository {

    private NotificationRepository() {

    }

    public static void insert(Notification object) {

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

    public static void delete(Notification object) {
        EntityManager em = JPA.getEM();
        EntityTransaction t = em.getTransaction();
        t.begin();
        em.remove(em.find(Notification.class, object.getId()));
        t.commit();
    }

    public static List<Notification> get(String key, Object value) {
        String operator = value instanceof String ? "like" : "=";
        String singleQuotes = value instanceof String ? "%" : "";
        EntityManager em = JPA.getEM();
        String fsingleQuotes = value instanceof String ? "'%" : "";
        String lsingleQuotes = value instanceof String ? "%'" : "";
        String queryString = "select x from " + Notification.class.getSimpleName() + " x where x."
                + key + " " + operator + " " + fsingleQuotes + value + lsingleQuotes;

        List<Notification> result = null;
        try {
            TypedQuery<Notification> query = em.createQuery(queryString, Notification.class);
            result = query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            result = result == null ? new ArrayList() : result;
        }

        return result;
    }

    public static List<Notification> get(MultiMap<String, Object> params) {

        EntityManager em = JPA.getEM();
        String queryString = "select x from " + Notification.class.getSimpleName() + " x where (";
        int i = 0;
        for (MultiEntry entry : params.entrySet()) {
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
                MultiEntry nextEntry = (MultiEntry) params.entrySet().toArray()[i];
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
        
        List<Notification> result = null;
        try {
            TypedQuery<Notification> query = em.createQuery(queryString, Notification.class);
            result = query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            result = result == null ? new ArrayList() : result;
        }

        return result;

    }

    public static List<Notification> getAll() {
        EntityManager em = JPA.getEM();
        String queryString = "select x from " + Notification.class.getSimpleName() + " x";
        TypedQuery<Notification> result = em.createQuery(queryString, Notification.class);
        return result.getResultList();
    }
}
