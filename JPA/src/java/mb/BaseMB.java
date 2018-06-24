/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mb;

import helper.NavigationHelper;

/**
 *
 * @author fabri
 */
public class BaseMB {

    public void verifyAuthorization() {

        UserMB userMB = UserMB.getINSTANCE();
        if (userMB == null || !userMB.isAuthorized()) {
            String indexUrl = "index.xhtml?faces-redirect=true";
            if (!NavigationHelper.currentUrlEndsWith("index.xhtml")) {
                NavigationHelper.navigate(indexUrl);
            }
        }

    }
}
