package repository;

import app.JPA;
import entity.User;
import entity.User;
import entity.User;
import entity.User;
import helper.IdHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import model.MultiMap;

public class UserRepository {

    private UserRepository() {

    }

    public static void insert(User object) {

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

    public static void delete(User object) {
        EntityManager em = JPA.getEM();
        EntityTransaction t = em.getTransaction();
        t.begin();
        em.remove(em.find(User.class, object.getId()));
        t.commit();
    }

    public static List<User> get(String key, Object value) {
        String operator = value instanceof String ? "like" : "=";
        String singleQuotes = value instanceof String ? "%" : "";
        EntityManager em = JPA.getEM();
        String fsingleQuotes = value instanceof String ? "'%" : "";
        String lsingleQuotes = value instanceof String ? "%'" : "";
        String queryString = "select x from " + User.class.getSimpleName() + " x where x."
                + key + " " + operator + " " + fsingleQuotes + value + lsingleQuotes;

        List<User> result = null;
        try {
            TypedQuery<User> query = em.createQuery(queryString, User.class);
            result = query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            result = result == null ? new ArrayList() : result;
        }

        return result;
    }

    public static List<User> get(MultiMap<String, Object> params) {

        EntityManager em = JPA.getEM();
        String queryString = "select x from " + User.class.getSimpleName() + " x where (";
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
        
        List<User> result = null;
        try {
            TypedQuery<User> query = em.createQuery(queryString, User.class);
            result = query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            result = result == null ? new ArrayList() : result;
        }

        return result;

    }

    public static List<User> getAll() {
        EntityManager em = JPA.getEM();
        String queryString = "select x from " + User.class.getSimpleName() + " x";
        TypedQuery<User> result = em.createQuery(queryString, User.class);
        return result.getResultList();
    }
}
