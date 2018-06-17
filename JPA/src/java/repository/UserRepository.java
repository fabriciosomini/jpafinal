package repository;


import entity.User;

public class UserRepository extends BaseRepository<User> {

    public  UserRepository () {
        init(User.class);     
    } 
  
}
