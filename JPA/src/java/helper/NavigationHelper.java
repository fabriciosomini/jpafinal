/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import javax.faces.context.FacesContext;

/**
 *
 * @author fabri
 */
public class NavigationHelper {
     public static void navigate(String url) {
        FacesContext facesContext = FacesContext.getCurrentInstance();  
        facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, url);
    }
}
