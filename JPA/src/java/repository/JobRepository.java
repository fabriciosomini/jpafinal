/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import model.Job;
import model.Notification;

/**
 *
 * @author fabri
 */
public class JobRepository extends BaseRepository<Job>{
     public  JobRepository () {
        init(Job.class);     
    } 
}
