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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author miguel
 */
@WebServlet(name = "editprivacy", urlPatterns = {"/editprivacy"})
public class EditPrivacy extends HttpServlet {

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
                System.err.println("err");
            }

            Connection conn = null;
            PreparedStatement stmt = null;
            try {
                out.println("<html><head><title>Profile Edited</title></head><body>");
                conn = DriverManager.getConnection("jdbc:sqlite:/home/miguel/NetBeansProjects/ContactList/contactList.db");

                String sqlStr = "update privacy set fullname = ?, email = ?, phone = ?, bio= ?, interests = ?, qualifications = ? where user = ?;";

                stmt = conn.prepareStatement(sqlStr);
                stmt.setString(1, request.getParameter("fullname"));
                stmt.setString(2, request.getParameter("email"));
                stmt.setString(3, request.getParameter("phone"));
                stmt.setString(4, request.getParameter("bio"));
                stmt.setString(5, request.getParameter("interests"));
                stmt.setString(6, request.getParameter("qualifications"));
                stmt.setString(7, request.getUserPrincipal().getName());

                stmt.executeUpdate(); // Send the query to the server

                out.println("<p>Privacy Settings were updated succefully</p>");
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
