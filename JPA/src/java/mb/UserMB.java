/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mb;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;
import entity.Authentication;
import entity.User;
import javax.faces.bean.ManagedBean;
import repository.AuthenticationRepository;
import repository.UserRepository;

/**
 *
 * @author fabri
 */
@Named(value = "userMB")
@ManagedBean
@SessionScoped
public class UserMB {

    private static UserMB INSTANCE;
    private User user;
    private Authentication authentication;
    private String sessionId = "";
    private AuthenticationRepository authenticationRepository;
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        INSTANCE = this;
        user = new User();
        authentication = new Authentication();
        authentication.setSessionId(sessionId);
        authenticationRepository = new AuthenticationRepository();
        userRepository = new UserRepository();
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public static UserMB getINSTANCE() {
        return INSTANCE;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isAuthorized() {
        String JSESSIONID = authentication.getSessionId();
        if (JSESSIONID != null) {
            if (!JSESSIONID.isEmpty()) {
                List<Authentication> authentications = authenticationRepository
                        .get("sessionId", JSESSIONID);
                if (authentications.size() > 0) {
                    Authentication a = authentications.get(0);
                    if (LocalDate.now().compareTo(a.getLimitDate()) < 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public User getUser() {
        return user;
    }

    public void login() {
        String email = String.valueOf(user.getEmail());
        String password = String.valueOf(user.getPassword());
        
        if (email != null && password != null) {
            if (!email.isEmpty()) {
                HashMap<String, Object> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                
                List<User> users = userRepository.get(params);
                if (users != null) {
                    if (users.size() > 0) {
                        user = users.get(0);
                        LocalDate sessionLimitDate = LocalDate.now().plus(Duration.of(1, ChronoUnit.MINUTES));
                        authentication.setLimitDate(sessionLimitDate);
                        authenticationRepository.insert(authentication);
                    }
                }
            }
        }
    }

    public boolean signUp() {

        //TODO: Retornar do banco
        return true;
    }

}
