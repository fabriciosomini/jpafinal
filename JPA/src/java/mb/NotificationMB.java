/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mb;

import app.JPA;
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
import java.io.Serializable;
import java.util.HashMap;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import model.MultiMap;
import repository.NotificationRepository;

/**
 *
 * @author fabri
 */
@Named(value = "notificationMB")
@ManagedBean
@SessionScoped
public class NotificationMB extends BaseMB implements Serializable {

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
    
    public void refresh(){
        if(UserMB.getINSTANCE().isAuthorized()){
            getNotifications();
        }
    }

    public List<Notification> getNotifications() {
        verifyAuthorization();
        UserMB userMB = UserMB.getINSTANCE();
        if (userMB != null) {
            User currentUser = userMB.getUser();
            int userId = currentUser.getId();

            MultiMap<String, Object> paramsHirer = new MultiMap<>();
            paramsHirer.put("notificationType", NotificationType.REQUEST_ADDED.getValue());
            paramsHirer.put("$conditionalOperator", "OR");
            paramsHirer.put("notificationType", NotificationType.JOB_STARTED.getValue());
            paramsHirer.put("$conditionalOperator", "OR");
            paramsHirer.put("notificationType", NotificationType.JOB_CANCELED_BY_HIREE.getValue());
            paramsHirer.put("$conditionalOperator", "OR");
            paramsHirer.put("notificationType", NotificationType.JOB_DONE.getValue());
            paramsHirer.put("$conditionalOperator", "AND");
            paramsHirer.put("hirer.id", userId);

            notifications = NotificationRepository.get(paramsHirer);

            MultiMap<String, Object> paramsHiree = new MultiMap<>();
            paramsHiree.put("notificationType", NotificationType.REQUEST_ACCEPTED.getValue());
            paramsHiree.put("$conditionalOperator", "OR");
            paramsHiree.put("notificationType", NotificationType.JOB_APPROVED.getValue());
            paramsHiree.put("$conditionalOperator", "OR");
            paramsHiree.put("notificationType", NotificationType.JOB_REPROVED.getValue());
            paramsHiree.put("$conditionalOperator", "OR");
            paramsHiree.put("notificationType", NotificationType.JOB_CANCELED_BY_HIRER.getValue());
            paramsHirer.put("$conditionalOperator", "AND");
            paramsHiree.put("hiree.id", userId);
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
        
        return getNotifications().size();
    }

    public boolean isEmpty() {
        verifyAuthorization();
        return notifications.isEmpty();
    }
}
