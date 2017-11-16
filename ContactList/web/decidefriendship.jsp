<%-- 
    Document   : decidefriendship
    Created on : Nov 18, 2016, 3:06:10 AM
    Author     : miguel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page language="java" import="java.sql.*" import="java.security.Principal"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Accept Friendships?</title>
    </head>
    <body>
        <%
            Connection con = null;
            ResultSet rst = null;
            Statement stmt = null;

            String url = "jdbc:sqlite:/home/miguel/NetBeansProjects/ContactList/contactList.db";
            con = DriverManager.getConnection(url);

            final Principal userPrincipal = request.getUserPrincipal();
            String username = userPrincipal.getName();

            stmt = con.createStatement();
            rst = stmt.executeQuery("select user1 from friendships where user2 = '" + username + "' and status = 'pending';");

            if (rst.isClosed()) {%>
        <p>There is no friendships requests</p> 
        <form class="form-horizontal" id="users"
              action='https://localhost:8443/ContactList/'
              method="GET">

            <button class="btn btn-success">Go Back</button>
        </form>
        <%
        } else {
            while (rst.next()) {%>

        You have a friend request from <strong><%=rst.getString("user1")%></strong>
        <form class="form-horizontal" id="accept"
              action='https://localhost:8443/ContactList/decidefriendship?friend=<%=rst.getString("user1")%>&decision=true'
              method="POST">
            <button class="btn btn-success">Accept</button>
        </form>
        <br>
        <form class="form-horizontal" id="reject"
              action='https://localhost:8443/ContactList/decidefriendship?friend=<%=rst.getString("user1")%>&decision=false'
              method="POST">
            <button class="btn btn-success">Reject</button>
        </form>
        <br>
        <%
                }
            }
        %>
    </body>
</html>
