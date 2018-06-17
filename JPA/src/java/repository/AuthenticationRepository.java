/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import entity.Authentication;

/**
 *
 * @author fabri
 */
public class AuthenticationRepository extends BaseRepository<Authentication> {

    public AuthenticationRepository() {
        init(Authentication.class);
    }
}
