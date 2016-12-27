<%-- 
    Document   : Register
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
    <title>Register a New Account on Mirror Cloud</title>

    <!-- CSS  -->
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link href="resources/css/materialize.css" type="text/css" rel="stylesheet" media="screen,projection"/>
    <link href="resources/css/style.css" type="text/css" rel="stylesheet" media="screen,projection"/>
</head>
<body style="background-image:url(resources/img/1.jpg)">
    <p class="red lighten-1 white-text" style="margin-top:0px; text-align:center;" id="errTxt">${errors}</p>
<div class="container center" style="font-family:'Microsoft YaHei UI Light' ;  font-weight: lighter ; padding: 5%; color: white; height: 180px;" >

    <span style="font-size: xx-large ;">MirrorCloud Register Form</span>
    <p>Get to your files and photos from anywhere, on any device. Share and work together with anyone in your work and life.</p>
</div>
<div class="container center" style="font-family:'Microsoft YaHei UI Light'  ; color: white ;font-size: larger">

    <div class="row">
        <div class="col m3 " ></div>
        <div class="col m6  " >

            <div class="row">
                <form:form action="processRegister" method="POST" class="col s12">
                    <div class="row">
                        <div class="input-field col s12" style="height: 25px;">
                            <form:input path="fullName" placeholder="Fullname..." id="fnam" type="text" class="validate short" style=" background-color: white ; color:black; padding-left: 3%; padding-right: 3%"></form:input>
                        </div>
                    </div>
                    <div class="row">
                        <div class="input-field col s12" style="height: 25px;">
                        <form:input path="uname" placeholder="Username..." id="unam" type="text" class="validate short" style=" background-color: white ; color:black; padding-left: 3%; padding-right: 3%"></form:input>
                        </div>
                    </div>
                    <div class="row">
                        <div class="input-field col s12" style="height: 25px;">
                        <form:input path="email" placeholder="Email..." id="email" type="email" class="validate short" style=" background-color: white ; color:black; padding-left: 3%; padding-right: 3%"></form:input>
                        </div>
                    </div>
                    <div class="row">
                        <div class="input-field col s12" style="height: 25px;">
                            <form:password path="pass" placeholder="Password..." id="password" class="validate short" style=" background-color: white ; color:black;padding-left: 3%; padding-right: 3%;"></form:password>
                        </div>
                    </div>
                    <div class="row">
                        <div class="input-field col s12" style="height: 25px;">
                            <input placeholder="Confirmpassword..." id="password_again" type="password" class="validate short" style=" background-color: white ; color:black;padding-left: 3%; padding-right: 3%;">
                        </div>
                    </div>
                    <div class="row">
                        <div class="input-field col s12" style="height: 25px;">
                            <input type="checkbox" id="test6" checked="checked" />
                            <label for="test6">Accept Terms and Conditions.</label>
                        </div>
                    </div>
                    <div class="row ">
                        <div class="col m12 " style="height: 25px;">

                            <button class="btn cyan" style="margin-top: 2%;">Register</button>
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
    
    $(document).ready(function () {
        $("#fnam").blur(function () {

            if ($(this).val() == null || $(this).val() == undefined || $(this).val() == "") {
                $(this).css("border", "2px solid red");
                return false;
            }

        });
        $("#fnam").on("blur", function () {
            if ($(this).val().match('^[a-zA-Z0-9 ]{3,16}$')) {

                $(this).css("border", "2px solid green");
                return true;
            } else {

                $(this).css("border", "2px solid red");
                return false;
            }
        });
        $("#unam").blur(function () {

            if ($(this).val() == null || $(this).val() == undefined || $(this).val() == "") {
                $(this).css("border", "2px solid red");
                return false;
            }

        });
        $("#unam").on("blur", function () {
            if ($(this).val().match('^[a-zA-Z0-9_-]{3,16}$')) {

                $(this).css("border", "2px solid green");
                return true;
            } else {
                $(this).css("border", "2px solid red");
                return false;
            }
        });
//Janiii agr key hi press nii hony denii too ye code lgain
        $("#fnam").bind("keypress", function (event) {
            if (event.charCode != 0) {
                var regex = new RegExp("^[a-zA-Z ]+$");
                var key = String.fromCharCode(!event.charCode ? event.which : event.charCode);
                if (!regex.test(key)) {
                    event.preventDefault();
                    return false;
                }
            }
        });
// 

        $('#email1').focusout(function () {

            $('#email1').filter(function () {
                var emil = $('#email1').val();
                var emailReg = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
                if (!emailReg.test(emil)) {
                    $(this).css("border", "2px solid red");
                    return false;
                } else {
                    $(this).css("border", "2px solid green");
                    return true;
                }
            });
        });
        $("form").on("submit", function (event) {
            if ($("#fnam").val() == null || $("#fnam").val() == undefined || $("#fnam").val() == "") {
                alert("Enter FullName");
                $("#fnam").css("border", "2px solid red");
                event.preventDefault();
                return false;
            }

            if ($("#unam").val() == null || $("#unam").val() == undefined || $("#unam").val() == "") {
                alert("Enter UserName");
                $("#unam").css("border", "2px solid red");
                event.preventDefault();
                return false;
            }
            if (!$("#unam").val().match('^[a-zA-Z0-9]{3,16}$')) {

                alert("Invalid UserName");
                $("#unam").css("border", "2px solid red");
                event.preventDefault();
                return false;
            }
            var emil = $('#email1').val();
            var emailReg = '^[A-Z0-9._%+-]+@[A-Z0-9.-]+.[A-Z]{2,4}$'; 
//            var emailReg = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
            if (!emailReg.test(emil)) {
                alert("Invalid Email");
                $('#email1').css("border", "2px solid red");
                event.preventDefault();
                return false;
            }
            var password = $("#password").val();
            var confirmPassword = $("#password_again").val();
            if (password == null || password == undefined || password == ""){

                alert("Please Enter Password.");
                event.preventDefault();
                return false;

            }
            if (password != confirmPassword) {
                alert("Passwords doesn't match.");
                event.preventDefault();
                return false;
            }
            return true;
        });
    });

</script>
    </body>
</html>
