<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta charset="utf-8">
        <meta name="robots" content="all,follow">
        <meta name="googlebot" content="index,follow,snippet,archive">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Mirror Cloud</title>
        <meta name="keywords" content="">
        <link href="http://fonts.googleapis.com/css?family=Roboto:400,100,100italic,300,300italic,500,700,800" rel="stylesheet" type="text/css">
        <link rel="stylesheet" href="resources/css/font-awesome.css">
        <link rel="stylesheet" href="resources/css/bootstrap.css">
        <link rel="stylesheet" href="resources/css/animate.css">
        <link rel="stylesheet" href="resources/css/style.default.css" id="theme-stylesheet">
        <link rel="stylesheet" href="resources/css/custom.css">
        <link rel="stylesheet" href="resources/css/owl.carousel.css">
        <link rel="stylesheet" href="resources/css/owl.theme.css">
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
    </head>
<body>        
<div id="all">
    <header>
        <div id="top">
            <div class="container">
                <div class="row">
                    <div class="col-xs-5 contact">
                        <p class="hidden-sm hidden-xs">Contact us on +92 347 533 7268 or ahmed.maddy@ymail.com.</p>
                        <p class="hidden-md hidden-lg"><a href="#" data-animate-hover="pulse"><i class="fa fa-phone"></i></a>  <a href="#" data-animate-hover="pulse"><i class="fa fa-envelope"></i></a>
                        </p>
                    </div>
                    <div class="col-xs-7">
                        <div class="social">
                            <a href="#" class="external facebook" data-animate-hover="pulse"><i class="fa fa-facebook"></i></a>
                            <a href="#" class="external gplus" data-animate-hover="pulse"><i class="fa fa-google-plus"></i></a>
                            <a href="#" class="external twitter" data-animate-hover="pulse"><i class="fa fa-twitter"></i></a>
                            <a href="#" class="email" data-animate-hover="pulse"><i class="fa fa-envelope"></i></a>
                        </div>
                        <div class="login">
                            
                            <span style="padding-right:5px;">Welcome <b>${uname}</b></span>
                                <%
                                    if(session.getAttribute("user")==null)
                                    {
                                %>
                                    <a href="#" data-toggle="modal" data-target="#login-modal"><i class="fa fa-sign-in"></i> <span class="hidden-xs text-uppercase">Sign in</span></a>
                                    <a href="register"><i class="fa fa-user"></i> <span class="hidden-xs text-uppercase">Sign up</span></a>      
                                <%
                                    }
                                    else
                                    {
                                        %>
                                        <a href="logout"><i class="fa fa-user"></i> <span class="hidden-xs text-uppercase">Logout</span></a>      
                                        <%
                                    }
                                %>
                                
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="navbar-affixed-top affix-top" data-spy="affix" data-offset-top="40">
            <div class="navbar navbar-default yamm" role="navigation" id="navbar">
                <div class="container">
                    <div class="navbar-header">
                        <a class="navbar-brand home" href="">
                            <h2 class="text-uppercase" style="margin-top:8px;">Mirror Cloud</h2>
                        </a>
                        <div class="navbar-buttons">
                            <button type="button" class="navbar-toggle btn-template-main" data-toggle="collapse" data-target="#navigation">
                                <span class="sr-only">Toggle navigation</span>
                                <i class="fa fa-align-justify"></i>
                            </button>
                        </div>
                    </div>
                    <!--/.navbar-header -->
                    <div class="navbar-collapse collapse" id="navigation">
                        <ul class="nav navbar-nav navbar-right">
                            <li class="active">
                                <a href="">Home</a>
                            </li>
                            <%
                                    if(session.getAttribute("user")!=null)
                                    {
                                        %>
                                        <li>
                                            <a href="viewOwnData">Data</a>
                                        </li>
                                        <%
                                    }
                                    %>
                            
                            <li>
                                <a href="">About</a>
                            </li>
                        </ul>
                    </div>
                    <!--/.nav-collapse -->
                </div>
            </div>
            <!-- /#navbar -->
        </div>    
    </header>
    <div class="modal fade" id="login-modal" tabindex="-1" role="dialog" aria-labelledby="Login" aria-hidden="true">
    <div class="modal-dialog modal-sm">

        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="Login">Client login</h4>
            </div>
            <div class="modal-body">
                <form:form action="processLogin" method="POST" class="col s12">
                    <div class="form-group">
                        <form:input path="uname" class="form-control" id="uname" type="text" placeholder="Username"></form:input>
                    </div>
                    <div class="form-group">
                        <form:password class="form-control" path="pass" id="pass" placeholder="Password" ></form:password>
                    </div>
                    <p class="text-center">
                        <button class="btn btn-template-main"><i class="fa fa-sign-in"></i> Log in</button>
                    </p>
                </form:form>
                
