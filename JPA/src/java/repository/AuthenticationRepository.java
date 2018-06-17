/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import app.JPA;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import model.Authentication;
import model.Notification;

/**
 *
 * @author fabri
 */
public class AuthenticationRepository extends BaseRepository<Authentication> {

    public AuthenticationRepository() {
        init(Authentication.class);
    }
}
