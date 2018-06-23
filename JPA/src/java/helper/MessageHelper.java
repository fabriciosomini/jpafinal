/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author fabri
 */
public class MessageHelper {

    public static void addMessage(String message){
        FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(message));
    }
}
