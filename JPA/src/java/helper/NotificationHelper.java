/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import model.Notification;
import model.NotificationType;
import model.User;

/**
 *
 * @author fabri
 */
public class NotificationHelper {

    public static Notification generate(NotificationType noticationType, User hirer, User hiree) {
        Notification notification = new Notification();
        String description = "";
        
        switch (noticationType) {
            case REQUEST_ACCEPTED:
               description = "O contratante aceitou seu pedido de trabalho, o  projeto é seu!";
                break;
            case REQUEST_ADDED:
                description = "Há um novo candidato para o trabalho";
                break;
            case REQUEST_DENIED:
                  description = "Infelizmente o contratante optou por outro profissional";
                break;

        }
        notification.setDescription(description);
        notification.setNotificationType(noticationType);
        notification.setHirer(hirer);
        notification.setHiree(hiree);
  
        return notification;
    }
}
