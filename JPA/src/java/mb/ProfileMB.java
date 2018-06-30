/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mb;

import entity.Job;
import entity.User;
import helper.NavigationHelper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import model.JobStatusType;
import model.MultiMap;
import repository.JobRepository;

/**
 *
 * @author fabri
 */
@Named(value = "profileMB")
@ManagedBean
@SessionScoped
public class ProfileMB extends BaseMB{

    private User profile;
    private List<Job> jobs;

    public List<Job> getJobs() {
        return jobs;
    }

    public User getProfile() {
        return profile;
    }

    @PostConstruct
    public void init() {

        profile = new User();
    }

    public void loadProfile(User p) {
        verifyAuthorization();
        this.profile = p;
        MultiMap<String, Object> params = new MultiMap<>();
        params.put("acceptedHiree.id", p.getId());
        jobs = JobRepository.get(params);
        NavigationHelper.navigate("profile.xhtml?faces-redirect=true");
    }

    public void back() {

        NavigationHelper.navigate("jobdetails.xhtml?faces-redirect=true");
    }

    public double getIncome() {
        verifyAuthorization();
        double income = jobs == null ? 0 : jobs.stream()
                .filter(k -> k.getJobStatusType() == JobStatusType.APPROVED.getValue())
                .mapToDouble(j -> j.getAmountPerHour()).sum();
        return income;
    }

    public float getSuccessRate() {
        verifyAuthorization();
        float successRate = 0;

        if (jobs != null) {
            long approvedCount = jobs.stream()
                    .filter(k -> k.getJobStatusType() == JobStatusType.APPROVED.getValue())
                    .count();
            long total = jobs.size();
            successRate = ((float)(approvedCount * 100) / total);
        }

        return successRate;
    }

}
