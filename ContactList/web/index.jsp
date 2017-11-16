<%-- 
    Document   : newjsp
    Created on : Nov 16, 2016, 1:06:27 AM
    Author     : miguel
--%>

<%@page import="customRealm.realm.MyPrincipal"%>
<%@page import="org.apache.catalina.realm.GenericPrincipal"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page language="java" import="java.sql.*" import="java.security.Principal"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="CSS/tabStyle.css">
        <script src="JavaScript/tabScript.js"></script>

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
        %>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Profile</title>
    </head>
    <body>
        <%
            final Principal userPrincipal = request.getUserPrincipal();
            String username = userPrincipal.getName();

            stmt = con.createStatement();
            rst = stmt.executeQuery("select (select count() from friendships where user2 = '" + username + "' and status = 'pending') "
                    + "as count, * from friendships;");
            String notifications;
            if (rst.isClosed()) {
                notifications = "0";
            } else {
                notifications = rst.getString("count");
            }
            rst = stmt.executeQuery("select * from profiles where username='" + username + "';");

            if (!rst.isClosed()) {
        %>
        <h1><%=rst.getString("fullname")%></h1<br><br>

        <form class="form-horizontal" id="edit"
              action='https://localhost:8443/ContactList/editprofile.jsp'
              method="POST">
            <button class="btn btn-success">Edit Profile</button>
        </form>

        <form class="form-horizontal" id="privacy"
              action='https://localhost:8443/ContactList/editprivacy.jsp'
              method="POST">
            <button class="btn btn-success">Change privacy settings</button>
        </form>

        <%
            MyPrincipal myprincipal = (MyPrincipal) userPrincipal;
            String role = myprincipal.getRole();
            if (role.equals("root")) { %>
        <form class="form-horizontal" id="lock"
              action='https://localhost:8443/ContactList/lockunlock.html'
              method="POST">
            <button class="btn btn-success">Lock/Unlock User</button>
        </form>
        <%

        } else {
        %>
        <p><h3>Friend Requests (<a href="https://localhost:8443/ContactList/decidefriendship.jsp"><%=notifications%>)</a></h3></p>
        <% }
        %>

    <h3><strong>E-Mail: </strong> <%=rst.getString("email")%> </h3> 
    <h3><strong>Phone: </strong><%=rst.getString("phone")%></h3> 
    <hr/>
    <ul class="tab">
        <li><a href="javascript:void(0)" class="tablinks" onclick="openTab(event, 'Bio')"><strong>BIO</strong></a></li>
        <li><a href="javascript:void(0)" class="tablinks" onclick="openTab(event, 'Interests')"><strong>Interests</strong></a></li>
        <li><a href="javascript:void(0)" class="tablinks" onclick="openTab(event, 'Qualifications')"><strong>Qualifications</strong></a></li>
            <% if (!role.equals("root")) { %>
        <li><a href="javascript:void(0)" class="tablinks" onclick="openTab(event, 'Friends')"><strong>Friends</strong></a></li>
            <% }
            %>  
    </ul>
    <div id="Bio" class="tabcontent">
        <% System.err.println(rst.getString("bio"));%>
        <p><pre><%=rst.getString("bio")%></pre></p>
</div>

<div id="Interests" class="tabcontent">
    <p><pre><%=rst.getString("Interests")%></pre></p>
</div>

<div id="Qualifications" class="tabcontent">
    <p><pre><%=rst.getString("Qualifications")%></pre></p> 
</div>
<% if (!role.equals("root")) { %>
<div id="Friends" class="tabcontent">
    <p><%
        rst = stmt.executeQuery("select user2, islocked from friendships join users where user1 = '" + username + "' and status = 'friend' and user2 = username;");
        if (rst.isClosed()) { %>
        No friends yet, go and invite your friends
        <%
        } else {
            while (rst.next()) {
                String user = rst.getString("user2");
                if (!rst.getBoolean("isLocked")) {%>
        <a href="https://localhost:8443/ContactList/profile.jsp?user=<%=user%>"> <%=user%> </a><br>
        <%             }
                    }
                }
            }
        %>
    </p> 
</div>

<%    } else {%>

No information

<%}
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        rst.close();
        stmt.close();
        con.close();
    }
%>
<hr/><br>

<form class="form-horizontal" id="users"
      action='https://localhost:8443/ContactList/allusers'
      method="GET">

    <button class="btn btn-success">Search Users</button>
</form>

<br>
<form class="form-horizontal" id="logout"
      action='https://localhost:8443/ContactList/logout'
      method="POST">

    <button class="btn btn-success">Logout</button>
</form>
</body>
</html>