<!--                
                <form action="http://localhost/ecomproject/home/attemptLogin" method="post">
                    <div class="form-group">
                        <input type="email" class="form-control" id="email_modal" placeholder="email" name="txt_email">
                    </div>
                    <div class="form-group">
                        <input type="password" class="form-control" id="password_modal" placeholder="password" name="txt_pass">
                    </div>
                    <p class="text-center">
                        <button class="btn btn-template-main"><i class="fa fa-sign-in"></i> Log in</button>
                    </p>

                </form>-->

                <p class="text-center text-muted">Not registered yet?</p>
                <p class="text-center text-muted"><a href="register"><strong>Register now</strong></a>! It is easy and done in 1&nbsp;minute</p>

            </div>
        </div>
    </div>
</div>    
<!--<section>-->
    <!-- *** HOMEPAGE CAROUSEL ***
_________________________________________________________ -->

<!--    <div class="home-carousel">

        <div class="dark-mask"></div>

        <div class="container">
            <div class="homepage owl-carousel">
                <div class="item">
                    <div class="row">
                        <div class="col-sm-5 right">
                            <p>
                                <img src="http://localhost/ecomproject/Assets/img/logo copy.png" alt="">
                            </p>
                            <h1>Welcome to AutoMania</h1>
                            <p>Shop Your Favorite Car Online.
                                <br />Compare with others.</p>
                        </div>
                        <div class="col-sm-7">
                            <img class="img-responsive" src="http://localhost/ecomproject/Assets/img/homeSlider1.png" alt="">
                        </div>
                    </div>
                </div>
                <div class="item">
                    <div class="row">

                        <div class="col-sm-7 text-center">
                            <img class="img-responsive" src="http://localhost/ecomproject/Assets/img/homeSlider2.png" alt="">
                        </div>

                        <div class="col-sm-5">
                            <h2>46 HTML pages full of features</h2>
                            <ul class="list-style-none">
                                <li>Sliders and carousels</li>
                                <li>4 Header variations</li>
                                <li>Google maps, Forms, Megamenu, CSS3 Animations and much more</li>
                                <li>+ 11 extra pages showing template features</li>
                            </ul>
                        </div>

                    </div>
                </div>
                <div class="item">
                    <div class="row">
                        <div class="col-sm-5 right">
                            <h1>Design</h1>
                            <ul class="list-style-none">
                                <li>Clean and elegant design</li>
                                <li>Full width and boxed mode</li>
                                <li>Easily readable Roboto font and awesome icons</li>
                                <li>7 preprepared colour variations</li>
                            </ul>
                        </div>
                        <div class="col-sm-7">
                            <img class="img-responsive" src="http://localhost/ecomproject/Assets/img/homeSlider3.png" alt="">
                        </div>
                    </div>
                </div>
                <div class="item">
                    <div class="row">
                        <div class="col-sm-7">
                            <img class="img-responsive" src="http://localhost/ecomproject/Assets/img/homeSlider4.png" alt="">
                        </div>
                        <div class="col-sm-5">
                            <h1>Easy to customize</h1>
                            <ul class="list-style-none">
                                <li>7 preprepared colour variations.</li>
                                <li>Easily to change fonts</li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
             /.project owl-slider 
        </div>
    </div>-->

    <!-- *** HOMEPAGE CAROUSEL END *** -->
