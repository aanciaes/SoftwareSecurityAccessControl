/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package softwaresecurity.servlets;

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

/**
 *
 * @author miguel
 */
@WebServlet(name = "unlockuser", urlPatterns = {"/unlockuser"})
public class UnlockUser extends HttpServlet {

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
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Lock User</title>");
            out.println("</head>");
            out.println("<body>");

            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException ex) {
                System.err.println("Hello");
            }

            Connection conn = null;
            Statement stmt = null;
            try {
                conn = DriverManager.getConnection("jdbc:sqlite:/home/miguel/NetBeansProjects/ContactList/contactList.db");
                stmt = conn.createStatement();

                String user = request.getParameter("unlockuser");
                String sqlStr = "SELECT isLocked FROM users where username = '" + user + "';";

                ResultSet rset = stmt.executeQuery(sqlStr); // Send the query to the server

                if (rset.isClosed()) {
                    out.println("No such user in the system");
                } else {
                    if (!rset.getBoolean("isLocked")) {
                        out.println("User is not Locked<br>");

                    } else {
                        stmt = conn.createStatement();
                        sqlStr = "update users set isLocked = 0 where username = '" + user + "';";
                        stmt.executeUpdate(sqlStr); // Send the query to the server
                        out.println("User unlocked<br>");
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

            out.println("</body>");
            out.println("</html>");
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
