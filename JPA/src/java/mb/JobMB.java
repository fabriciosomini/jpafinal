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
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import model.JobStatusType;
import model.MultiMap;
import repository.JobRepository;

/**
 *
 * @author fabri
 */
@Named(value = "jobMB")
@ManagedBean
@SessionScoped
public class JobMB extends BaseMB {

    private static JobMB INSTANCE;

    private static JobMB getINSTANCE() {
        return INSTANCE;
    }

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

        UserMB userMB = UserMB.getINSTANCE();
        if (userMB == null || !userMB.isAuthorized()) {
            jobs = JobRepository.getAll();
        } else {
            MultiMap<String, Object> params = new MultiMap<>();
            int currentUserId = UserMB.getINSTANCE().getUser().getId();
            params.put("acceptedHiree.id", currentUserId);
            params.put("$conditionalOperator", "OR");
            params.put("jobStatusType", JobStatusType.UNASSIGNED.getValue());
            params.put("$conditionalOperator", "OR");
            params.put("hirer.id", currentUserId);
            jobs = JobRepository.get(params);
        }
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        verifyAuthorization();
        this.jobs = jobs;
    }

    @PostConstruct
    public void init() {

        jobs = getJobs();
        job = job == null ? new Job() : job;
        INSTANCE = this;
    }

    public void addJobRequest(Job job) {
        verifyAuthorization();
        if (!UserMB.getINSTANCE().isAuthorized()) {
            MessageHelper.addMessage("Você precisa entrar para poder inserir um trabalho");
        } else {
            if (!isJobRequestedByMe(job)) {
                User currentUser = UserMB.getINSTANCE().getUser();
                jobs = getJobs();
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
                jobs = getJobs();
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
        job.setJobStatusType(JobStatusType.NOT_STARTED.getValue());
        JobRepository.update(job);
        jobs = getJobs();
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
        job.setJobStatusType(JobStatusType.UNASSIGNED.getValue());
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
                jobs = getJobs();
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
        j.setJobStatusType(JobStatusType.DONE.getValue());
        JobRepository.update(j);
        NotificationMB.getINSTANCE().generateNotification(NotificationType.JOB_DONE,
                j.getHirer(), j.getAcceptedHiree(), job);
    }

    public void setJobAsCanceledByHiree(Job j) {
        j.setJobStatusType(JobStatusType.CANCELED_BY_HIREE.getValue());
        JobRepository.update(j);
        NotificationMB.getINSTANCE().generateNotification(NotificationType.JOB_CANCELED_BY_HIREE,
                j.getHirer(), j.getAcceptedHiree(), job);
        jobs = getJobs();
    }

    public void setJobAsCanceledByHirer(Job j) {
        j.setJobStatusType(JobStatusType.CANCELED_BY_HIRER.getValue());
        JobRepository.update(j);
        NotificationMB.getINSTANCE().generateNotification(NotificationType.JOB_CANCELED_BY_HIRER,
                j.getHirer(), j.getAcceptedHiree(), job);
        jobs = getJobs();
    }

    public void setJobAsStarted(Job j) {
        j.setJobStatusType(JobStatusType.STARTED.getValue());
        JobRepository.update(j);
        NotificationMB.getINSTANCE().generateNotification(NotificationType.JOB_STARTED,
                j.getHirer(), j.getAcceptedHiree(), job);
        jobs = getJobs();
    }

    public void setJobAsApprovedByHirer(Job j) {
        j.setJobStatusType(JobStatusType.APPROVED.getValue());
        JobRepository.update(j);
        NotificationMB.getINSTANCE().generateNotification(NotificationType.JOB_APPROVED,
                j.getHirer(), j.getAcceptedHiree(), job);
    }

    public void setJobAsReprovedByHirer(Job j) {
        j.setJobStatusType(JobStatusType.REPROVED.getValue());
        JobRepository.update(j);
        NotificationMB.getINSTANCE().generateNotification(NotificationType.JOB_REPROVED,
                j.getHirer(), j.getAcceptedHiree(), job);
        jobs = getJobs();
    }

    public void setJobAsUnassigned(Job j) {
        j.setJobStatusType(JobStatusType.UNASSIGNED.getValue());
        User acceptedHiree = j.getAcceptedHiree();
        User hiree = j.getHirees()
                .stream()
                .filter(h -> h.getId() == acceptedHiree.getId())
                .findFirst()
                .orElse(null);
        if (hiree != null) {
            j.getHirees().remove(hiree);
        }
        j.setAcceptedHiree(null);
        JobRepository.update(j);
        jobs = getJobs();
    }

    public boolean isJobPersisted(Job j) {
        return j.getId() != 0;
    }

    public String getJobStatus(Job j) {
        JobStatusType status = JobStatusType.getValue(j.getJobStatusType());
        if (status == null) {
            status = JobStatusType.UNASSIGNED;
        }
        String jobStatus = "";
        switch (status) {
            case UNASSIGNED:
                jobStatus = "Trabalho não atribuído";
                break;
            case NOT_STARTED:
                jobStatus = "Trabalho não iniciado";
                break;
            case CANCELED_BY_HIRER:
                jobStatus = "Trabalho cancelado pelo(a) contradador(a)";
                break;
            case CANCELED_BY_HIREE:
                jobStatus = "Trabalho cancelado pelo(a) contratado(a)";
                break;
            case DONE:
                jobStatus = "Trabalho concluído";
                break;
            case APPROVED:
                jobStatus = "Trabalho Aprovado";
                break;
            case REPROVED:
                jobStatus = "Trabalho Reprovado";
                break;
            case STARTED:
                jobStatus = "Trabalho em progresso";
                break;
        }

        return jobStatus;
    }

    public String getButtonJobStatus(Job j) {
        JobStatusType status = JobStatusType.getValue(j.getJobStatusType());
        if (status == null) {
            status = JobStatusType.UNASSIGNED;
        }
        String jobStatus = "";

        JobMB jobMB = JobMB.getINSTANCE();
        boolean isJobRequestedByMe = jobMB.isJobRequestedByMe(j);
        boolean isJobAssigned = jobMB.isJobAssigned(j);
        if (isJobRequestedByMe && !isJobAssigned && status != JobStatusType.CANCELED_BY_HIRER
                && status != JobStatusType.CANCELED_BY_HIREE) {
            jobStatus = "Em análise";
        } else {
            switch (status) {
                case UNASSIGNED:
                    jobStatus = "Candidatar-se";
                    break;
                case NOT_STARTED:
                    jobStatus = "Não iniciado";
                    break;
                case CANCELED_BY_HIREE:
                    jobStatus = "Renunciado";
                    break;
                case CANCELED_BY_HIRER:
                    jobStatus = "Encerrado";
                    break;
                case DONE:
                    jobStatus = "Concluído";
                    break;
                case APPROVED:
                    jobStatus = "Aprovado";
                    break;
                case REPROVED:
                    jobStatus = "Reprovado";
                    break;
                case STARTED:
                    jobStatus = "Em progresso";
                    break;
            }

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

    public boolean isRenderedRequestJobButton(Job j) {
        boolean isRenderRequestJob = false;
        UserMB userMB = UserMB.getINSTANCE();
        JobMB jobMB = JobMB.getINSTANCE();
        if (userMB != null && jobMB != null) {
            boolean isAuthorized = userMB.isAuthorized();
            boolean isJobPersisted = jobMB.isJobPersisted(j);
            boolean isJobMine = jobMB.isJobMine(j);
            boolean isJobAssigned = jobMB.isJobAssigned(j);

            isRenderRequestJob = isAuthorized && isJobPersisted && !isJobMine && !isJobAssigned;
        }
        return isRenderRequestJob;
    }

    public boolean isDisabledRequestJobButton(Job j) {
        boolean isDisabledRequestJobButton = false;
        UserMB userMb = UserMB.getINSTANCE();
        JobMB jobMB = JobMB.getINSTANCE();
        if (userMb != null && jobMB != null) {
            JobStatusType jobStatusType = JobStatusType.getValue(j.getJobStatusType());;
            boolean isAuthorized = userMb.isAuthorized();
            boolean isJobRequestedByMe = jobMB.isJobRequestedByMe(j);
            boolean isJobMine = jobMB.isJobMine(j);
            boolean isJobCanceledByHiree = jobStatusType == JobStatusType.CANCELED_BY_HIREE;
            boolean isJobCanceledByHirer = jobStatusType == JobStatusType.CANCELED_BY_HIRER;
            boolean isJobDone = jobStatusType == JobStatusType.DONE;
            isDisabledRequestJobButton = (isJobRequestedByMe || isJobMine)
                    || (isJobCanceledByHiree || isJobCanceledByHirer || isJobDone);
        }
        return isDisabledRequestJobButton;
    }

    public boolean isRenderedRemoveJobButton(Job j) {

        boolean isRenderedRemoveJobButton = false;
        UserMB userMb = UserMB.getINSTANCE();
        JobMB jobMB = JobMB.getINSTANCE();
        if (userMb != null && jobMB != null) {
            JobStatusType jobStatusType = JobStatusType.getValue(j.getJobStatusType());;
            boolean isAuthorized = userMb.isAuthorized();
            boolean isJobPersisted = jobMB.isJobPersisted(j);
            boolean isJobMine = jobMB.isJobMine(j);
            boolean isJobApproved = jobStatusType == JobStatusType.APPROVED;
            boolean isJobReproved = jobStatusType == JobStatusType.REPROVED;
            boolean isJobCanceledByHiree = jobStatusType == JobStatusType.CANCELED_BY_HIREE;
            boolean isJobCanceledByHirer = jobStatusType == JobStatusType.CANCELED_BY_HIRER;
            boolean isJobDone = jobStatusType == JobStatusType.DONE;

            isRenderedRemoveJobButton = isAuthorized && isJobPersisted && isJobMine
                    && (!isJobCanceledByHiree && !isJobCanceledByHirer
                    && !isJobApproved && !isJobReproved && !isJobDone);
        }
        return isRenderedRemoveJobButton;
    }

    public boolean isRenderedCancelJobButton(Job j) {

        boolean isRenderedCancelJobButton = false;
        UserMB userMb = UserMB.getINSTANCE();
        JobMB jobMB = JobMB.getINSTANCE();
        if (userMb != null && jobMB != null) {
            JobStatusType jobStatusType = JobStatusType.getValue(j.getJobStatusType());;
            boolean isAuthorized = userMb.isAuthorized();
            boolean isJobGrantedToMe = jobMB.isJobGrantedToMe(j);
            boolean isJobMine = jobMB.isJobMine(j);
            boolean isJobApproved = jobStatusType == JobStatusType.APPROVED;
            boolean isJobReproved = jobStatusType == JobStatusType.REPROVED;
            boolean isJobCanceledByHiree = jobStatusType == JobStatusType.CANCELED_BY_HIREE;
            boolean isJobCanceledByHirer = jobStatusType == JobStatusType.CANCELED_BY_HIRER;
            boolean isJobDone = jobStatusType == JobStatusType.DONE;

            isRenderedCancelJobButton = isAuthorized && isJobGrantedToMe
                    && !isJobMine
                    && (!isJobCanceledByHiree && !isJobCanceledByHirer
                    && !isJobApproved
                    && !isJobReproved
                    && !isJobDone);
        }
        return isRenderedCancelJobButton;
    }

    public String getRequestJobButtonText(Job j) {

        String requestJobButtonText = "";
        UserMB userMb = UserMB.getINSTANCE();
        JobMB jobMB = JobMB.getINSTANCE();
        if (userMb != null && jobMB != null) {
            boolean isJobMine = jobMB.isJobMine(j);
            boolean isJobAssigned = jobMB.isJobAssigned(j);
            JobStatusType status = JobStatusType.getValue(j.getJobStatusType());
            if (isJobMine && status != JobStatusType.CANCELED_BY_HIRER
                    && status != JobStatusType.CANCELED_BY_HIREE) {
                if (!isJobAssigned) {
                    requestJobButtonText = "Candidatos: " + jobMB.hireesCount(j);
                }
            }

            if (requestJobButtonText.isEmpty()) {
                requestJobButtonText = jobMB.getButtonJobStatus(j);
            }
        }
        return requestJobButtonText;
    }

    public boolean isRenderedSetJobDoneButton(Job j) {

        boolean isRenderedSetJobDoneButton = false;
        UserMB userMb = UserMB.getINSTANCE();
        JobMB jobMB = JobMB.getINSTANCE();
        if (userMb != null && jobMB != null) {
            JobStatusType jobStatusType = JobStatusType.getValue(j.getJobStatusType());;
            boolean isAuthorized = userMb.isAuthorized();
            boolean isJobGrantedToMe = jobMB.isJobGrantedToMe(j);

            boolean isJobStarted = jobStatusType == JobStatusType.STARTED;

            isRenderedSetJobDoneButton = isAuthorized && isJobGrantedToMe && isJobStarted;
        }
        return isRenderedSetJobDoneButton;
    }

    public boolean isRenderedSetJobStartedButton(Job j) {

        boolean isRenderedSetJobDoneButton = false;
        UserMB userMb = UserMB.getINSTANCE();
        JobMB jobMB = JobMB.getINSTANCE();
        if (userMb != null && jobMB != null) {
            JobStatusType jobStatusType = JobStatusType.getValue(j.getJobStatusType());;
            boolean isAuthorized = userMb.isAuthorized();
            boolean isJobGrantedToMe = jobMB.isJobGrantedToMe(j);

            boolean isJobNotStarted = jobStatusType == JobStatusType.NOT_STARTED;

            isRenderedSetJobDoneButton = isAuthorized && isJobGrantedToMe && isJobNotStarted;
        }
        return isRenderedSetJobDoneButton;
    }

    public boolean isRenderedUpdateJobButton(Job j) {
        boolean isRenderedUpdateJobButton = false;
        UserMB userMb = UserMB.getINSTANCE();
        JobMB jobMB = JobMB.getINSTANCE();
        if (userMb != null && jobMB != null) {
            JobStatusType jobStatusType = JobStatusType.getValue(j.getJobStatusType());;
            boolean isAuthorized = userMb.isAuthorized();
            boolean isJobMine = jobMB.isJobMine(j);
            boolean isJobAssigned = jobMB.isJobAssigned(j);

            isRenderedUpdateJobButton = isAuthorized && isJobMine && !isJobAssigned
                    && (jobStatusType != JobStatusType.APPROVED
                    && jobStatusType != JobStatusType.REPROVED
                    && jobStatusType != JobStatusType.CANCELED_BY_HIRER
                    && jobStatusType != JobStatusType.CANCELED_BY_HIREE);
        }
        return isRenderedUpdateJobButton;

    }

    public boolean isReadOnlyJobDetails(Job j) {
        boolean isReadOnlyJobDetails = false;
        UserMB userMb = UserMB.getINSTANCE();
        JobMB jobMB = JobMB.getINSTANCE();
        if (userMb != null && jobMB != null) {
            JobStatusType jobStatusType = JobStatusType.getValue(j.getJobStatusType());;

            boolean isAuthorized = userMb.isAuthorized();
            boolean isJobAssigned = jobMB.isJobAssigned(j);
            boolean isJobMine = jobMB.isJobMine(j);
            isReadOnlyJobDetails = (isAuthorized && isJobAssigned)
                    || !isJobMine
                    || (jobStatusType == JobStatusType.CANCELED_BY_HIRER
                    || jobStatusType == JobStatusType.CANCELED_BY_HIREE);
        }
        return isReadOnlyJobDetails;
    }

    public boolean isRenderedSetHireeButton(Job j) {
        boolean isRenderedSetHireeButton = false;
        UserMB userMb = UserMB.getINSTANCE();
        JobMB jobMB = JobMB.getINSTANCE();
        if (userMb != null && jobMB != null) {
            JobStatusType jobStatusType = JobStatusType.getValue(j.getJobStatusType());;

            boolean isAuthorized = userMb.isAuthorized();
            boolean isJobMine = jobMB.isJobMine(j);
            isRenderedSetHireeButton = isAuthorized
                    && isJobMine
                    && (jobStatusType != JobStatusType.CANCELED_BY_HIRER
                    && jobStatusType != JobStatusType.CANCELED_BY_HIREE);
        }
        return isRenderedSetHireeButton;
    }

    public boolean isRenderedApproveJobButton(Job j) {
        boolean isRenderedApproveJobButton = false;
        UserMB userMb = UserMB.getINSTANCE();
        JobMB jobMB = JobMB.getINSTANCE();
        if (userMb != null && jobMB != null) {
            JobStatusType jobStatusType = JobStatusType.getValue(j.getJobStatusType());;

            boolean isAuthorized = userMb.isAuthorized();
            boolean isJobMine = jobMB.isJobMine(j);
            isRenderedApproveJobButton = isAuthorized
                    && isJobMine
                    && jobStatusType == JobStatusType.DONE;
        }
        return isRenderedApproveJobButton;
    }

    public boolean isRenderedReproveJobButton(Job j) {
        boolean isRenderedApproveJobButton = false;
        UserMB userMb = UserMB.getINSTANCE();
        JobMB jobMB = JobMB.getINSTANCE();
        if (userMb != null && jobMB != null) {
            JobStatusType jobStatusType = JobStatusType.getValue(j.getJobStatusType());;

            boolean isAuthorized = userMb.isAuthorized();
            boolean isJobMine = jobMB.isJobMine(j);
            isRenderedApproveJobButton = isAuthorized
                    && isJobMine
                    && jobStatusType == JobStatusType.DONE;
        }
        return isRenderedApproveJobButton;
    }

    public boolean isRenderedSetJobUnassignedJobButton(Job j) {
        boolean isRenderedSetJobUnassignedJobButton = false;
        UserMB userMb = UserMB.getINSTANCE();
        JobMB jobMB = JobMB.getINSTANCE();
        if (userMb != null && jobMB != null) {
            JobStatusType jobStatusType = JobStatusType.getValue(j.getJobStatusType());

            boolean isAuthorized = userMb.isAuthorized();
            boolean isJobMine = jobMB.isJobMine(j);
            isRenderedSetJobUnassignedJobButton = isAuthorized
                    && isJobMine
                    && jobStatusType == JobStatusType.CANCELED_BY_HIREE;
        }
        return isRenderedSetJobUnassignedJobButton;
    }

}
