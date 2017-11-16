/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package softwaresecurity.servlets;

import softwaresecurity.accessControl.AccessControl;
import softwaresecurity.accessControl.Capability;
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
@WebServlet(name = "decidefriendship", urlPatterns = {"/decidefriendship"})
public class DecideFriendship extends HttpServlet {

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
            Connection con = null;
            ResultSet rst = null;
            Statement stmt = null;

            String url = "jdbc:sqlite:/home/miguel/NetBeansProjects/ContactList/contactList.db";
            try {
                con = DriverManager.getConnection(url);

                if (request.getParameter("decision").equals("true")) {
                    AccessControl ac = new AccessControl();

                    Capability c = ac.makeKey(request.getUserPrincipal().getName(), request.getParameter("friend"), 2, request.getUserPrincipal().getName(), "internal", 0, ac.getKey(request.getUserPrincipal().getName()));
                    stmt = con.createStatement();
                    stmt.executeUpdate("update friendships set status = 'friend' where user1 ='" + request.getParameter("friend") + "'  and user2 = '" + request.getUserPrincipal().getName() + "';");

                    stmt.executeUpdate("insert into capabilities (user, owner, grantee, resource, operation) values ("
                            + "'" + c.getGrantee() + "',"
                            + "'" + c.getOwner() + "',"
                            + "'" + c.getGrantee() + "',"
                            + "'" + c.getResource() + "',"
                            + "'" + c.getOperation() + "');");
                    out.println("<html><head><title>Request accepted</title></head><body><p>Request Accepted</p><b>"
                            + "<button class='btn btn-success' "
                            + "onclick=\"location.href = 'https://localhost:8443/ContactList/';\">Go Back</button>"
                            + "</body></html>");
                } else {
                    stmt = con.createStatement();
                    stmt.executeUpdate("delete from friendships where user1 ='" + request.getParameter("friend") + "';");
                    out.println("<html><head><title>Request rejected</title></head><body><p>Request rejected</p><b>"
                            + "<button class='btn btn-success' "
                            + "onclick=\"location.href = 'https://localhost:8443/ContactList/';\">Go Back</button>"
                            + "</body></html>");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
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
