/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mb;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;
import entity.Job;
import model.NotificationType;
import entity.User;
import helper.MessageHelper;
import helper.NavigationHelper;
import javax.faces.bean.ManagedBean;
import repository.JobRepository;

/**
 *
 * @author fabri
 */
@Named(value = "jobMB")
@ManagedBean
@SessionScoped
public class JobMB extends BaseMB {

    private List<Job> jobs;
    private Job job;


    public Job getJob() {
        verifyAuthorization();
        return job;
    }

    public void setJob(Job job) {
        verifyAuthorization();
        this.job = job;
    }

    public List<Job> getJobs() {
        verifyAuthorization();
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        verifyAuthorization();
        this.jobs = jobs;
    }

    @PostConstruct
    public void init() {
       
        jobs = JobRepository.getAll();
        job = new Job();
     
    }

    public void addJobRequest(Job job) {
        verifyAuthorization();
        if (!UserMB.getINSTANCE().isAuthorized()) {
            MessageHelper.addMessage("Você precisa entrar para poder inserir um trabalho");
        } else {
            User currentUser = UserMB.getINSTANCE().getUser();

            jobs.stream().forEach(p -> {
                if (p.getId() == job.getId()) {
                    p.addHirees(currentUser);
                }
            });
            User hirer = job.getHirer();
            NotificationMB.getINSTANCE().generateNotification(NotificationType.REQUEST_ADDED, hirer, currentUser);
            NavigationHelper.navigate("index.xhtml?faces-redirect=true");

        }

    }

    public boolean isJobMine() {
        verifyAuthorization();
        User currentUser = UserMB.getINSTANCE().getUser();
        User hirer = job.getHirer();
        boolean isMine = hirer == null ? false : job.getHirer().equals(currentUser);
        return isMine;
    }

    public void acceptHiree(User hiree) {
        verifyAuthorization();
        User hirer = UserMB.getINSTANCE().getUser();
        NotificationMB.getINSTANCE().generateNotification(NotificationType.REQUEST_ACCEPTED, hirer, hiree);
    }

    public void edit(Job job) {
        verifyAuthorization();
        this.job = job;
    }

    public void newJob() {
        verifyAuthorization();
        User hirer = UserMB.getINSTANCE().getUser();
        job = new Job();
        job.setHirer(hirer);
    }

    public void saveJob() {
        verifyAuthorization();
        if (!UserMB.getINSTANCE().isAuthorized()) {
            MessageHelper.addMessage("Você precisa entrar para poder inserir um trabalho");
        } else {
            User hirer = UserMB.getINSTANCE().getUser();
            job.setHirer(hirer);
            JobRepository.insert(job);
            jobs = JobRepository.getAll();
            job = new Job();
            NavigationHelper.navigate("index.xhtml?faces-redirect=true");
        }

    }
    
    public void back(){
        job = new Job();
        NavigationHelper.navigate("index.xhtml?faces-redirect=true");
    }

}
