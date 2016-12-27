
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page import="mirror.web.UserPC"%>
<%@page import="mirror.web.SystemDetail"%>
<%@page import="mirror.web.User"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <!-- Head -->
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>Shared With You - MirrorCloud</title>
        <link href="resources/css/materialize.min.css" rel="stylesheet"/>
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet"/>
        <!--<link href="resources/css/materialdesignicons.min.css" type="text/css" rel="stylesheet" media="screen,projection"/>-->
        
        <link href="resources/css/style.css" type="text/css" rel="stylesheet" media="screen,projection"/>
        <link href="resources/css/dropzone.css" type="text/css" rel="stylesheet" media="screen,projection"/>
        <script src="resources/js/jquery-2.1.1.min.js"></script>
        <script src="resources/js/dropzone.js"></script>
        
        <style>
            /* Start by setting display:none to make this hidden.
                Then we position it in relation to the viewport window
                with position:fixed. Width, height, top and left speak
                speak for themselves. Background we set to 80% white with
                our animation centered, and no-repeating */
            .brand-logo img{
                width: 85%;
                padding-bottm:0px; 
              }
              .brand-logo{
                height:10px;
              }
            #upload-widget{
                border:1px dashed blue;
                padding:30px;
              }
              .dz-message{
                color:lightblue;
                font-weight: bold;
                font-size:medium;
              }


             .loadingmodal {
                 display:    none;
                 position:   fixed;
                 z-index:    1000;
                 top:        0;
                 left:       0;
                 height:     100%;
                 width:      100%;
                 background: rgba( 255, 255, 255, .5 ) 
                             
             }

             /* When the body has the loading class, we turn
                the scrollbar off with overflow:hidden */
             body.loading {
                 overflow: hidden;   
             }

             /* Anytime the body has the loading class, our
                modal element will be visible */
             body.loading .loadingmodal {
                 display: block;
             }
             .breadcrumb{
                 color:blue;
             }
             .breadcrumb.active{
                pointer-events: none !important;
                cursor: default;
                color:black;
            }
            #ab {
                height: 40px;
                line-height: 40px;
            }


            #ab .button-collapse i {
                height: 40px;
                line-height: 40px;
            }

            #ab .brand-logo {
                font-size: 1.6rem;
            }

            @media only screen and (min-width: 601px){
                #ab, nav .nav-wrapper i, nav a.button-collapse, nav a.button-collapse i {
                    height: 30px;
                    line-height: 30px;
                }
            }
            #x {
                height: 68px;
                line-height: 68px;
            }
            .invisible
            {
                display:none;
            }
        </style>
        
        
    </head>
    <body class="">
        
        <!-- Main Top NavBar -->
        <div class="navbar-fixed"  style="z-index:1001;">
            <nav>
                <div class="nav-wrapper  white">
                    <a href="#!" class="brand-logo"><img src="resources/img/logo1.png"  width="80%" alt="Logo"/></a>
                    <a href="#" data-activates="mobile-demo" class="button-collapse"><i class="material-icons">menu</i></a>
                    <ul class="right hide-on-med-and-down">
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
        <!-- Paths Navigation Bar -->
        <div class="navbar-fixed">
            <nav>
                <div class="nav-wrapper blue-grey  lighten-4 fixed"  >
                    <div class="col s12" id="navPaths">
                        <a href="#" data-num="${num-1}" data-path="${parentPath}" class="breadcrumb active">Shared</a>
                    </div>
                </div>
            </nav>
        </div>
        <!-- Blue Options Bar -->
        <div class="row navbar-fixed" style="height:10px; ">
            <nav id="ab" class="center-on-small-only  " >
                <div class="nav-wrapper  blue">
                    <ul class="hide-on-small-and-down" >
                        <li><a href="" class="white-text invisible">New <span style="font-size: inherit;" class="material-icons">input</span></a></li>
                        <li><a href="#" class=" white-text invisible" id="uploadLink">Upload <span style="font-size: inherit;" class="material-icons">import_export</span></a>
                            <form action="#" enctype="multipart/form-data" name="uploadForm" id="uploadForm">
                                <input type="file" id="uploadField" style="display:none;" name="file"/>
                            </form>
                        </li>
                        <li><a href="#"  id="backLink" class="white-text "><span style="font-size: inherit;" class="tiny material-icons">skip_previous</span>Back </a></li>
                        <!--<li><a href="#"  id="refreshLink" class="white-text "><span style="font-size: inherit;" class="tiny material-icons">replay</span>Refresh </a></li>-->
                            <li><a href="#" id="downloadSelectedLink" class=" white-text invisible">Download <span style="font-size: inherit;" class="material-icons">loop</span></a></li>
                        
                        <li><a id="downloadDirLink" href="#zipWaitModal" class="modal-trigger white-text invisible">Download This <span style="font-size: inherit;" class="material-icons">markunread_mailbox</span></a></li>
                        <c:if test="${dataAccess==1}">
                            <li><a id="deleteLink" href="#confirmDeleteModal" class="modal-trigger white-text invisible">Delete <span style="font-size: inherit;" class="material-icons">delete</span></a></li>
                            <li><a id="renameLink" href="#renameModal" class="modal-trigger white-text left invisible">Rename <span style="font-size: inherit;" class="material-icons">description</span></a></li>
                        </c:if>
                    </ul>
                    <ul class="right hide-on-small-and-down">
                        <li><a href="#" id="selectedStatus" class="dropdown-button white-text invisible" data-activates="dropdown1" ><span style="font-size: inherit;" class="material-icons">done</span> <span id="selectedCount"></span> Item/s Selected</a></li>
                    </ul>
                    
                    <ul id="dropdown1" class="dropdown-content dropdown-menu sub-menu blue">
                        <li><a href="#" id="selectAllLink" class="white-text"><span style="font-size: inherit;" class="material-icons">done_all</span> Select All</a></li>
                        <li class="divider white-text"></li>
                        <li><a href="#" id="deselectAllLink" class="white-text"><span style="font-size: inherit;" class="material-icons">not_interested</span> Deselect All</a></li>
                    </ul>
                    <ul class="hide-on-med-and-up" id="mobile-demo1">
                        <li><a href="" class="white-text"><span style="font-size: inherit;" class="material-icons">input</span></a></li>
                        <li><a href="#" class=" white-text" id="uploadLink"><span style="font-size: inherit;" class="material-icons">import_export</span></a></li>
                        <li><a href="" class=" white-text"><span style="font-size: inherit;" class="material-icons">swap_horiz</span></a></li>
                        <li><a href="" class=" white-text"><span style="font-size: inherit;" class="material-icons">loop</span></a></li>
                        <li><a href="" class=" white-text"><span style="font-size: inherit;" class="material-icons">equalizer</span></a></li>
                        <li><a href="" class=" white-text"><span style="font-size: inherit;" class="material-icons">call_merge</span></a></li>
                        <li><a href="" class=" white-text left"><span style="font-size: inherit;" class="material-icons">description</span></a></li>
                    </ul>
                    <ul class="right hide-on-med-and-up ">
                        <li><a href="" class=" white-text"><span style="font-size: inherit;" class="material-icons">delete</span></a></li>
                    </ul>
                </div>
            </nav>
        </div>
                                        <br/>
        <div class="row" >
            <div class="col m2" data-pcId="${pc_id}" id="pcd">
                
            </div>
            <div class="col m8">
                <!-- Drop Zone for Uploading Files --><form id="upload-widget" method="post" action="uploadWebFile" class="dropzone" style="display:none;">
                    <div class="fallback">
                        <input name="file" type="file" />
                    </div>
                </form>
                
                <% List<String> files = (List<String>) request.getAttribute("files");
                if(files!=null){%>
                <input type="text" hidden="hidden" value="${paths}" id="shPths" />
                <input type="text" hidden="hidden" value="${parentPath}" id="shPth" />
                <input type="text" hidden="hidden" value="${num}" id="shNm" />
                <input type="text" hidden="hidden" value="${link}" id="shareLink" />
<!--                <div class="col m12">
                    <span><a href='#' id="backLink" class="chip"><span style="font-size: inherit;" class="material-icons">fast_rewind</span>Back</a></span>
                </div>-->
                <div id="dirview" class="row">
                    <%
                    int fileNum =1;
                    for(String file : files)
                    { if(file.contains("."))
                            continue;
                    %>
                    <a href="#" class="link">
                        <div class="col m3" style="">
                            <div class="card blue hoverable">
                                <div class="blue right">
                                    <form action="#">
                                        <p>
                                            <%
                                                String id = "file"+fileNum;
                                                fileNum++;
                                            %>
                                            <input type="checkbox" class="filled-in"  id="<%out.println(id);%>"/>
                                            <label for="<%out.println(id);%>" style="display:none;" class="white-text checks" ></label>
                                        </p>
                                    </form>
                                </div>
                                <div class="card-content white-text">
                                  <span class="truncate card-title"><%out.print(file);%></span>
                                  <p>Remote Directory</p>
                                </div>
                                <div class="card-action indigo darken-1">
                                    <span class="white-text">Double Click to Browse</span>
                                </div>
                              </div>
                        </div>
                    </a>
                    <%
                    } %>
                </div>
                <div id="filesview" class="row">
                    <%
                    fileNum =1;
                    for(String file : files)
                    { if(!file.contains("."))
                            continue;
                    %>
                    <div class="col m3" style="">
                        <a href="#" title="<%out.print(file);%>" class="link2"></a>
                        <div class="card indigo darken-1 hoverable">
                            <a href="#" title="<%out.print(file);%>" class="link2">
                                <div class="indigo darken-1 right">
                                    <form action="#">
                                        <p>
                                            <%
                                                String id = "file"+fileNum;
                                                fileNum++;
                                            %>
                                            <input type="checkbox" class="filled-in"  id="<%out.println(id);%>"/>
                                            <label for="<%out.println(id);%>" style="display:none;" class="white-text checks" ></label>
                                        </p>
                                    </form>
                                </div>
                                <div class="card-content white-text">
                                    <span class="truncate card-title"><%out.print(file);%></span>
                                    <p>Remote File</p>
                                </div>
                                <div class="card-action blue">
                                    <span class="white-text">Double Click to Download</span>
                                </div>
                            </a>
                        </div>
                    </div>
                                    <%
                    } %>
                </div>
            <% } %>
        </div>
    </div>

    
    <div id="zipWaitModal" class="modal" style="background-color:rgb(25,145,236);">
        <div class="modal-content center">
            <p id="zipWaitModalMsg" class="white-text">
                Please wait while your requested data is being compressed. 
            </p>
            <div class="row">
                <div class="col offset-m4 m4" id="loaderDiv">

            </div>
            </div>

        </div>
        <div class="modal-footer">
            <a href="#" class="modal-action modal-close waves-effect waves-red btn-flat ">Send to Background</a>
        </div>
    </div> 
    <div id="sysInfoModal" class="modal" >
        <div class="modal-content">
            <span id="magnify" class="hide">${user.id}</span>
            <h5 class="center">Your System Information</h5>
            <div class="row">
                <div class="col m4">
                    
                </div>
                <div class="col m8">
                    
                    <p><b>Computer Name : </b> ${thisPc.detail.machineName}</p>
                    <p><b>Model : </b> ${thisPc.detail.sysModel} </p>
                    <p class="truncate"><b>Operating System : </b> ${thisPc.detail.osName} </p>
                    <p><b>Processor : </b>  ${thisPc.detail.cpu} </p>
                    <p><b>Memory : </b> ${thisPc.detail.memory} </p>
                    <p><b>H/W Address : </b> ${thisPc.detail.macAddress} </p>
                </div>
                
            </div>
        </div>
    </div> 
    <div id="renameModal" class="modal blue" >
        <h5 class="center modal-header white-text">Rename </h5>
        <div class="modal-content white">
            
            <div class='row'>
                <p class="col s12 center-align">Please Enter a New Name </p>
                <div class='input-field col s12 offset-m4 m4'>
                    <input class='validate' type="text" id="newNmae"/>
                    <label for="newName">New Name</label>
                </div>
            </div>
        </div>
        <div class="modal-footer blue-grey lighten-5">
            <a href="#" class="modal-action modal-close waves-effect waves-red btn-flat ">Cancel</a>
            <a href="#" id="renameFileBtn" class="waves-effect waves-light btn cyan">Rename</a>
        </div>
    </div> 
    <div id="confirmDeleteModal" class="modal blue" >
        <h5 class="center modal-header white-text">Confirm Your Action</h5>
        <div class="modal-content white">
            
            <div class='row'>
                <p class="col s12 center-align">Are you sure want to delete the selected file(s)? </p>
            </div>
        </div>
        <div class="modal-footer blue-grey lighten-5">
            <a href="#" id="confirmDeleteBtn" class="waves-effect waves-light btn red">Yes! Delete File(s)</a>
            <a href="#" class="modal-action modal-close waves-effect waves-light btn-flat">Cancel</a>
        </div>
    </div> 
    <div id="deletePcModal" class="modal blue" >
        <h5 class="center modal-header white-text">Confirm Your Action</h5>
        <div class="modal-content white">
            
            <div class='row'>
                <p class="col s12 center-align">Are you sure want to delete All the Data Synchronized for this PC? </p>
            </div>
        </div>
        <div class="modal-footer blue-grey lighten-5">
            <a href="#" id="deleteAllYesBtn" class="waves-effect waves-light btn red">Yes! Delete This PC</a>
            <a href="#" class="modal-action modal-close waves-effect waves-light btn-flat">Cancel</a>
        </div>
    </div>
    <div id="shareModal" class="modal bottom-sheet" >
        <h5 class="center modal-header blue-text white">Generating Public Link</h5>
        <div class="modal-content white">
            <div class='row white hide' id="sharePublicLink">
                <div class='input-field col s12'>
                    <input id="shareInput" type="text"/>
                    <label for="shareInput" class="shareLabel" >Anyone having this link can use the shared data.</label>
                </div>
            </div>
            <div id="shareWaiting" class="center">
                <div class="preloader-wrapper big active center">
                <div class="spinner-layer spinner-blue">
                  <div class="circle-clipper left">
                    <div class="circle"></div>
                  </div><div class="gap-patch">
                    <div class="circle"></div>
                  </div><div class="circle-clipper right">
                    <div class="circle"></div>
                  </div>
                </div>

                <div class="spinner-layer spinner-red">
                  <div class="circle-clipper left">
                    <div class="circle"></div>
                  </div><div class="gap-patch">
                    <div class="circle"></div>
                  </div><div class="circle-clipper right">
                    <div class="circle"></div>
                  </div>
                </div>

                <div class="spinner-layer spinner-yellow">
                  <div class="circle-clipper left">
                    <div class="circle"></div>
                  </div><div class="gap-patch">
                    <div class="circle"></div>
                  </div><div class="circle-clipper right">
                    <div class="circle"></div>
                  </div>
                </div>

                <div class="spinner-layer spinner-green">
                  <div class="circle-clipper left">
                    <div class="circle"></div>
                  </div><div class="gap-patch">
                    <div class="circle"></div>
                  </div><div class="circle-clipper right">
                    <div class="circle"></div>
                  </div>
                </div>
              </div>            <!-- Place at bottom of page -->
            </div>
        </div>
        <div class="modal-footer blue-grey lighten-5">
            <a href="#" class="modal-action modal-close waves-effect waves-light btn-flat shareCloseBtn">Cancel</a>
        </div>
    </div>
