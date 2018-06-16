package test;


import app.JPA;
import java.util.List;
import javax.persistence.*;

public class TestRepository {
    
    public static void salvar(TestModel testModel) {
        EntityManager em = JPA.getEM();
        EntityTransaction t = em.getTransaction();
        t.begin();
        em.merge(testModel);
        t.commit();
    }

    public static void excluir(TestModel testModel) {
        EntityManager em = JPA.getEM();
        EntityTransaction t = em.getTransaction();
        t.begin();
        em.remove(em.find(TestModel.class, testModel.getCodigo()));
        t.commit();
    }

    public static TestModel getTestModel(Integer codigo) {
        EntityManager em = JPA.getEM();
        return em.find(TestModel.class, codigo);
    }
    
    public static List<TestModel> getTestModels() {
        EntityManager em = JPA.getEM();
        return em.createQuery("select p from TestModel p", TestModel.class).getResultList();
    }
    
    public static List<TestModel> getTestModels(String valor) {
        EntityManager em = JPA.getEM();
        TypedQuery<TestModel> query = em.createQuery("select x from TestModel x where x.nome like :valor", TestModel.class);
        query.setParameter("valor", "%" + valor + "%");
        return query.getResultList();
    }
}