<!--</section>-->
<!--<div class="container">
    <section>
        <div class="row portfolio">

            <div class="col-md-12">
                <div class="heading text-center">
                    <h3>Featured Products</h3>
                    <br/>

                </div>
                <p class="lead text-center">We have some marvellous cars that you will love to see.</p>
            </div>
                                <div class="col-sm-6 col-md-3">
                        <div class="box-image">
                            <div class="image">
                                <img src="http://localhost/ecomproject/Assets/img/uploads/img1-1.jpg" alt="" class="img-responsive" style="width: 262px; height: 196px;">
                            </div>
                            <div class="bg"></div>
                            <div class="name">
                                <h3><a href="http://localhost/ecomproject/home/showCar/1">Honda R8</a></h3>
                            </div>
                            <div class="text">
                                <p class="buttons">
                                    <a href="http://localhost/ecomproject/home/showCar/1" class="btn btn-template-transparent-primary">View</a>
                                    <a href="#" class="btn btn-template-transparent-primary">Compare</a>
                                </p>
                            </div>
                        </div>
                         /.box-image 

                    </div>
                                <div class="col-sm-6 col-md-3">
                        <div class="box-image">
                            <div class="image">
                                <img src="http://localhost/ecomproject/Assets/img/uploads/img2-1.jpg" alt="" class="img-responsive" style="width: 262px; height: 196px;">
                            </div>
                            <div class="bg"></div>
                            <div class="name">
                                <h3><a href="http://localhost/ecomproject/home/showCar/2">Honda Civic VTI Oriel</a></h3>
                            </div>
                            <div class="text">
                                <p class="buttons">
                                    <a href="http://localhost/ecomproject/home/showCar/2" class="btn btn-template-transparent-primary">View</a>
                                    <a href="#" class="btn btn-template-transparent-primary">Compare</a>
                                </p>
                            </div>
                        </div>
                         /.box-image 

                    </div>
                                <div class="col-sm-6 col-md-3">
                        <div class="box-image">
                            <div class="image">
                                <img src="http://localhost/ecomproject/Assets/img/uploads/img3-1.jpg" alt="" class="img-responsive" style="width: 262px; height: 196px;">
                            </div>
                            <div class="bg"></div>
                            <div class="name">
                                <h3><a href="http://localhost/ecomproject/home/showCar/3">Toyota B8</a></h3>
                            </div>
                            <div class="text">
                                <p class="buttons">
                                    <a href="http://localhost/ecomproject/home/showCar/3" class="btn btn-template-transparent-primary">View</a>
                                    <a href="#" class="btn btn-template-transparent-primary">Compare</a>
                                </p>
                            </div>
                        </div>
                         /.box-image 

                    </div>
                                <div class="col-sm-6 col-md-3">
                        <div class="box-image">
                            <div class="image">
                                <img src="http://localhost/ecomproject/Assets/img/uploads/img4-1.jpg" alt="" class="img-responsive" style="width: 262px; height: 196px;">
                            </div>
                            <div class="bg"></div>
                            <div class="name">
                                <h3><a href="http://localhost/ecomproject/home/showCar/4">BMW Y10</a></h3>
                            </div>
                            <div class="text">
                                <p class="buttons">
                                    <a href="http://localhost/ecomproject/home/showCar/4" class="btn btn-template-transparent-primary">View</a>
                                    <a href="#" class="btn btn-template-transparent-primary">Compare</a>
                                </p>
                            </div>
                        </div>
                         /.box-image 

                    </div>
            





        </div>
    </section>
</div>-->
<!--<br/>-->


<section class="bar background-pentagon no-mb">
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <div class="heading text-center">
                    <h2>Testimonials</h2>
                </div>

                <p class="lead">We have worked with many clients and we always like to hear they come out from the cooperation happy and satisfied. Have a look what our clients said about us.</p>


                <!-- *** TESTIMONIALS CAROUSEL ***
