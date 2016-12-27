<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home Page</title>
        <link href="resources/css/materialize.min.css" rel="stylesheet"/>
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <link href="resources/css/style.css" type="text/css" rel="stylesheet" media="screen,projection"/>
        <script src="resources/js/fblogin.js"></script>
    </head>

    <body>
        <div class="navbar-fixed">
            <nav>
                <div class="nav-wrapper  white">
                    <a href="#!" class="brand-logo"><img src="resources/img/logo1.png"  width="80%" alt="Logo"/></a>
                    <a href="#" data-activates="mobile-demo" class="button-collapse"><i class="material-icons">menu</i></a>
                    <ul class="right hide-on-med-and-down">

                    <li><a href="" ><i class="material-icons">search</i></a></li>
                    <li><span class="" style="color:black;">Welcome <span style="font-weight:bold;">${uname}</span></span></li>
                    <%
                                    if(session.getAttribute("user")==null)
                                    {
                                %>
                        <li ><a class="waves-effect waves-light  cyan btn modal-trigger" href="#modal1" style=" border-radius: 15px 50px 30px;">Login</a></li>
                        <li><a class="waves-effect waves-light btn   cyan modal-trigger" href="#modal2" style=" border-radius: 15px 50px 30px;">Register</a></li>
                                <% } else
                                    {
                                        %>
                                        
                        <li><a class="waves-effect waves-light btn cyan " href="logout" style=" border-radius: 15px 50px 30px;">Logout</a></li>
                        <%
                                    }
                                    
                        %>
                    </ul>
                    <ul class="side-nav" id="mobile-demo">
                        <%
                                    if(session.getAttribute("user")==null)
                                    {
                                %>
                        <li><a class="modal-trigger" href="#modal1">Login</a></li>
                        <li><a class=" modal-trigger" href="#modal2">Register</a></li>
                        <% } else
                                    {
                                        %>
                        
                        <%
                                    }
                                    
                        %>
                    </ul>
                </div>
            </nav>
        </div>
        <div id="index-banner" class="parallax-container">
        <div class="section no-pad-bot">
          <div class="container">
            <br><br>
            <h1 class="header center  ">Mirror Cloud</h1>
            <div class="row center">
              <h5 class="header col s12 light">Cloud computing is a type of Internet-based computing that provides shared computer processing.</h5>
            </div>
            <div class="row center">
                <%
                                    if(session.getAttribute("user")!=null)
                                    {
                                        %>
                                        <a href="viewOwnData" id="download-button" class="btn-large waves-effect waves-light   cyan ">View Synced Data</a>
                                        <%
                                    }
                                    else{
                                        %>
                                            <a href="" id="download-button" class="btn-large waves-effect waves-light   cyan ">Get Started</a>
                                        <%
                                    }
                                    %>
              
            </div>
            <br><br>

          </div>
        </div>
        <div class="parallax"><img src="resources/img/background1.jpg" alt="Unsplashed background img 1"></div>
      </div>
        <div class="container">
   
      <!--   Icon Section   -->
      <div class="row ">
	  
 <div class="col s8 ">
          <div class="icon-block">
            <h2 class="center green-text"><i class="material-icons"></i></h2>
            <h5 class="" style="font-weight:bold">Take your docs anywhere</h5>
            <p class="light  flow-text">Save files on your computer, then access them on your phone from the road. Everything you keep in synced automatically to all your devices.</p>
          <a class="btn-floating btn-large waves-effect waves-light cyan"><i class="material-icons">add</i></a><span>    Learn more.</span>
        
		  </div>
        </div>
		 <div class="col s4 card-image ">
         
				<img src="resources/img/1.png" class="responsive-img" style="padding-top:40%">
			
        </div>
		
