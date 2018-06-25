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
import javax.faces.bean.RequestScoped;
import model.JobStatusType;
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
        jobs = JobRepository.getAll();
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        verifyAuthorization();
        this.jobs = jobs;
    }

    @PostConstruct
    public void init() {

        jobs = JobRepository.getAll();
        job = job == null ? new Job() : job;

    }

    public void addJobRequest(Job job) {
        verifyAuthorization();
        if (!UserMB.getINSTANCE().isAuthorized()) {
            MessageHelper.addMessage("Você precisa entrar para poder inserir um trabalho");
        } else {
            if (!isJobRequestedByMe(job)) {
                User currentUser = UserMB.getINSTANCE().getUser();
                jobs = JobRepository.getAll();
                jobs.stream().forEach(j -> {
                    if (j.getId() == job.getId()) {
                        if (!job.getHirees().contains(currentUser)) {
                            job.addHirees(currentUser);
                        }
                    }
                });
                JobRepository.update(job);
                User hirer = job.getHirer();
                NotificationMB.getINSTANCE().generateNotification(NotificationType.REQUEST_ADDED,
                        hirer, currentUser, job);
                jobs = JobRepository.getAll();
                NavigationHelper.navigate("index.xhtml?faces-redirect=true");

            }

        }

    }

    public boolean isJobRequestedByMe(Job j) {
        User currentUser = UserMB.getINSTANCE().getUser();
        boolean isJobRequestedByMe = false;

        if (j != null) {
            List<User> hirees = j.getHirees();
            isJobRequestedByMe = hirees
                    .stream()
                    .filter(h -> h.getId() == currentUser.getId())
                    .count() > 0;

        }
        return isJobRequestedByMe;
    }

    public boolean isJobMine(Job j) {
        verifyAuthorization();
        User currentUser = UserMB.getINSTANCE().getUser();
        boolean isMine = false;
        if (j != null) {
            User hirer = j.getHirer();
            isMine = j.getId() == 0 ? true : (hirer == null ? false : hirer.getId() == currentUser.getId());
        }
        return isMine;
    }

    public void acceptHiree(User hiree) {
        verifyAuthorization();
        User hirer = UserMB.getINSTANCE().getUser();
        job.setAcceptedHiree(hiree);
        job.setJobStatusType(JobStatusType.NOT_STARTED);
        JobRepository.update(job);
        jobs = JobRepository.getAll();
        NotificationMB.getINSTANCE().generateNotification(NotificationType.REQUEST_ACCEPTED,
                hirer, hiree, job);
    }

    public void edit(Job job) {
        verifyAuthorization();
        if (UserMB.getINSTANCE().isAuthorized()) {
            this.job = job;
            NavigationHelper.navigate("jobdetails.xhtml?faces-redirect=true");
        }
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
            if (isJobMine(job)) {
                User hirer = UserMB.getINSTANCE().getUser();
                job.setHirer(hirer);
                JobRepository.insert(job);
                jobs = JobRepository.getAll();
                job = new Job();
                NavigationHelper.navigate("index.xhtml?faces-redirect=true");
            }
        }

    }

    public void back() {
        job = new Job();
        NavigationHelper.navigate("index.xhtml?faces-redirect=true");
    }

    public int hireesCount(Job j) {
        return j.getHirees().size();
    }

    public boolean isJobGrantedToMe(Job j) {
        User acceptedHiree = j.getAcceptedHiree();
        User currentUser = UserMB.getINSTANCE().getUser();
        boolean isJobGrantedToMe = false;
        if (acceptedHiree != null) {
            isJobGrantedToMe = acceptedHiree.getId() == currentUser.getId();
        }
        return isJobGrantedToMe;
    }

    public void setJobAsDone(Job j) {
        j.setJobStatusType(JobStatusType.DONE);
        JobRepository.update(j);
        NotificationMB.getINSTANCE().generateNotification(NotificationType.JOB_DONE,
                j.getHirer(), j.getAcceptedHiree(), job);
    }

    public void setJobAsCanceledByHiree(Job j) {
        j.setJobStatusType(JobStatusType.CANCELED_BY_HIREE);
        JobRepository.update(j);
        NotificationMB.getINSTANCE().generateNotification(NotificationType.JOB_CANCELED_BY_HIREE,
                j.getHirer(), j.getAcceptedHiree(), job);
    }

    public void setJobAsCanceledByHirer(Job j) {
        j.setJobStatusType(JobStatusType.CANCELED_BY_HIRER);
        JobRepository.update(j);
        NotificationMB.getINSTANCE().generateNotification(NotificationType.JOB_CANCELED_BY_HIRER,
                j.getHirer(), j.getAcceptedHiree(), job);
    }

    public void setJobAsStarted(Job j) {
        j.setJobStatusType(JobStatusType.STARTED);
        JobRepository.update(j);
        NotificationMB.getINSTANCE().generateNotification(NotificationType.JOB_STARTED,
                j.getHirer(), j.getAcceptedHiree(), job);
    }

    public boolean isJobPersisted(Job j) {
        return j.getId() != 0;
    }

    public String getJobStatus(Job j) {
        JobStatusType status = j.getJobStatusType();
        if (status == null) {
            status = JobStatusType.NOT_STARTED;
        }
        String jobStatus = "";
        switch (status) {
            case NOT_STARTED:
                jobStatus = "Trabalho não iniciado";
                break;
            case CANCELED_BY_HIREE:
                jobStatus = "Trabalho cancelado pelo(a) contradador(a)";
                break;
            case CANCELED_BY_HIRER:
                jobStatus = "Trabalho cancelado pelo(a) contratado(a)";
                break;
            case DONE:
                jobStatus = "Trabalho concluído";
                break;
            case STARTED:
                jobStatus = "Trabalho em progresso";
                break;
        }

        return jobStatus;
    }

    public String getButtonJobStatus(Job j) {
        JobStatusType status = j.getJobStatusType();
        if (status == null) {
            status = JobStatusType.NOT_STARTED;
        }
        String jobStatus = "";
        switch (status) {
            case NOT_STARTED:
                if (isJobAssigned(j)) {
                    jobStatus = "Não iniciado";
                } else {
                    jobStatus = "Cadastrar-se";
                }
                break;
            case CANCELED_BY_HIREE:
                jobStatus = "Cancelado";
                break;
            case CANCELED_BY_HIRER:
                jobStatus = "Desistido";
                break;
            case DONE:
                jobStatus = "Concluído";
                break;
            case STARTED:
                jobStatus = "Em progresso";
                break;
        }

        return jobStatus;
    }

    public boolean isJobAssigned(Job j) {
        return j.getAcceptedHiree() != null;
    }

    public boolean isJobAssignedTo(User hiree, Job j) {
        return j.getAcceptedHiree() != null
                && j.getAcceptedHiree().getId() == hiree.getId();
    }

}
