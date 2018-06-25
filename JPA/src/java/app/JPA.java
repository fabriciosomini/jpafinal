package app;

import javax.persistence.*;

public class JPA {
    
    public static EntityManagerFactory emf;
    
    public static EntityManager getEM() {
        if(emf == null) {
            emf = Persistence.createEntityManagerFactory(
                    "JPAPU");
        }
        return emf.createEntityManager();
    }
    
    public static void close(){
        getEM().close();
        emf = null;
    }
    
}
