/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mb;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import model.Job;
import model.NotificationType;
import model.User;

/**
 *
 * @author fabri
 */
@Named(value = "jobMB")
@Dependent
public class JobMB {

    private List<Job> jobs;

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    @PostConstruct
    public void init() {
        jobs = new ArrayList();

        generateMemoryData();

    }

    public void addJobRequest(Job job) {
        User currentUser = UserMB.getInstance().getCurrentUser();
        jobs.stream().forEach(p -> {
            if (p.getId() == job.getId()) {
                p.addHirees(currentUser);
            }
        });
        User hirer = job.getHirer();
        NotificationMB.getInstance().generateNotification(NotificationType.REQUEST_ADDED, hirer, currentUser);
    }

    private void generateNotification() {

    }

    private void generateMemoryData() {

        String email = "fabricio.somini@gmail.com";
        String firstName = "Fabricio";
        String lastName = "Somini";
        String cpf = "877.593.036-61";
        String password = "#$%_FA15";

        User hirer = new User();
        hirer.setId(0);
        hirer.setEmail(email);
        hirer.setFirstName(firstName);
        hirer.setLastName(lastName);
        hirer.setNationalIdentity(cpf);
        hirer.setPassword(password);

        String description = "Create a CRUD app.";
        float amountPerHour = 25.50f;

        Job job = new Job();
        job.setId(0);
        job.setDescription(description);
        job.setAmountPerHour(amountPerHour);
        job.setHirer(hirer);
        jobs.add(job);
    }

}
