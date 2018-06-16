/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mb;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import model.User;
import repository.UserRepository;

/**
 *
 * @author fabri
 */
@Named(value = "userMB")
@Dependent
public class UserMB {

    private User user;
    private static UserMB userMB;

    public static UserMB getInstance() {
        return userMB;
    }

    @PostConstruct
    public void init() {
        userMB = this;
        user = new User();
    }

    public boolean isAuthorized() {
        return true;
    }

    public User getCurrentUser() {
        return user;
    }
    
    public String login(){
        User dbUser = UserRepository.getUser(user);
        if(dbUser != null){
            return "index.xhtml";
        }else{
            return "signup.xhtml";
        }
    }
    
    public boolean signUp(){
        
        //TODO: Retornar do banco
        return true;
    }

}
