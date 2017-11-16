package softwaresecurity.authenticator;

public class Account {

    String username;
    String pwd;	//Encrypted Form
    boolean logged_in;
    boolean locked_in;

    public Account(String username, String pwd) {
        this.username = username;
        this.pwd = pwd;
        locked_in = false;
        logged_in = false;
    }

    public void changePWD(String pwd) {
        this.pwd = pwd;
    }

    public void changeLocked(boolean locked_in) {
        this.locked_in = locked_in;
    }

    public void changeLogin(boolean logged_in) {
        this.logged_in = logged_in;
    }

    public String getAccountName() {
        return username;
    }

    public String getPassword() {
        return pwd;
    }

    public boolean isLoggedIn() {
        return logged_in;
    }

    public boolean isLocked() {
        return locked_in;
    }
}
