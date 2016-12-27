<%-- 
    Document   : index
    Created on : Dec 16, 2016, 12:13:02 AM
    Author     : ahmed
--%>

<%@page import="mirror.web.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no"/>
    <title>Home : Mirror Cloud</title>

    <!-- CSS  -->
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link href="resources/css/materialize.css" type="text/css" rel="stylesheet" media="screen,projection"/>
    <link href="resources/css/style.css" type="text/css" rel="stylesheet" media="screen,projection"/>
</head>

<body style="background-image:url(resources/img/1.jpg)">
    <p class="red lighten-1 white-text" style="margin-top:0px; text-align:center;" id="errTxt">${errors}</p>
    
    <div class="container center" style=" font-family:'Microsoft YaHei UI Light' ;  font-weight: lighter ; padding-top: 5%; color: white; margin-top:8%;" >

    <span style="font-size: xx-large ;">Login To MirrorCloud </span>
    <p>Get to your files and photos from anywhere, on any device. Share and work together with anyone in your work and life.</p>
    
</div>
<div class="container center" style="font-family:'Microsoft YaHei UI Light'  ; color: white ;font-size: larger">
                                <% User user = (User)session.getAttribute("user"); 
                                if(user!=null)
                                {
                                    String site = new String("viewOwnData");
                                    response.setStatus(response.SC_MOVED_TEMPORARILY);
                                    response.setHeader("Location", site); 
                                }
                                %>
    <div class="row">
        <div class="col m3 " ></div>
            <div class="col m6  " >

                <div class="row">
                    <form:form action="processLogin" method="POST" class="col s12">
                        <div class="row">
                            <div class="input-field col s12" style="height: 25px;">
                                <form:input path="uname" id="uname" placeholder="Username..." type="text" class="validate short" style=" background-color: white ; color:black; padding-left: 3%; padding-right: 3%"></form:input>
                            </div>

                        </div>

                        <div class="row">
                            <div class="input-field col s12" style="height: 25px;">
                                <form:password path="pass" placeholder="Password..." id="pass" class="validate short" style=" background-color: white ; color:black;padding-left: 3%; padding-right: 3%;"></form:password>
                            </div>
                        </div>
                        <div class="row center">
                            <div class="input-field col s12" style="height: 25px;">

                                <button type="submit" class="btn cyan" style="margin-top: 2%;">login</button>
                                <a href="register" class="btn cyan" style="margin-top: 2%;">Register</a>
                            </div>
                        </div>
                    </form:form>
                </div>
            </div>
        <div class="col m3 " ></div>
    </div>
    <hr><span style="font-size: small" >Made By MirrorCloud Team</span>
    </div>
<script src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
        <script src="resources/js/materialize.js"></script>
        <script src="resources/js/init.js"></script>
<script>
    $(".short").mousedown(function(){

        $(this).css("border","2px solid teal");
    });
    $(".short").mouseenter(function(){

        $(this).css("border","2px solid teal");
    });
    $(".short").mouseleave(function(){
        $(this).css("border","1px solid white");
    });
    $(function(){
        setTimeout(function(){
            $("#errTxt").fadeOut();
        },3000)
        
    });

</script>
</body>
</html>