_________________________________________________________ -->

                <ul class="owl-carousel testimonials same-height-row">
                    <li class="item">
                        <div class="testimonial same-height-always">
                            <div class="text">
                                <p>One morning, when Gregor Samsa woke from troubled dreams, he found himself transformed in his bed into a horrible vermin. He lay on his armour-like back, and if he lifted his head a little he could see his brown
                                    belly, slightly domed and divided by arches into stiff sections.</p>
                            </div>
                            <div class="bottom">
                                <div class="icon"><i class="fa fa-quote-left"></i>
                                </div>
                                <div class="name-picture">
                                    <img class="" alt="" src="http://localhost/ecomproject/Assets/img/bmwceo.jpg" />
                                    <h5>Harald Krüger</h5>
                                    <p>CEO, BMW</p>
                                </div>
                            </div>
                        </div>
                    </li>
                    <li class="item">
                        <div class="testimonial same-height-always">
                            <div class="text">
                                <p>The bedding was hardly able to cover it and seemed ready to slide off any moment. His many legs, pitifully thin compared with the size of the rest of him, waved about helplessly as he looked. "What's happened to
                                    me? " he thought. It wasn't a dream.</p>
                            </div>
                            <div class="bottom">
                                <div class="icon"><i class="fa fa-quote-left"></i>
                                </div>
                                <div class="name-picture">
                                    <img class="" alt="" src="http://localhost/ecomproject/Assets/img/hondaceo.jpg">
                                    <h5>Takahiro Hachigo</h5>
                                    <p>CEO, Honda</p>
                                </div>
                            </div>
                        </div>
                    </li>
                    <li class="item">
                        <div class="testimonial same-height-always">
                            <div class="text">
                                <p>His room, a proper human room although a little too small, lay peacefully between its four familiar walls.</p>

                                <p>A collection of textile samples lay spread out on the table - Samsa was a travelling salesman - and above it there hung a picture that he had recently cut out of an illustrated magazine and housed in a nice, gilded
                                    frame.</p>
                            </div>
                            <div class="bottom">
                                <div class="icon"><i class="fa fa-quote-left"></i>
                                </div>
                                <div class="name-picture">
                                    <img class="" alt="" src="http://localhost/ecomproject/Assets/img/mercedesceo.jpg">
                                    <h5>Dieter Zetsche</h5>
                                    <p>CEO, Mercedes</p>
                                </div>
                            </div>
                        </div>
                    </li>
                    <li class="item">
                        <div class="testimonial same-height-always">
                            <div class="text">
                                <p>It showed a lady fitted out with a fur hat and fur boa who sat upright, raising a heavy fur muff that covered the whole of her lower arm towards the viewer. Gregor then turned to look out the window at the dull
                                    weather. Drops of rain could be heard hitting the pane, which made him feel quite sad.</p>
                            </div>

                            <div class="bottom">
                                <div class="icon"><i class="fa fa-quote-left"></i>
                                </div>
                                <div class="name-picture">
                                    <img class="" alt="" src="http://localhost/ecomproject/Assets/img/ferrariceo.jpg">
                                    <h5>Amedeo Felisa</h5>
                                    <p>CEO, Ferrari</p>
                                </div>
                            </div>
                        </div>
                    </li>
                    <li class="item">
                        <div class="testimonial same-height-always">
                            <div class="text">
                                <p>It showed a lady fitted out with a fur hat and fur boa who sat upright, raising a heavy fur muff that covered the whole of her lower arm towards the viewer. Gregor then turned to look out the window at the dull
                                    weather. Drops of rain could be heard hitting the pane, which made him feel quite sad. Gregor then turned to look out the window at the dull weather. Drops of rain could be heard hitting the pane, which made
                                    him feel quite sad.</p>
                            </div>

                            <div class="bottom">
                                <div class="icon"><i class="fa fa-quote-left"></i>
                                </div>
                                <div class="name-picture">
                                    <img class="" alt="" src="http://localhost/ecomproject/Assets/img/toyotaceo.jpg">
                                    <h5>Akio Toyoda</h5>
                                    <p>CEO, Toyota</p>
                                </div>
                            </div>
                        </div>
                    </li>
                </ul>
                <!-- /.owl-carousel -->

                <!-- *** TESTIMONIALS CAROUSEL END *** -->
            </div>

        </div>
    </div>
</section>

<section class="bar background-image-fixed-2 no-mb color-white text-center">
    <div class="dark-mask"></div>
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <div class="icon icon-lg" style="padding-top:23px;"><i class="fa fa-cloud"></i>
                </div>
                <h3 class="text-uppercase">Interested in Cloud Storage?</h3>
                <p class="lead">We have the world's best cloud storage ever.</p>
                <p class="text-center">
                    <a href="http://localhost/ecomproject/home/showAllBrands" class="btn btn-template-transparent-black btn-lg">Register Now</a>
                </p>
            </div>

        </div>
    </div>
