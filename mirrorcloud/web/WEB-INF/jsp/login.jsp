<%-- 
    Document   : login
    Created on : Aug 25, 2016, 4:17:45 PM
    Author     : ahmed
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
        <link href="resources/css/materialize.min.css" rel="stylesheet"/>
        <link href="resources/css/icon.css" rel="stylesheet"/>
        <script src="resources/js/jquery-3.1.0.min.js"></script>
        <script src="resources/js/materialize.min.js"></script>
    </head>
    <body>
        <div class="container">
            <h1>Login</h1>
            <h4 class="red-text">${errors}</h4>
            <div class="row">
                <form:form action="processLogin" method="POST" class="col s12">
                    <div class="input-field col s12">
                        <form:input path="uname" id="uname" type="text"></form:input>
                        <form:label path="uname" for="uname">Username</form:label>
                        <span class="red-text">${unameError}</span>
                    </div>
                    <div class="input-field col s12">
                        <form:password path="pass" id="pass" ></form:password>
                        <form:label path="pass" for="pass">Password</form:label>
                        <span class="red-text">${passError}</span>
                    </div>
                    <button class="btn waves-effect waves-light" type="submit" name="action">Login
                      <i class="material-icons right">send</i>
                    </button>       
                </form:form>
            </div>
        </div>
    </body>
</html>
