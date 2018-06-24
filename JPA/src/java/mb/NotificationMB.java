/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mb;

import entity.Job;
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
import java.util.HashMap;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import repository.NotificationRepository;

/**
 *
 * @author fabri
 */
@Named(value = "notificationMB")
@ManagedBean
@SessionScoped
public class NotificationMB extends BaseMB {

    private static NotificationMB INSTANCE;
    private List<Notification> notifications;

    @PostConstruct
    public void init() {

        INSTANCE = this;
        notifications = new ArrayList<>();

    }

    public static NotificationMB getINSTANCE() {
        return INSTANCE;
    }

    public List<Notification> getNotifications() {
        verifyAuthorization();
        UserMB userMB = UserMB.getINSTANCE();
        if (userMB != null) {
            User currentUser = userMB.getUser();
            int userId = currentUser.getId();
            HashMap<String, Object> paramsHirer = new HashMap<String, Object>();
            paramsHirer.put("hirer.id", userId);
            paramsHirer.put("notificationType", NotificationType.REQUEST_ADDED.getValue());

            notifications = NotificationRepository.get(paramsHirer);

            HashMap<String, Object> paramsHiree = new HashMap<String, Object>();
            paramsHiree.put("hiree.id", userId);
            paramsHiree.put("notificationType", NotificationType.REQUEST_ACCEPTED.getValue());
            notifications.addAll(NotificationRepository.get(paramsHiree));
        }

        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        verifyAuthorization();
        this.notifications = notifications;
    }

    public void generateNotification(NotificationType notificationType, User hirer, User hiree, Job job) {
        verifyAuthorization();
        Notification notification = NotificationHelper.generate(notificationType, hirer, hiree, job);
        NotificationRepository.insert(notification);

    }

    public void removeNotification(Notification notification) {
        NotificationRepository.delete(notification);
        notifications = NotificationRepository.getAll();
    }

    public int notificationCount() {
        verifyAuthorization();
        return notifications.size();
    }

    public boolean isEmpty() {
        verifyAuthorization();
        return notifications.isEmpty();
    }
}