</section>
<div id="map"></div>

    <br/>
    <footer id="footer">
    <div class="container">
        <div class="col-md-3 col-sm-6">
            <h4>About us</h4>

            <p>Mirror Cloud is a Cloud Storage Platform with tightly bound synchronization features for your data mobility.</p>

            <hr>

            <h4>Join our monthly newsletter</h4>

            <form>
                <div class="input-group">

                    <input class="form-control" type="text">

                            <span class="input-group-btn">

                        <button class="btn btn-default" type="button"><i class="fa fa-send"></i></button>

                    </span>

                </div>
                <!-- /input-group -->
            </form>

            <hr class="hidden-md hidden-lg hidden-sm">

        </div>
        <!-- /.col-md-3 -->

        <div class="col-md-3 col-sm-6">

            <h4>Blog</h4>

            <div class="blog-entries">
                <div class="item same-height-row clearfix">
                    <div style="height: 38px;" class="image same-height-always">
                        <a href="#">
                            <img class="img-responsive" src="http://localhost/ecomproject/Assets/img/detailsquare.jpg" alt="">
                        </a>
                    </div>
                    <div style="height: 38px;" class="name same-height-always">
                        <h5><a href="#">Blog post name</a></h5>
                    </div>
                </div>

                <div class="item same-height-row clearfix">
                    <div style="height: 38px;" class="image same-height-always">
                        <a href="#">
                            <img class="img-responsive" src="http://localhost/ecomproject/Assets/img/detailsquare.jpg" alt="">
                        </a>
                    </div>
                    <div style="height: 38px;" class="name same-height-always">
                        <h5><a href="#">Blog post name</a></h5>
                    </div>
                </div>

                <div class="item same-height-row clearfix">
                    <div style="height: 38px;" class="image same-height-always">
                        <a href="#">
                            <img class="img-responsive" src="http://localhost/ecomproject/Assets/img/detailsquare.jpg" alt="">
                        </a>
                    </div>
                    <div style="height: 38px;" class="name same-height-always">
                        <h5><a href="#">Very very long blog post name</a></h5>
                    </div>
                </div>
            </div>

            <hr class="hidden-md hidden-lg">

        </div>
        <!-- /.col-md-3 -->

        <div class="col-md-3 col-sm-6">

            <h4>Contact</h4>

            <p><strong>Universal Ltd.</strong>
                <br>13/25 New Avenue
                <br>Newtown upon River
                <br>45Y 73J
                <br>England
                <br>
                <strong>Great Britain</strong>
            </p>

            <a href="contact.html" class="btn btn-small btn-template-main">Go to contact page</a>

            <hr class="hidden-md hidden-lg hidden-sm">

        </div>
        <!-- /.col-md-3 -->



        <div class="col-md-3 col-sm-6">

            <h4>Photostream</h4>

            <div class="photostream">
                <div style="height: 84px;">
                    <a href="#">
                        <img src="http://localhost/ecomproject/Assets/img/photost1.png" class="img-responsive" alt="#">
                    </a>
                </div>
                <div style="height: 84px;">
                    <a href="#">
                        <img src="http://localhost/ecomproject/Assets/img/photost2.png" class="img-responsive" alt="#">
                    </a>
                </div>
                <div style="height: 84px;">
                    <a href="#">
                        <img src="http://localhost/ecomproject/Assets/img/photost3.png" class="img-responsive" alt="#">
                    </a>
                </div>
                <div style="height: 84px;">
                    <a href="#">
                        <img src="http://localhost/ecomproject/Assets/img/photost4.png" class="img-responsive" alt="#">
                    </a>
                </div>
                <div style="height: 84px;">
                    <a href="#">
                        <img src="http://localhost/ecomproject/Assets/img/photost5.png" class="img-responsive" alt="#">
                    </a>
                </div>
                <div style="height: 84px;">
                    <a href="#">
                        <img src="http://localhost/ecomproject/Assets/img/photost6.png" class="img-responsive" alt="#">
                    </a>
                </div>
            </div>

        </div>
        <!-- /.col-md-3 -->
    </div>
    <!-- /.container -->
</footer>
<div id="copyright">
    <div class="container">
        <div class="col-md-12">
            <p class="pull-left">© 2016. AutoMania</p>
            <p class="pull-right">Template by <a href="http://localhost/ecomproject/">Maddy</a>
                <!-- Not removing these links is part of the licence conditions of the template. Thanks for understanding :) -->
            </p>

        </div>
    </div>
</div>

</div>


<script>

    window.jQuery || document.write('<script src="resources/js/jquery-1.11.0.min.js"><\/script>')

</script>

<!--<script src="--><?//=base_url("Assets/js/jquery-1.11.0.min.js");?><!--"></script>-->
<script src="resources/js/bootstrap.js"></script>

<script src="resources/js/jquery.cookie.js"></script>
<script src="resources/js/waypoints.min.js"></script>
<script src="resources/js/jquery.counterup.min.js"></script>
<script src="resources/js/jquery.parallax-1.1.3.js"></script>
<script src="resources/js/front.js"></script>
<script src="resources/js/owl.carousel.min.js"></script>

<script src="https://maps.googleapis.com/maps/api/js?v=3.exp&amp;sensor=false"></script>

<script src="resources/js/gmaps.js"></script>
<script src="resources/js/gmaps.init.js"></script>
</body>

</html>