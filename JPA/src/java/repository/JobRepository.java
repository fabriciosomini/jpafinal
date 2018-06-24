/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import app.JPA;
import entity.Job;
import entity.Job;
import helper.IdHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

/**
 *
 * @author fabri
 */
public class JobRepository {

    private JobRepository() {

    }

    public static void insert(Job object) {

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

    public static void delete(Job object) {
        EntityManager em = JPA.getEM();
        EntityTransaction t = em.getTransaction();
        t.begin();
        em.remove(em.find(Job.class, object.getId()));
        t.commit();
    }

    public static List<Job> get(String key, Object value) {
        String operator = value instanceof String ? "like" : "=";
        String singleQuotes = value instanceof String ? "%" : "";
        EntityManager em = JPA.getEM();
        String fsingleQuotes = value instanceof String ? "'%" : "";
        String lsingleQuotes = value instanceof String ? "%'" : "";
        String queryString = "select x from " + Job.class.getSimpleName() + " x where x."
                + key + " " + operator + " " + fsingleQuotes + value + lsingleQuotes;

        List<Job> result = null;
        try {
            TypedQuery<Job> query = em.createQuery(queryString, Job.class);
            result = query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            result = result == null ? new ArrayList() : result;
        }

        return result;
    }

    public static List<Job> get(Map<String, Object> params) {

        EntityManager em = JPA.getEM();
        String queryString = "select x from " + Job.class.getSimpleName() + " x where x.";
        int i = 0;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            String operator = value instanceof String ? "like" : "=";
            String fsingleQuotes = value instanceof String ? "'%" : "";
            String lsingleQuotes = value instanceof String ? "%'" : "";
            queryString += key + " " + operator + " " + fsingleQuotes + value + lsingleQuotes;
            i++;
            boolean isLast = params.entrySet().size() == i;
            if (!isLast) {
                queryString += " AND x.";
            }

        }
        List<Job> result = null;
        try {
            TypedQuery<Job> query = em.createQuery(queryString, Job.class);
            result = query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            result = result == null ? new ArrayList() : result;
        }

        return result;

    }

    public static List<Job> getAll() {
        EntityManager em = JPA.getEM();
        String queryString = "select x from " + Job.class.getSimpleName() + " x";
        TypedQuery<Job> result = em.createQuery(queryString, Job.class);
        return result.getResultList();
    }
}
