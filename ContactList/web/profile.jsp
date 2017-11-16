<%-- 
    Document   : testjsp
    Created on : Nov 19, 2016, 12:49:51 AM
    Author     : miguel
--%>

<%@page import="softwaresecurity.exceptions.CorruptCapabilityError"%>
<%@page import="softwaresecurity.privacies.Privacies"%>
<%@page import="softwaresecurity.accessControl.AccessControl"%>
<%@page import="softwaresecurity.exceptions.AccessControlError"%>
<%@page import="customRealm.realm.MyPrincipal"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page language="java" import="java.sql.*" import="java.security.Principal"%>

<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="CSS/tabStyle.css">
        <script src="JavaScript/tabScript.js"></script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Profile</title>
    </head>
    <body>
        <%
            String username = request.getParameter("user");
            Privacies p = new Privacies(username);
            AccessControl ac = new AccessControl();

            final Principal authUser = request.getUserPrincipal();

            if (!p.getFullNamePrivacy().equals("public")) {
                try {
                    ac.checkPermission(authUser, ac.getCapabilities(request), username, p.getFullNamePrivacy());
                    String result;
                    String fullname = "select fullname from profiles where username='" + username + "';";
                    result = p.executeQuery(fullname, "fullname");
        %>
        <h1><%=result%></h1<br><br>
        <%
        } catch (AccessControlError | CorruptCapabilityError e) {
        %><h1>(Username): (<%=username%>)</h1<br><br><%
            }
        } else {
            String result;
            String fullname = "select fullname from profiles where username='" + username + "';";
            result = p.executeQuery(fullname, "fullname");
        %>
        <h1><%=result%></h1<br><br>
        <%
            }
            if (!request.getUserPrincipal().getName().equals("root")) {
                String friendsQuery = "select * from friendships where user1='" + request.getUserPrincipal().getName() + "' and user2 = '" + username + "';";
                String status = p.executeQuery(friendsQuery, "status");

                if (status == null) {%>
        <form class="form-horizontal" id="addfriend"
              action='https://localhost:8443/ContactList/addfriend?friend=<%=username%>'
              method="POST">
            <button class="btn btn-success">Add as Friend</button>
        </form>
        <% } else {
            if (status.equals("pending")) { %>

        <p><h4>Friendship pending...</h4></p>

    <%
    } else {
        if (status.equals("friend")) { %>

    <p><h4>You are friends</h4></p>

<%
                }
            }
        }
    }%>
<h3><strong>E-Mail: </strong> <%
    if (!p.getEmailPrivacy().equals("public")) {
        try {
            ac.checkPermission(authUser, ac.getCapabilities(request), username, p.getEmailPrivacy());
            String emailquery = "select email from profiles where username='" + username + "';";
            String result = p.executeQuery(emailquery, "email");
    %>
    <%=result%><br>
    <%
    } catch (AccessControlError | CorruptCapabilityError e) {
    %>You are not authorized to see this information<%
        }
    } else {
        String emailquery = "select email from profiles where username='" + username + "';";
        String result = p.executeQuery(emailquery, "email");
    %>
    <%=result%><br>
    <%
        }
    %> </h3>
<h3><strong>Phone: </strong> <%
    if (!p.getPhonePrivacy().equals("public")) {
        try {
            ac.checkPermission(authUser, ac.getCapabilities(request), username, p.getPhonePrivacy());
            String phonequery = "select phone from profiles where username='" + username + "';";
            String result = p.executeQuery(phonequery, "phone");
    %>
    <%=result%><br><br>
    <%
    } catch (AccessControlError | CorruptCapabilityError e) {
    %>You are not authorized to see this information<%
        }
    } else {
        String phonequery = "select phone from profiles where username='" + username + "';";
        String result = p.executeQuery(phonequery, "phone");
    %>
    <%=result%><br><br>
    <%
        }
    %> 
</h3>
</hr>
<ul class="tab">
    <li><a href="javascript:void(0)" class="tablinks" onclick="openTab(event, 'Bio')"><strong>BIO</strong></a></li>
    <li><a href="javascript:void(0)" class="tablinks" onclick="openTab(event, 'Interests')"><strong>Interests</strong></a></li>
    <li><a href="javascript:void(0)" class="tablinks" onclick="openTab(event, 'Qualifications')"><strong>Qualifications</strong></a></li>
</ul>
<div id="Bio" class="tabcontent">
    <p><%
        if (!p.getBioPrivacy().equals("public")) {
            try {
                ac.checkPermission(authUser, ac.getCapabilities(request), username, p.getBioPrivacy());
                String bioquery = "select bio from profiles where username='" + username + "';";
                String result = p.executeQuery(bioquery, "bio");
        %>
    <pre><%=result%></pre><br><br>
    <%
    } catch (AccessControlError | CorruptCapabilityError e) {
    %><p>You are not authorized to see this information</p><%
        }
    } else {
        String bioquery = "select bio from profiles where username='" + username + "';";
        String result = p.executeQuery(bioquery, "bio");
    %>
    <pre><%=result%></pre><br><br>
    <%
        }
    %></p>
</div>

<div id="Interests" class="tabcontent">
    <p><%
        if (!p.getInterestsPrivacy().equals("public")) {
            try {
                ac.checkPermission(authUser, ac.getCapabilities(request), username, p.getInterestsPrivacy());
                String interestsquery = "select interests from profiles where username='" + username + "';";
                String result = p.executeQuery(interestsquery, "interests");
        %>
    <pre><%=result%></pre><br><br>
    <%
    } catch (AccessControlError | CorruptCapabilityError e) {
    %><p>You are not authorized to see this information</p><%
        }
    } else {
        String interestsquery = "select interests from profiles where username='" + username + "';";
        String result = p.executeQuery(interestsquery, "interests");
    %>
    <pre><%=result%></pre><br><br>
    <%
        }
    %></p>
</div>

<div id="Qualifications" class="tabcontent">
    <p><%
        if (!p.getQualificationsPrivacy().equals("public")) {
            try {
                ac.checkPermission(authUser, ac.getCapabilities(request), username, p.getQualificationsPrivacy());
                String qualificationsquery = "select qualifications from profiles where username='" + username + "';";
                String result = p.executeQuery(qualificationsquery, "qualifications");
        %>
    <pre><%=result%></pre><br><br>
    <%
    } catch (AccessControlError | CorruptCapabilityError e) {
    %><p>You are not authorized to see this information</p><%
        }
    } else {
        String qualificationsquery = "select qualifications from profiles where username='" + username + "';";
        String result = p.executeQuery(qualificationsquery, "qualifications");
    %>
    <pre><%=result%></pre><br><br>
    <%
        }
    %></p>
</div>

<hr/><br>

<form class="form-horizontal" id="back"
      action='https://localhost:8443/ContactList/allusers'
      method="POST">
    <button class="btn btn-success">Go Back</button>
</form>
<br>
<form class="form-horizontal" id="backtoprofile"
      action='https://localhost:8443/ContactList/'
      method="POST">
    <button class="btn btn-success">Back to my profile</button>
</form>
<br>

<form class="form-horizontal" id="logout"
      action='https://localhost:8443/ContactList/logout'
      method="POST">

    <button class="btn btn-success">Logout</button>
</form>
</body>
</html>