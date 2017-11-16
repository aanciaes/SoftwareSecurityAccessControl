package customRealm.realm;

import softwaresecurity.accessControl.AccessControl;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.realm.RealmBase;

import softwaresecurity.exceptions.AccountLockedException;
import softwaresecurity.exceptions.AuthenticationError;
import softwaresecurity.exceptions.UndefinedAccountException;
import java.sql.SQLException;
import softwaresecurity.authenticator.Authenticator;
import softwaresecurity.encryptionAlgorithm.AESencrp;

public class MyRealm extends RealmBase {

    private String username;

    private String password;

    public Principal authenticate(String username, String credentials) {
        this.username = username;
        this.password = credentials;

        //System.out.println("Entering tomcatRealmTest1.authenticate");
        Authenticator authenticator = new Authenticator();
        try {
            String pwd = AESencrp.encrypt(credentials, "");
            //login
            authenticator.login(username, pwd);
            return getPrincipal(username);
        } catch (UndefinedAccountException | AccountLockedException | AuthenticationError | SQLException ex) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    protected Principal getPrincipal(String username) {
        //System.out.println("Entering tomcatRealmTest1.getPrincipal");
        List<String> roles = new ArrayList<String>();
        Authenticator authenticator = new Authenticator();
        try {
            String role = authenticator.getRole(username);
            roles.add(role);
            List<String> caps = setCapabilities(username);
            return new MyPrincipal(username, password, roles, caps);
        } catch (UndefinedAccountException e) {
            return null;
        }
    }

    protected List<String> setCapabilities(String username) {
        AccessControl ac = new AccessControl();
        return ac.setCapabilities(username);
    }

    protected String getPassword(String string) {
        //System.out.println("Entering tomcatRealmTest1.getPassword");
        return password;
    }

    protected String getName() {
        //System.out.println("Entering tomcatRealmTest1.getName");
        return username;
    }
}
