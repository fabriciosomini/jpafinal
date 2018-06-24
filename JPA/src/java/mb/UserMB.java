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
import entity.Job;
import entity.User;
import helper.CookieHelper;
import helper.MessageHelper;
import helper.NavigationHelper;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
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

    private AuthenticationRepository authenticationRepository;
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        INSTANCE = this;
        user = new User();
        authentication = new Authentication();
        authenticationRepository = new AuthenticationRepository();
        userRepository = new UserRepository();
    }

    public static UserMB getINSTANCE() {
        return INSTANCE;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private String getCurrentSessionId() {
        CookieHelper cookieHelper = new CookieHelper();
        Cookie cookie = cookieHelper.getCookie("JSESSIONID");
        String JSESSIONID = "";
        if (cookie != null) {
            JSESSIONID = cookie.getValue();

        }

        JSESSIONID = JSESSIONID == null ? "" : JSESSIONID;

        return JSESSIONID;
    }

    public boolean isAuthorized() {
        String sessionId = getCurrentSessionId();
        if (!sessionId.isEmpty()) {
            List<Authentication> authentications = authenticationRepository
                    .get("sessionId", sessionId);
            if (authentications.size() > 0) {
                Authentication a = authentications.get(0);
                /*if (LocalDate.now().compareTo(a.getLimitDate()) < 0) {
                        return true;
                    }*/
                return true;
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
            if (!email.isEmpty() && !password.isEmpty()) {
                HashMap<String, Object> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);

                List<User> users = userRepository.get(params);
                if (users != null) {
                    if (users.size() > 0) {
                        user = users.get(0);
                        //LocalDate sessionLimitDate = LocalDate.now().plus(Duration.of(1, ChronoUnit.MINUTES));
                        //authentication.setLimitDate(sessionLimitDate);
                        String sessionId = getCurrentSessionId();
                        authentication.setSessionId(sessionId);
                        authenticationRepository.insert(authentication);
                        NavigationHelper.navigate("index.xhtml?faces-redirect=true");
                    }
                }
            }
        }

        MessageHelper.addMessage("Email ou senha incorretos");

    }

    public void signUp() {

        List<User> usersResult = userRepository.get("email", user.getEmail());

        if (usersResult.size() > 0) {
            MessageHelper.addMessage("Usuário já existe, favor informar outro email");
        } else {
            int result = userRepository.insert(user);
            String outcome = "";
            if (result == 0) {
                login();
            } else {
                MessageHelper.addMessage("Houve um erro ao criar seu usuário. "
                        + "Tente novamente");
            }
        }
    }

    public void disconnect() {

        authenticationRepository.delete(authentication);
        user = new User();
        authentication = new Authentication();

    }

    public void newUser() {
        user = new User();
        NavigationHelper.navigate("signup.xhtml?faces-redirect=true");
    }

    public void back() {
        user = new User();
        NavigationHelper.navigate("index.xhtml?faces-redirect=true");
    }
}
