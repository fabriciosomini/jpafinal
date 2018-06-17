/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mb;

import helper.NotificationHelper;
import java.util.ArrayList;
import java.util.List;
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
    private static NotificationMB INSTANCE;
    private List<Notification> notifications;

    @PostConstruct
    public void init() {
        INSTANCE = this;
        User currentUser = UserMB.getInstance().getUser();
        notifications = NotificationRepository.getNotifications(currentUser);
    }
    
    public static NotificationMB getINSTANCE() {
        return INSTANCE;
    }

    public static NotificationMB getInstance() {
        return INSTANCE;
    }
   
    public List<Notification> getNotifications() {
        return notifications;
    }
    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public void generateNotification(NotificationType notificationType, User hirer, User hiree) {

        Notification notification = NotificationHelper.generate(notificationType, hirer, hiree);
        NotificationRepository.insertNotification(notification);

    }
    
    public String removeNotification(){
        return "";
    }

}
