package softwaresecurity.authenticator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import softwaresecurity.encryptionAlgorithm.AESencrp;

import softwaresecurity.exceptions.AccountLockedException;
import softwaresecurity.exceptions.AuthenticationError;
import softwaresecurity.exceptions.UndefinedAccountException;
import softwaresecurity.exceptions.UsernameAlreadyExistsException;

public class Authenticator {

    public Authenticator() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {

        }
    }

    public Account login(String name, String pwd)
            throws UndefinedAccountException, AccountLockedException, AuthenticationError, SQLException {
        Connection conn = null;
        Statement stmt = null;
        name = name.toLowerCase();
        try {
            // Step 1: Create a database "Connection" object
            // For SqLite3
            conn = connectDB();

            // Step 2: Create a "Statement" object inside the "Connection"
            stmt = conn.createStatement();

            // Step 3: Execute a SQL SELECT query
            String sqlStr = "SELECT password, isLocked FROM users WHERE username = "
                    + "'" + name + "';";

            ResultSet rset = stmt.executeQuery(sqlStr); // Send the query to the server
            if (rset.isClosed()) {
                throw new UndefinedAccountException();
            } else {
                // Step 4: Process the query result
                String password = rset.getString("password");
                boolean isLocked = rset.getBoolean("isLocked");
                if (isLocked) {
                    throw new AccountLockedException();
                } else {
                    if (pwd.equals(password)) {
                        return new Account(name, pwd);
                    } else {
                        throw new AuthenticationError();
                    }
                }
            }
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
    }

    public String getRole(String username) throws UndefinedAccountException {
        Connection conn = null;
        Statement stmt = null;
        username = username.toLowerCase();
        try {
            // Step 1: Create a database "Connection" object
            // For SqLite3
            conn = connectDB();

            // Step 2: Create a "Statement" object inside the "Connection"
            stmt = conn.createStatement();

            // Step 3: Execute a SQL SELECT query
            String sqlStr = "SELECT role FROM roles WHERE username = "
                    + "'" + username + "';";

            ResultSet rset = stmt.executeQuery(sqlStr); // Send the query to the server
            if (rset.isClosed()) {
                throw new UndefinedAccountException();
            } else {
                // Step 4: Process the query result
                String role = rset.getString("role");

                return role;
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

        return null;
    }

    public void create_account(String name, String pwd1, String pwd2, String fullname, String email, String phone) throws UsernameAlreadyExistsException {
        Connection conn = null;
        Statement stmt = null;
        name = name.toLowerCase();
        try {
            conn = connectDB();
            stmt = conn.createStatement();

            String sqlStr = "SELECT username FROM users WHERE username = "
                    + "'" + name + "';";
            stmt.executeUpdate(sqlStr);
            ResultSet rset = stmt.executeQuery(sqlStr); // Send the query to the server

            if (!rset.isClosed()) {
                throw new UsernameAlreadyExistsException();
            } else if (pwd1.equals(pwd2)) {
                String sqlStr2 = "INSERT INTO users (username, password) values ( '" + name + "','" + pwd1 + "');";
                stmt.executeUpdate(sqlStr2);
                String sqlStr3 = "INSERT INTO profiles (username, fullname, email, phone, bio, interests, qualifications) values('"
                        + name + "', '" + fullname + "', '" + email + "', '" + phone + "', '', '', '');";
                stmt.executeUpdate(sqlStr3);
                String key = AESencrp.generateRandom();
                String sqlStr4 = "INSERT INTO keys (user, key) values ('" + name + "', '"
                        + key + "');";
                stmt.executeUpdate(sqlStr4);

                String sqlStr5 = "insert into capabilities (user, owner, grantee, resource, operation) values ('root', '"
                        + name + "', 'root', '" + name + "', 'internal');";
                stmt.executeUpdate(sqlStr5);
                String sqlStr6 = "insert into capabilities (user, owner, grantee, resource, operation) values ('root', '"
                        + name + "', 'root', '" + name + "', 'private');";
                stmt.executeUpdate(sqlStr6);
                String sqlStr7 = "insert into capabilities (user, owner, grantee, resource, operation) values ('root', '"
                        + name + "', 'root', '" + name + "', 'readlock');";
                stmt.executeUpdate(sqlStr7);
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
    }

    /**
     * Creates a connection to database
     *
     * @return The connection
     * @throws SQLException
     */
    private Connection connectDB() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:/home/miguel/NetBeansProjects/ContactList/contactList.db");  // <<== Check
    }
}
