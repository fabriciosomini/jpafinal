/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mb;

import helper.NotificationHelper;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import model.Notification;
import model.NotificationType;
import model.User;
import repository.NotificationRepository;

/**
 *
 * @author fabri
 */
@Named(value = "notificationMB")
@Dependent
public class NotificationMB {

    private static NotificationMB notificationMB;

    public static NotificationMB getInstance() {
        return notificationMB;
    }

    @PostConstruct
    public void init() {
        notificationMB = this;
    }


    void generateNotification(NotificationType notificationType, User hirer, User hiree) {
        
        
        Notification notification =  NotificationHelper.generate(notificationType, hirer, hiree);
        NotificationRepository.insertNotification(notification);
        
    }
    
}
