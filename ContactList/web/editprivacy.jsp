<%-- 
    Document   : editprivacy
    Created on : Nov 18, 2016, 11:50:18 PM
    Author     : miguel
--%>

<%@page import="softwaresecurity.privacies.Privacies"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Change privacy settings</title>
    </head>
    <body>
        <h1>Privacy Settings</h1> 
        <%
            Privacies cdb = new Privacies(request.getUserPrincipal().getName());

            String nameprivacy = cdb.getPhonePrivacy();
            String emailprivacy = cdb.getEmailPrivacy();
            String phoneprivacy = cdb.getPhonePrivacy();
            String bioprivacy = cdb.getBioPrivacy();
            String interestsprivacy = cdb.getInterestsPrivacy();
            String qualiprivacy = cdb.getQualificationsPrivacy();

        %>

        <form action = "https://localhost:8443/ContactList/editprivacy">
            <strong>Full Name:</strong>
            <input type="radio" name="fullname" value="public" <%if (nameprivacy.equals("public")) { %> checked <%}%> required> Public
            <input type="radio" name="fullname" value="internal" <%if (nameprivacy.equals("internal")) { %> checked <%}%>> Internal 
            <input type="radio" name="fullname" value="private" <%if (nameprivacy.equals("private")) { %> checked <%}%>> Private
            <br><br>
            <strong>E-mail:</strong>
            <input type="radio" name="email" value="public" required <%if (emailprivacy.equals("public")) { %> checked <%}%>> Public
            <input type="radio" name="email" value="internal" <%if (emailprivacy.equals("internal")) { %> checked <%}%>> Internal 
            <input type="radio" name="email" value="private" <%if (emailprivacy.equals("private")) { %> checked <%}%>> Private
            <br><br>
            <strong>Phone:</strong>
            <input type="radio" name="phone" value="public" required <%if (phoneprivacy.equals("public")) { %> checked <%}%>> Public
            <input type="radio" name="phone" value="internal" <%if (phoneprivacy.equals("internal")) { %> checked <%}%>> Internal 
            <input type="radio" name="phone" value="private" <%if (phoneprivacy.equals("private")) { %> checked <%}%>> Private
            <br><br>
            <strong>Biography:</strong>
            <input type="radio" name="bio" value="public" required <%if (bioprivacy.equals("public")) { %> checked <%}%>> Public
            <input type="radio" name="bio" value="internal" <%if (bioprivacy.equals("internal")) { %> checked <%}%>> Internal 
            <input type="radio" name="bio" value="private" <%if (bioprivacy.equals("private")) { %> checked <%}%>> Private
            <br><br>
            <strong>Interests</strong>
            <input type="radio" name="interests" value="public" required <%if (interestsprivacy.equals("public")) { %> checked <%}%>> Public
            <input type="radio" name="interests" value="internal" <%if (interestsprivacy.equals("internal")) { %> checked <%}%>> Internal 
            <input type="radio" name="interests" value="private" <%if (interestsprivacy.equals("private")) { %> checked <%}%>> Private
            <br><br>
            <strong>Qualifications</strong>
            <input type="radio" name="qualifications" value="public" required <%if (qualiprivacy.equals("public")) { %> checked <%}%>> Public
            <input type="radio" name="qualifications" value="internal" <%if (qualiprivacy.equals("internal")) { %> checked <%}%>> Internal 
            <input type="radio" name="qualifications" value="private" <%if (qualiprivacy.equals("private")) { %> checked <%}%>> Private

            <br><hr/>
            <h5>(public - Every one can see this information)</h5>
            <h5>(internal - Only your friends can see this information)</h5>
            <h5>(private - Only you can see this information)</h5>

            <hr/>
            <button class="btn btn-success">Submit</button>
        </form>
        <br>
        <form class="form-horizontal" id="cancel_editing_p"
              action='https://localhost:8443/ContactList/'
              method="POST">
            <button class="btn btn-success">Cancel</button>
        </form>
    </body>
</html>
