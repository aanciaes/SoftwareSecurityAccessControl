/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package softwaresecurity.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import softwaresecurity.authenticator.Authenticator;
import softwaresecurity.encryptionAlgorithm.AESencrp;
import softwaresecurity.exceptions.UsernameAlreadyExistsException;

/**
 *
 * @author miguel
 */
@WebServlet(name = "createuser", urlPatterns = {"/createuser"})
public class CreateUser extends HttpServlet {

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
        // Set the MIME type for the response message
        response.setContentType("text/html");
        // Get a output writer to write the response message into the network socket
        PrintWriter out = response.getWriter();
        try {

            Authenticator authenticator = new Authenticator();

            String pwd = AESencrp.encrypt(request.getParameter("password"));
            String pwdConfirm = AESencrp.encrypt(request.getParameter("confirm_password"));

            authenticator.create_account(request.getParameter("username"),
                    pwd, pwdConfirm, request.getParameter("fullname"), request.getParameter("email"), request.getParameter("phone"));

            out.println("<html><head><title>User Created</title></head><body><p><h1>User was created successfully</h1></p>"
                    + "<button class='btn btn-success' "
                    + "onclick=\"location.href = 'https://localhost:8443/ContactList/';\">Go Back</button>"
                    + "</body></html>");

        } catch (UsernameAlreadyExistsException e) {
            out.println("<html><head><title>Error on creating user</title></head><body><p><h1>Username Already In Use</h1></p>"
                    + "<button class='btn btn-success' "
                    + "onclick=\"location.href = 'https://localhost:8443/ContactList/';\">Go Back</button>"
                    + "</body></html>");
        } catch (Exception e) {
            out.println("<html><head><title>Error on creating user</title></head><body><p><h1>Somethin went wrong. Please try again</h1></p>"
                    + "<button class='btn btn-success' "
                    + "onclick=\"location.href = 'https://localhost:8443/ContactList/';\">Go Back</button>"
                    + "</body></html>");
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