<script type="text/javascript" src="resources/js/sharedDataScript.js"></script>

<script src="resources/js/materialize.js"></script>
        <script src="resources/js/init.js"></script>
            
    
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

        <div class="loadingmodal">
            <div class="preloader-wrapper big active center" style="position:fixed;top:50%;left:50%">
            <div class="spinner-layer spinner-blue">
              <div class="circle-clipper left">
                <div class="circle"></div>
              </div><div class="gap-patch">
                <div class="circle"></div>
              </div><div class="circle-clipper right">
                <div class="circle"></div>
              </div>
            </div>

            <div class="spinner-layer spinner-red">
              <div class="circle-clipper left">
                <div class="circle"></div>
              </div><div class="gap-patch">
                <div class="circle"></div>
              </div><div class="circle-clipper right">
                <div class="circle"></div>
              </div>
            </div>

            <div class="spinner-layer spinner-yellow">
              <div class="circle-clipper left">
                <div class="circle"></div>
              </div><div class="gap-patch">
                <div class="circle"></div>
              </div><div class="circle-clipper right">
                <div class="circle"></div>
              </div>
            </div>

            <div class="spinner-layer spinner-green">
              <div class="circle-clipper left">
                <div class="circle"></div>
              </div><div class="gap-patch">
                <div class="circle"></div>
              </div><div class="circle-clipper right">
                <div class="circle"></div>
              </div>
            </div>
          </div>            <!-- Place at bottom of page -->
        </div>    
</body>
</html>
