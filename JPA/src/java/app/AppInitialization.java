/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 *
 * @author fabri
 */
@WebListener
public class AppInitialization implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
      
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
          JPA.close();
    }

}
