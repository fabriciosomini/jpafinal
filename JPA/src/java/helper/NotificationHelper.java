/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import entity.Job;
import entity.Notification;
import model.NotificationType;
import entity.User;

/**
 *
 * @author fabri
 */
public class NotificationHelper {

    public static Notification generate(NotificationType noticationType, User hirer, User hiree, Job job) {
        Notification notification = new Notification();
        String description = "";
        
        switch (noticationType) {
            case REQUEST_ACCEPTED:
               description = "Pedido de trabalho aceito: "+ job.getTitle();
                break;
            case REQUEST_ADDED:
                description = "Novo candidato para o trabalho: " + job.getTitle();
                break;
  
        }
        notification.setDescription(description);
        notification.setNotificationType(noticationType);
        notification.setHirer(hirer);
        notification.setHiree(hiree);
        notification.setJob(job);
  
        return notification;
    }
}