</div>
    
  </div>

  
  
  <div class="container">
   
      <!--   Icon Section   -->
      <div class="row">
	  <div class="col s4 card-image ">
         
				<img src="resources/img/2.png" class="responsive-img" style="padding-top:40%">
			
        </div>
	
		<div class="col s8 ">
          <div class="icon-block">
            <h2 class="center green-text"><i class="material-icons"></i></h2>
            <h5 class="" style="font-weight:bold">Work on slides together</h5>

            <p class="light  flow-text">Save files on your computer, then access them on your phone from the road. Everything you keep in Dropbox is synced automatically to all your devices.</p>
			<a class="btn-floating btn-large waves-effect waves-light cyan"><i class="material-icons">add</i></a><span>    Learn more.</span>
                 
		 </div>
        </div>
   </div>
    
  </div>
  
  <div class="container">
   
      <!--   Icon Section   -->
      <div class="row ">
	  
 <div class="col s8 ">
          <div class="icon-block">
            <h2 class="center green-text"><i class="material-icons"></i></h2>
            <h5 class="" style="font-weight:bold">Never lose a file again</h5>
            <p class="light  flow-text">Save files on your computer, then access them on your phone from the road. Everything you keep in synced automatically to all your devices.</p>
            <a class="btn-floating btn-large waves-effect waves-light cyan"><i class="material-icons">add</i></a><span>    Learn more.</span>
        
		  </div>
        </div>
		 <div class="col s4 card-image ">
         
				<img src="resources/img/1.png" class="responsive-img" style="padding-top:40%">
			
        </div>
		
</div>
    
  </div>

  <div class="parallax-container valign-wrapper">
    <div class="section no-pad-bot">
      <div class="container">
        <div class="row center">
          <h5 class="header col s12 light">Cloud computing is a type of Internet-based computing that provides shared computer processing.</h5>
        </div>
      </div>
    </div>
    <div class="parallax"><img src="resources/img/background2.jpg" alt="Unsplashed background img 2"></div>
  </div>

  <div class="container">
    <div class="section">

      <div class="row ">
          <ul class="collapsible popout" data-collapsible="accordion">
    <li>
      <div class="collapsible-header active"><i class="material-icons">filter_drama</i>How its Works</div>
      <div class="collapsible-body"><p>Sharing and Storing Data. Cloud computing, in turn, refers to sharing resources, software, and information via a network, in this case the Internet. The information is stored on physical servers maintained and controlled by a cloud computing provider, such as Apple in regards to iCloud.</p></div>
    </li>
    <li>
      <div class="collapsible-header"><i class="material-icons">place</i>Second</div>
      <div class="collapsible-body"><p>Sharing and Storing Data. Cloud computing, in turn, refers to sharing resources.</p></div>
    </li>
    <li>
      <div class="collapsible-header"><i class="material-icons">whatshot</i>Third</div>
      <div class="collapsible-body"><p>Sharing and Storing Data. Cloud computing, in turn, refers to sharing resources.</p></div>
    </li>
  </ul>
        
      </div>

    </div>
  </div>
<div class="parallax-container valign-wrapper">
    <div class="section no-pad-bot">
      <div class="container">
        <div class="row center">
          <h5 class="header col s12 light">Cloud computing is a type of Internet-based computing that provides shared computer processing.</h5>
        </div>
      </div>
    </div>
    <div class="parallax"><img src="resources/img/background3.jpg" alt="Unsplashed background img 3"></div>
  </div>

  <footer class="page-footer teal">
    <div class="container">
      <div class="row">
        <div class="col l6 s12">
          <h5 class="white-text">Mirror Cloud</h5>
          <p class="grey-text text-lighten-4">We are a team of college students working on this project like it's our full time job. Any amount would help support and continue development on this project and is greatly appreciated.</p>


        </div>
        <div class="col l3 s12">
          <h5 class="white-text">Help and Support</h5>
          <ul>
            <li><a class="white-text" href="#!">Help Center</a></li>
            <li><a class="white-text" href="#!">Terms of Use</a></li>
            <li><a class="white-text" href="#!">Forums</a></li>
            <li><a class="white-text" href="#!">Explore our system</a></li>
          </ul>
        </div>
		  <div class="col l3 s12">
          <h5 class="white-text">NewsLetters</h5>
          <ul>
            <li class="white-text">Get Envato Tuts+ updates, news, surveys & offers<li>
           <div class="row">
		<form class="col s12">
		  <div class="row">
			<div class="input-field col s12">
			  <input id="email" type="email" class="validate" placeholder="Enter Email">
			  <a class="white-text btn col s12 cyan" href="#!">Subscribe</a>
			</div>
		  </div>
		 </form>
		 </div>
				
          </ul>
        </div>
       
      </div>
    </div>
    <div class="footer-copyright">
      <div class="container center">
       <a class="brown-text text-lighten-3" href="">Trademarks and brands are the property of their respective owners.</a>
      </div>
    </div>
  </footer>
    
    
