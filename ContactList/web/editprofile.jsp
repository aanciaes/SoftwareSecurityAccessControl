<%-- 
    Document   : editprofile
    Created on : Nov 18, 2016, 5:46:47 PM
    Author     : miguel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page language="java" import="java.sql.*" import="java.security.Principal"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Edit Profile</title>
    </head>
    <body>
        <%
            //Database Connector
            String DRIVER = "org.sqlite.JDBC";
            Class.forName(DRIVER).newInstance();

            Connection con = null;
            ResultSet rst = null;
            Statement stmt = null;

            try {
                String url = "jdbc:sqlite:/home/miguel/NetBeansProjects/ContactList/contactList.db";
                con = DriverManager.getConnection(url);

                final Principal userPrincipal = request.getUserPrincipal();
                String username = userPrincipal.getName();

                stmt = con.createStatement();
                rst = stmt.executeQuery("select * from profiles where username='" + username + "';");
        %>

        <h1>Update your profile</h1>
        <form action="https://localhost:8443/ContactList/editprofile">
            Biography: <br><br>
            <textarea id="e_bio" name="bio" rows="10" cols="70"><%=rst.getString("bio")%></textarea><br><hr/>
            Interests: <br><br>
            <textarea id="e_interests" name="interests" rows="10" cols="70"><%=rst.getString("interests")%></textarea><br><hr/>
            Qualifications:<br><br>
            <textarea id="e_qualifications" name="qualifications" rows="10" cols="70"><%=rst.getString("qualifications")%></textarea><br><hr/>
            <br>
            <input type="submit" value="Submit">
        </form>
        <br>
        <form class="form-horizontal" id="cancel_editing"
              action='https://localhost:8443/ContactList/'
              method="POST">
            <button class="btn btn-success">Cancel</button>
        </form>
        <%
                stmt.close();
                rst.close();
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        %>
    </body>
</html>