/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package softwaresecurity.servlets;

import softwaresecurity.accessControl.AccessControl;
import softwaresecurity.exceptions.AccessControlError;
import softwaresecurity.exceptions.CorruptCapabilityError;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import customRealm.realm.MyPrincipal;

/**
 *
 * @author miguel
 */
@WebServlet(name = "allusers", urlPatterns = {"/allusers"})
public class SearchUsers extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException ex) {
                System.err.println("Hello");
            }

            AccessControl ac = new AccessControl();

            Connection conn = null;
            Statement stmt = null;
            try {
                out.println("<html><head><title>All Users</title></head><body><p><h1>Search Users</h1></p>");
                conn = DriverManager.getConnection("jdbc:sqlite:/home/miguel/NetBeansProjects/ContactList/contactList.db");
                stmt = conn.createStatement();
                MyPrincipal authUser = (MyPrincipal) request.getUserPrincipal();

                String sqlStr = "SELECT username, isLocked FROM users;";

                ResultSet rset = stmt.executeQuery(sqlStr); // Send the query to the server

                if (rset.isClosed()) {
                    out.println("No users in the system");
                } else {
                    while (rset.next()) {
                        String user = rset.getString("username");
                        if (!user.equals("root") && !user.equals(authUser.getName())) {
                            if (!rset.getBoolean("isLocked")) {
                                out.println("<a href=\"https://localhost:8443/ContactList/profile.jsp?user="
                                        + user + "\">" + user + "</a><br>");
                            } else {
                                try {
                                    for (String cap : authUser.getCapabilities()) {
                                        System.err.println(cap);
                                    }
                                    ac.checkPermission(authUser, authUser.getCapabilities(), user, "readlock");
                                    out.println("<a href=\"https://localhost:8443/ContactList/profile.jsp?user="
                                            + user + "\">" + user + " (locked)</a><br>");
                                } catch (AccessControlError | CorruptCapabilityError e) {

                                }
                            }
                        }
                    }
                }
                out.println("<br><button class='btn btn-success' "
                        + "onclick=\"location.href = 'https://localhost:8443/ContactList/';\">Back to my profile</button>"
                        + "</body></html>");
                out.println("</body></html>");

            } catch (SQLException e) {
                e.printStackTrace();
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
    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
