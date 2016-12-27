<%-- 
    Document   : register
    Created on : Sep 03, 2016, 1:58:45 AM
    Author     : ahmed
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Register</title>
        <link href="resources/css/materialize.min.css" rel="stylesheet"/>
        <link href="resources/css/icon.css" rel="stylesheet"/>
        <script src="resources/js/jquery-3.1.0.min.js"></script>
        <script src="resources/js/materialize.min.js"></script>
    </head>
    <body>
        <div class="container">
            <h1>Register</h1>
            <h4 class="red-text">${errors}</h4>
            <div class="row">
                <form:form action="processRegister" method="POST" class="col s12">
                    <div class="input-field col s12">
                        <form:input path="fullName" id="fullName" type="text"></form:input>
                        <form:label path="fullName" for="fullName">Full Name</form:label>
                        <span class="red-text">${fullNameError}</span>
                    </div>
                    <div class="input-field col s12">
                        <form:input path="email" id="email" type="email"></form:input>
                        <form:label path="email" for="email">Email</form:label>
                        <span class="red-text">${emailError}</span>
                    </div>
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
                    <div class="input-field col s12">
                        <input type="password" id="cpass" />
                        <label  for="cpass">Confirm Password</label>

                    </div>
                    <button class="btn waves-effect waves-light" type="submit" name="action">Register
                      <i class="material-icons right">send</i>
                    </button>       
                </form:form>
            </div>
            
  
        
        </div>
    </body>
</html>
