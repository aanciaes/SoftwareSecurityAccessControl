/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package softwaresecurity.privacies;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author miguel
 */
public class Privacies {

    private static final int FULLNAME = 1;
    private static final int EMAIL = 2;
    private static final int PHONE = 3;
    private static final int BIO = 4;
    private static final int INTERESTS = 5;
    private static final int QUALIFICATIONS = 6;

    private static final String conectionURL = "jdbc:sqlite:/home/miguel/NetBeansProjects/ContactList/contactList.db";
    private String[] privacy;

    public Privacies(String username) {
        //Database Connector
        String DRIVER = "org.sqlite.JDBC";
        try {
            Class.forName(DRIVER).newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Connection con = null;
        Statement statement = null;
        ResultSet rst = null;
        privacy = new String[7];

        try {
            con = DriverManager.getConnection(conectionURL);
            statement = con.createStatement();
            rst = statement.executeQuery("select * from privacy where user='" + username + "';");

            for (int index = 0, columnIndex = 1; index < 7; index++, columnIndex++) {;
                privacy[index] = rst.getString(columnIndex);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
                rst.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public String executeQuery(String query, String column) {
        Connection con = null;
        Statement stmt = null;
        try {
            con = DriverManager.getConnection(conectionURL);
            stmt = con.createStatement();
            System.err.println(query);
            return stmt.executeQuery(query).getString(column);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    public String getFullNamePrivacy() {
        return privacy[FULLNAME];
    }

    public String getEmailPrivacy() {
        return privacy[EMAIL];
    }

    public String getPhonePrivacy() {
        return privacy[PHONE];
    }

    public String getBioPrivacy() {
        return privacy[BIO];
    }

    public String getInterestsPrivacy() {
        return privacy[INTERESTS];
    }

    public String getQualificationsPrivacy() {
        return privacy[QUALIFICATIONS];
    }
}
