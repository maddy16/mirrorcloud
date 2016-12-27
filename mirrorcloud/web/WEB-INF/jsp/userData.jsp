<%-- 
    Document   : userData
    Created on : Sep 20, 2016, 2:59:13 PM
    Author     : ahmed
--%>


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
        <title>Your Files at MirrorCloud</title>
        <link href="resources/css/materialize.min.css" rel="stylesheet"/>
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet"/>
        <!--<link href="resources/css/materialdesignicons.min.css" type="text/css" rel="stylesheet" media="screen,projection"/>-->
        
        <link href="resources/css/style.css" type="text/css" rel="stylesheet" media="screen,projection"/>
        <link href="resources/css/dropzone.css" type="text/css" rel="stylesheet" media="screen,projection"/>
        <link rel="stylesheet" href="resources/css/jquery.contextmenu.css">
        <script src="resources/js/jquery-2.1.1.min.js"></script>
        <script src="resources/js/dropzone.js"></script>
        
        <style>
            .mychip{
                padding:5px;
                border-radius: 5px 5px;
                margin-left:2px;
                margin-right:2px;
                margin-bottom: 2px;
                display: inline-block;
            }
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
    
    
    <body>
        
        <!-- Main Top NavBar -->
        <div class="navbar-fixed" style="z-index:1001;">
            <nav>
                <div class="nav-wrapper  white">
                    <a href="/MirrorWeb"><div class="brand-logo"><img src="resources/img/logo2.png" class="responsive-img" alt="Mirror Cloud Logo"/></div></a>
                    <a href="#" data-activates="mobile-demo" class="button-collapse"><i class="material-icons">menu</i></a>
                    
                    <ul class="right hide-on-med-and-down">
                        
                        <li>
                            
                            <span style="color:black;">
                                
                                Welcome <span style="font-weight:bold;">
                                <% User user = (User)session.getAttribute("user"); 
                                if(user==null)
                                {
                                    String site = new String("MirrorWeb");
                                    response.setStatus(response.SC_MOVED_TEMPORARILY);
                                    response.setHeader("Location", site); 
                                }
                                else{
                                    out.println(user.getFullName());
                                }%></span>
                            </span>
                        </li>
                        <li><a class="waves-effect waves-light btn cyan modal-trigger" href="#sysInfoModal" style=" border-radius: 15px 50px 30px;">System Details</a></li>
                        <li><a class="waves-effect waves-light btn cyan" href="logout" style=" border-radius: 15px 50px 30px;">Logout</a></li>
                    </ul>
                            
                    <ul class="side-nav" id="mobile-demo">
                        <li><a class="modal-trigger" href="#sysInfoModal">System Details</a></li>
                        <li><a class=" modal-trigger" href="logout">Logout</a></li>    
                    </ul>
                </div>
            </nav>
        </div>
        <!-- Paths Navigation Bar -->
        <div class="navbar-fixed">
            <nav>
                <div class="nav-wrapper blue-grey  lighten-4 fixed"  >
                    <div class="col s12" id="navPaths">
                        <a href="#" data-num="1" data-path="" class="breadcrumb active">Master PC</a>
                    </div>
                </div>
            </nav>
        </div>
        <!-- Blue Options Bar -->
        <div class="row navbar-fixed" style="height:10px; ">
            <nav id="ab" class="center-on-small-only  " >
                <div class="nav-wrapper  blue">
                    <ul class="hide-on-small-and-down" >
                        <li><a href="#"  id="backLink" class="white-text "><span style="font-size: inherit;" class="tiny material-icons">skip_previous</span>Back </a></li>
                        <li><a href="#"  id="refreshLink" class="white-text "><span style="font-size: inherit;" class="tiny material-icons">replay</span>Refresh </a></li>
                        <li><a href="" class="white-text invisible">New <span style="font-size: inherit;" class="material-icons">input</span></a></li>
                        <li><a href="#" class=" white-text invisible" id="uploadLink">Upload <span style="font-size: inherit;" class="material-icons">import_export</span></a>
                            <form action="#" enctype="multipart/form-data" name="uploadForm" id="uploadForm">
                                <input type="file" id="uploadField" style="display:none;" name="file"/>
                            </form>
                        </li>
                        <li><a id="shareLink" href="#sharingToModal" class="modal-trigger white-text ">Share<span style="font-size: inherit;" class="material-icons">swap_horiz</span></a></li>
                        <!--<li><a href="#"  class="dropdown-button white-text " data-beloworigin="true" data-constrainwidth="false" data-activates="shareDropdown" >Share <span style="font-size: inherit;" class="material-icons">swap_horiz</span></a></li>-->
                        <li><a href="#" id="downloadSelectedLink" class=" white-text invisible">Download <span style="font-size: inherit;" class="material-icons">loop</span></a></li>
                        <li><a id="downloadDirLink" href="#zipWaitModal" class="modal-trigger white-text invisible">Download This <span style="font-size: inherit;" class="material-icons">markunread_mailbox</span></a></li>
                        <li><a href="" class=" white-text invisible">Move to <span style="font-size: inherit;" class="material-icons">equalizer</span></a></li>
                        <li><a href="" class=" white-text invisible">Copy to <span style="font-size: inherit;" class="material-icons">call_merge</span></a></li>
                        <li><a id="deleteLink" href="#confirmDeleteModal" class="modal-trigger white-text invisible">Delete <span style="font-size: inherit;" class="material-icons">delete</span></a></li>
                        <li><a id="renameLink" href="#renameModal" class="modal-trigger white-text left invisible">Rename <span style="font-size: inherit;" class="material-icons">description</span></a></li>
                    </ul>
                    <ul class="right hide-on-small-and-down">
                        <li><a href="#" id="selectedStatus" class="dropdown-button white-text invisible" data-activates="dropdown1" ><span style="font-size: inherit;" class="material-icons">done</span> <span id="selectedCount"></span> Item/s Selected</a></li>
                        <li><a href="#" id="moreOptions" class="dropdown-button white-text invisible" data-activates="dropdown2" ><span style="font-size: 20px; padding-top:5px; padding-left:50px;padding-right:50px;" class="material-icons">settings</span> <span id="selectedCount"></span>  </a></li>
                        <li><a href="" class=" white-text invisible">Remove from my Drive<span style="font-size: inherit;" class="material-icons">delete</span></a></li>
                    </ul>
                    
                    <ul id="dropdown1" class="dropdown-content dropdown-menu sub-menu blue">
                        <li><a href="#" id="selectAllLink" class="white-text"><span style="font-size: inherit;" class="material-icons">done_all</span> Select All</a></li>
                        <li class="divider white-text"></li>
                        <li><a href="#" id="deselectAllLink" class="white-text"><span style="font-size: inherit;" class="material-icons">not_interested</span> Deselect All</a></li>
                    </ul>
                    <ul id="dropdown2" class="dropdown-content dropdown-menu sub-menu blue hide">
                        <li><a href="#deletePcModal" id="deleteAllLink" class="modal-trigger white-text"><span style="font-size: inherit;" class="red-text material-icons">report_problem</span> Clear PC</a></li>
                    </ul>
                    <ul id="shareDropdown" class="dropdown-content dropdown-menu sub-menu blue">
                        <li><a href="#shareModal" id="shareReadOnly" class="modal-trigger white-text"><span style="font-size: inherit;" class="material-icons">report_problem</span> Read Only</a></li>
                        <li><a href="#shareModal" id="shareFullAccess" class="modal-trigger white-text"><span style="font-size: inherit;" class="material-icons">report_problem</span> Full Access</a></li>
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
                <%
                                    if((List<String>) request.getAttribute("files")!=null)
                                    {
                                    %>
                                    <h5 class="center">All Devices</h5>
                <ul class="collection">
                <c:forEach items="${pcs}" var="item">
                    <a href="viewOwnData?pcId=${item.pcId}"> <li style="min-height: 59px;line-height: 3.0rem;" class="collection-item avatar <c:if test="${pc_id==item.pcId}">
                                blue lighten-3
                            </c:if> black-text" >
                        <img data-tooltip="MAC Address : ${item.detail.macAddress} Operating System: ${item.detail.osName}" data-delay="5" data-position="top"  src="resources/img/icon.jpg" alt="" class="circle tooltipped"/>
                        <span class="title" style="font-weight: bold">${item.pcName}</span>
                        <span class="secondary-content  pcStatus" id="pcStatus_${item.pcId}"><img src="resources/img/app-off.png" /></span>
                      </li>
                      </a>
                    <!--<a href="viewOwnData?pcId=${item.pcId}" class="btn waves-effect waves-light">${item.pcName}</a>-->
                </c:forEach>
                    </ul>
                    <%
                                    } %>
            </div>
            <div class="col m8 " id="mythingy">
                <!-- Drop Zone for Uploading Files --><form id="upload-widget" method="post" action="uploadWebFile" class="dropzone" style="display:none;">
                    <div class="fallback">
                        <input name="file" type="file" />
                    </div>
                </form>
                
                <% List<String> files = (List<String>) request.getAttribute("files");
                if(files!=null)
                {%>
<!--                <div class="col m12">
                    <span><a href='#' id="backLink" class="chip"><span style="font-size: inherit;" class="material-icons">fast_rewind</span>Back</a></span>
                </div>-->
                <div id="dirview" class="row">
                    <%
                    int fileNum =1;
                    for(String file : files)
                    {%>
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
                                  <span class="card-title"><%out.print(file);%></span>
                                  <p>Remote Drive</p>
                                </div>
                                <div class="card-action indigo darken-1">
                                    <span class="white-text">Double Click to Browse</span>
                                </div>
                              </div>

                        </div>
                    </a>
                    <%
                    }
                }
                    else
                {%>
                <h5 class="center">Please Download and Install the Application to Synchronize your Master Data.</h5>
                    <%
                }%>
                </div>
            <div id="filesview" class="row"></div>
        </div>
    </div>
                
