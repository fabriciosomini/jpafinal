package repository;


import app.JPA;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import model.User;
import test.TestModel;

public class UserRepository {
    
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
        TypedQuery<TestModel> result = em.createQuery("select p from TestModel p", TestModel.class);
        return result.getResultList();
    }
    
    public static List<TestModel> getTestModels(String valor) {
        EntityManager em = JPA.getEM();
        TypedQuery<TestModel> query = em.createQuery("select x from TestModel x where x.nome like :valor", TestModel.class);
        query.setParameter("valor", "%" + valor + "%");
        return query.getResultList();
    }

    public static User getUser(User user) {
        //TODO: Buscar do banco
        String email = "fabricio.somini@gmail.com";
        String firstName = "Fabricio";
        String lastName = "Somini";
        String cpf = "877.593.036-61";
        String password = "#$%_FA15";
        
        User dbUser = new User();
        dbUser.setId(0);
        dbUser.setEmail(email);
        dbUser.setFirstName(firstName);
        dbUser.setLastName(lastName);
        dbUser.setNationalIdentity(cpf);
        dbUser.setPassword(password);
        
        return dbUser; 
    }
}