<!-- Modal Structure -->
<div   id="modal1" class="modal" >
   <div class="modal-content">
        <main>
            <center>
                <div class="container ">
                    <div  class="#ECECEC row" style="display: inline-block; padding: 10px 48px 20px 48px; ">
                        <img class="responsive-img" style="width: 250px;" src="resources/img/image.gif" /></center>
                        
                        <form:form action="processLogin" method="POST" class="col s12">
                            <div class='row'>
                                <div class='input-field col s12'>
                                    <form:input path="uname" id="uname" type="text"></form:input>
                                    <form:label path="uname" for="uname">Your Username</form:label>
                                </div>
                            </div>
                            <div class='row'>
                                <div class='input-field col s12'>
                                    <form:password class='validate' path="pass" id="pass" ></form:password>
                                    <form:label path="pass" for="pass">Your Password</form:label>
                                </div>
                              
                            </div>
                        <div class='row'>
                                    <button type='submit' name='btn_login' class='col s12 btn btn-large waves-effect cyan'>Login</button>
                                </div>
                            <center>
                            <span class="center"> Looking to </span><a href="#!" class="green-text">Create account</a><span class="center"> ? </span><br/>
                            <a href="#">Login with Facebook</a>
                            <fb:login-button scope="public_profile,email" onlogin="checkLoginState();">
                            </fb:login-button>

                            <div id="status">
                            </div>
                            <a href="#!" class="red-text">Forgot Password</a><span class="center"> ? </span>
                            </center>
                    </form:form>
                    </div>
                </div>
            
        </main>
    </div>
</div>


<!-- Modal Structure -->

<div   id="modal2" class="modal">
   <div class="modal-content" >
        <main>
            
            <center>
                <div class="container ">

                    <div  class="#ECECEC row" style="display: inline-block; padding: 10px 48px 20px 48px; ">
                        <img class="responsive-img" style="width: 250px;" src="resources/img/image.gif" /></center>
                        <form:form action="processRegister" method="POST" class="row">
                          
							<div class="col s6">
                            <div class='row'>
                                <div class='input-field col s12'>
                                    <form:input class='validate' path="fullName" id="fullName" type="text"></form:input>
                                    <form:label path="fullName" for="fullName">Full Name</form:label>
                                </div>
                            </div>

                           <div class='row'>
                                <div class='input-field col s12'>
                                    <form:input class='validate' path="email" id="email" type="email"></form:input>
                                    <form:label path="email" for="email">Email</form:label>
                                </div>
                            </div>
							<div class='row'>
                                <div class='input-field col s12'>
                                    <input type="password" class='validate' id="cpass" />
                                    <label  for="cpass">Confirm Password</label>
                                </div>
                            </div>
							</div>
							<div class="col s6">
                            <div class='row'>
                                <div class='input-field col s12'>
                                    <form:input class='validate' path="uname" id="uname" type="text"></form:input>
                                    <form:label path="uname" for="uname">User Name</form:label>
                                </div>
                            </div>

                            <div class='row'>
                                <div class='input-field col s12'>
                                    <form:password class='validate' path="pass" id="pass" ></form:password>
                                    <form:label path="pass" for="pass">Password</form:label>
                                </div>
                              
                            </div>
							 
							  <div class='row'>
                                <div class='input-field col s12'>
                                    <input class='validate' type='checkbox' name='chkbox' id='chkbox' />
                                    <label for='chkbox'>Terms and Conditions</label>
                                </div>
                    
                            </div>
							</div>

                                <div class='row'>
                                    <button type='submit' name='action' class='col s12 btn btn-large waves-effect cyan'>Register</button>
                                </div>
								<center>
							<span class="center"> Already have a   </span><a href="#!" class="green-text"> Account</a><span class="center"> ? </span><br/>
							
							</center>
                        </form:form>
                    </div>
                </div>
        </main>
</div>
</div>

        
        
        
        
        
        
        <script src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
        <script src="resources/js/materialize.js"></script>
        <script src="resources/js/init.js"></script>
        <script>
            $(document).ready(function(){
                // the "href" attribute of .modal-trigger must specify the modal ID that wants to be triggered
                $('.modal-trigger').leanModal();
       
                  $('.carousel').carousel();
    
            });
        </script>
    </body>
</html>