<footer class="page-footer teal" style="margin-top:400px;">
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
                <p class="col s12 center-align">File to Rename: <b><span id="fileToRename"></span></b> </p>
                <div class='input-field col s12 offset-m4 m4'>
                    <input class='validate' type="text" id="newNameTxt"/>
                    <label for="newName">Please Enter New Name</label>
                </div>
                <p class="col s12 center-align"><b>Note: </b>Please Enter New Name without file extension. E.g. newName instead of newName.mp3 </p>
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
    <div id="sharingToModal" class="modal blue" >
        <h5 class="center modal-header white-text" style="position:relative;">Please Select to whom you want to share</h5>
        <div class="modal-content white">
            <form action="#">
            <div class="row">
                
                    <div class='input-field col s12  m7'>
                        <input class='validate' type="text" id="shareSearchTerm" placeholder="Enter Name or Username"/>
                        <label for="shareSearchTerm">Share To</label>
                        
                <div id="toShareSelected">
                    
                </div>
                    </div>    
                    <div class="input-field col m2 s12">
                        <input type="checkbox" id="shareToAll"/>
                        <label for="shareToAll">Everyone</label>
                    </div>
                    <div class="input-field col s12 m3">
                            <select id="shareAccess">
                                <option value="1" selected>Read Only</option>
                                <option value="2">Full Access</option>
                              </select>
                              <label>Access</label>
                  </div>
                
            </div>
            <div class='row'>
                
                <div id="shareSuggestions" style="height:50px;">

                </div>
            </div>
            </form>
        </div>
        
            
        <!--</div>-->
        <div class="modal-footer blue-grey lighten-5">
            <a href="#" id="shareFinalBtn" class="waves-effect waves-light btn blue">Share</a>
            <a href="#" class="modal-action modal-close waves-effect waves-light btn-flat initialShareCloseBtn">Cancel</a>
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
<script type="text/javascript" src="resources/js/datascript.js"></script>

<script src="resources/js/materialize.js"></script>
<script src="resources/js/jquery.contextmenu.js"></script>
        <script src="resources/js/init.js"></script>
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
