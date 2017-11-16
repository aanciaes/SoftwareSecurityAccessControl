/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customRealm.realm;

import java.util.List;
import org.apache.catalina.realm.GenericPrincipal;

/**
 *
 * @author miguel
 */
public class MyPrincipal extends GenericPrincipal {

    private List<String> capabilities;

    public MyPrincipal(String name, String password, List<String> roles, List<String> caps) {
        super(name, password, roles);
        this.capabilities = caps;
    }

    public String getRole() {
        //Only one role per user
        return this.getRoles()[0];
    }

    public List<String> getCapabilities() {
        return capabilities;
    }
}
