/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package softwaresecurity.accessControl;

import softwaresecurity.exceptions.AccessControlError;
import softwaresecurity.exceptions.CorruptCapabilityError;
import java.security.Principal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import customRealm.realm.MyPrincipal;
import softwaresecurity.encryptionAlgorithm.AESencrp;

/**
 *
 * @author miguel
 */
public class AccessControl {

    public Capability makeKey(String owner, String Grantee, int Nonce, String Resource, String Operation, long timeout, String key) {
        return new Capability(owner, Grantee, Nonce, Resource, Operation, timeout, key);
    }

    public boolean checkPermission(Principal user, List<String> cap, String resource, String operation)
            throws CorruptCapabilityError, AccessControlError {
        boolean access = false;
        Iterator<String> i = cap.iterator();
        while (i.hasNext()) {
            String[] str = i.next().split("\\.");

            String payload = str[0];
            String signed = str[1];

            Capability c = new Capability(payload);

            if (!checkAuthenticity(payload, signed, c.getOwner())) {
                throw new CorruptCapabilityError();
            }

            if (c.equals(resource, operation)) {
                access = true;
            }
        }
        if (!access) {
            throw new AccessControlError();
        }
        return access;
    }

    public List<String> getCapabilities(HttpServletRequest request) {
        MyPrincipal mp = (MyPrincipal) request.getUserPrincipal();
        return mp.getCapabilities();
    }

    private boolean checkAuthenticity(String payload, String signed, String owner) {
        int payloadHash = payload.hashCode();

        String signed_decoded = new String(Base64.getDecoder().decode(signed));
        try {
            String paylod_unsigned = AESencrp.decrypt(signed_decoded, getKey(owner));
            System.err.println("PayloadUnsigned: " + paylod_unsigned);
            System.err.println("Normal Payload: " + payload);
            int signedhash = paylod_unsigned.hashCode();
            return signedhash == payloadHash;
        } catch (Exception ex) {
            return false;
        }
    }

    public List<String> setCapabilities(String user) {
        List<String> caps = new ArrayList();
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = connectDB();
            stmt = conn.createStatement();
            String sqlStr = "select * from capabilities where user = '" + user + "';";

            ResultSet rset = stmt.executeQuery(sqlStr); // Send the query to the server
            if (!rset.isClosed()) {
                while (rset.next()) {
                    Capability c = makeKey(rset.getString("owner"), rset.getString("grantee"), generateNonce(), rset.getString("resource"), rset.getString("operation"),
                            -1, getKey(rset.getString("owner")));
                    caps.add(c.getCapability());
                    System.err.println("Capability: " + c.getCapability());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                // Step 5: Close the Statement and Connection
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return caps;
    }

    /**
     * Generate 10 random integers in the range 0..99999.
     */
    public int generateNonce() {
        Random randomGenerator = new Random();
        return randomGenerator.nextInt(100000);
    }

    public String getKey(String username) {
        String key = "";
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = connectDB();
            stmt = conn.createStatement();
            String sqlStr = "select key from keys where user = '" + username + "';";

            ResultSet rset = stmt.executeQuery(sqlStr); // Send the query to the server
            if (!rset.isClosed()) {
                key = rset.getString("key");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                // Step 5: Close the Statement and Connection
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return key;
    }

    /**
     * Creates a connection to database
     *
     * @return The connection
     * @throws SQLException
     */
    private Connection connectDB() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
        }
        return DriverManager.getConnection("jdbc:sqlite:/home/miguel/NetBeansProjects/ContactList/contactList.db");  // <<== Check
    }
}
