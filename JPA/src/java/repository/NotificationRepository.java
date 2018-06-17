/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import java.util.ArrayList;
import java.util.List;
import model.Notification;
import model.User;

/**
 *
 * @author fabri
 */
public class NotificationRepository {

    public static void insertNotification(Notification notification) {
        //TODO: adicionar no banco
    }

    public static List<Notification> getNotifications(User currentUser) {
        //TODO: pegar do banco
        
        return new ArrayList<>();
    }
    
}
