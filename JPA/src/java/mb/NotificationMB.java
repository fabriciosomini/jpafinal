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
import javax.faces.bean.SessionScoped;
import entity.Notification;
import model.NotificationType;
import entity.User;
import javax.faces.bean.ManagedBean;
import repository.NotificationRepository;

/**
 *
 * @author fabri
 */
@Named(value = "notificationMB")
@ManagedBean
@SessionScoped
public class NotificationMB {

    private static NotificationMB INSTANCE;
    private List<Notification> notifications;
    private NotificationRepository notificationRepository;

    @PostConstruct
    public void init() {
        INSTANCE = this;

        notificationRepository = new NotificationRepository();

    }

    public static NotificationMB getINSTANCE() {
        return INSTANCE;
    }

    public List<Notification> getNotifications() {
        if (notifications == null) {
            UserMB userMB = UserMB.getINSTANCE();
            if (userMB != null) {
                User currentUser = userMB.getUser();
                String userId = String.valueOf(currentUser.getId());
                notifications = notificationRepository.get("id", userId);
            }
        }
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public void generateNotification(NotificationType notificationType, User hirer, User hiree) {

        Notification notification = NotificationHelper.generate(notificationType, hirer, hiree);
        notificationRepository.insert(notification);

    }

    public String removeNotification() {
        return "";
    }

}
