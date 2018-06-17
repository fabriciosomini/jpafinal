package repository;


import app.JPA;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import model.Authentication;
import model.User;
import test.TestModel;

public class UserRepository extends BaseRepository<User> {

    public  UserRepository () {
        init(User.class);     
    } 
  
}
