            var LOGGED_IN_USER = parseInt($("#magnify").html());
            var PC_ID = $("#pcd").data("pcid");
            var json = null;
            var num =2;
            var path = "";
            var dp;            
            var selected = {};
            var selectedCount =0;
            var refreshed = true;
            var deletedCount = 0;
            var selectedShares = {};
            var selectedShareIndex=0;
            function share(paths,ids,access)
            {
                $.ajax({
                    url: 'share',
                    type: 'POST',
                    dataType: "text",
                    data: { path: paths.replace(/_/g,'\\'),shareTo:ids, userId: LOGGED_IN_USER,pc_id: PC_ID,access:access},
                    success: function(data)
                    {
                        $("#shareWaiting").addClass("hide");
                        $("#sharePublicLink").removeClass("hide");
                        $(".shareLabel").addClass("active");
                        var json = jQuery.parseJSON(data);
                        if(json.success)
                        {    
                            
                            $("#shareInput").val("http:\\\\localhost:8080/MirrorWeb/public?data="+json.link);
                        }
                        else
                        {
                            $("#shareInput").val(json.error);
                        }
                        $("#shareInput").prop("readonly", true);
                        $(".shareCloseBtn").html("Close");
                    },
                    error: function(jqXHR, textStatus, errorThrown)
                    {
                        // Handle errors here
                        $("#shareWaiting").addClass("hide");
                        $("#sharePublicLink").removeClass("hide");
                        $("#shareInput").val("Something Went Wrong. Please Try Again Later");
                        $("#shareInput").prop("readonly", true);
                        $(".shareCloseBtn").html("Close");
                    }
                });
            }
            function getChilds()
            {
                $("body").addClass("loading");
                $.ajax({      
                          type: 'POST', 
                          url: "viewDataAjax",
                          dataType: "text",
                          data: { path: path, num: num ,pc_id: PC_ID},

                          success: function(data) {
                              
                             json = $.parseJSON(data);
                             if(!json.loggedIn)
                             {
                                alert("Please Login to Continue");
                                location="";
                             }
                             else{
                                $("#dirview").empty();
                                $("#filesview").empty();
                                for(var i =1;$($(json)[0][i]).length>0;i++)
                                {
                                    if(json[i].isDirty)
                                    {
                                        var element = "<a href=\"#\"class=\"link\" title=\""+json[i].fileName+"\"><div class=\"col m3\" style=\"\"><div class=\"card blue hoverable\"><div class=\"blue right\"><form action=\"#\"><p><input type=\"checkbox\" class=\"filled-in\"  id=\"file"+i+"\"/><label for=\"file"+i+"\" class=\"white-text checks\" style=\"display:none;\"></label></p></form></div><div class=\"card-content white-text\"><span class=\"card-title truncate\">"+json[i].fileName+"</span><p>Remote Directory</p></div><div class=\"card-action indigo darken-1\"><span class=\"white-text \">Double Click to Browse</span></div></div</div></a>";
                                        $("#dirview").append(element);
                                    }
                                    else
                                    {
                                        var element = "<a href=\"#\" title=\""+json[i].fileName+"\" class=\"link2\"><div class=\"col m3\" style=\"\"><div class=\"card indigo darken-1 hoverable\"><div class=\"indigo darken-1 right\"><form action=\"#\"><p><input type=\"checkbox\" class=\"filled-in\"  id=\"file"+i+"\"/><label for=\"file"+i+"\" class=\"white-text checks\" style=\"display:none;\"></label></p></form></div><div class=\"card-content white-text\"><span class=\"truncate card-title\">"+json[i].fileName+"</span><p>Remote File</p></div><div class=\"card-action blue\"><span class=\"white-text\">Double Click to Download</span></div></div</div></a>";
                                        $("#filesview").append(element);
                                    }
                                }
                                $(".link").contextPopup({
                                
                    items: [
                        {label:'Open',                  action:function(abc) { $(abc.currentTarget).trigger("dblclick") } },
                        {
                            label:'Share', 
                            action:function(abc) { 
                                if(selectedCount==0){
                                    var p = path.replace(/_/g,'\\')+"\\"+$(abc.currentTarget).parents(".card").find(".card-title").html();
                                    selected[p]=p;
                                    selectedCount++;
                                }
                                $("#shareLink").trigger("click");
                            } 
                        }
                        
                         ]
                });
                               $(".link2").on("dblclick",function(event){
                                   var p = path.replace(/_/g,'\\')+"\\"+ $(this).find(".card-title").html();
                                   
                                   var url = 'download';
                                   var form = $('<form action="' + url + '" method="post">' +
                                     '<input type="text" name="path" value="' + p + '" />' +
                                     '<input type="text" name="userId" value="' + LOGGED_IN_USER + '" />' +
                                     '<input type="text" name="pc_id" value="' + PC_ID+ '" />' +
                                     '</form>');
                                   $('body').append(form);
                                   form.submit();
                                   form.remove();
   //                                location='download?path='+p+'&userId='+LOGGED_IN_USER+'&pc_id='+PC_ID;
                               });
                               if(dp.getQueuedFiles().length==0)
                                   $("#upload-widget").fadeOut();
                               num++;
//                               deselectAll();
                               $("body").removeClass("loading");
                            }
                                 
                             refreshed=true;
                      }

                    });
            }
            
            function isImage(fileName)
            {
                if(fileName.match(/.jpg$/) || fileName.match(/.png$/) || fileName.match(/.gif$/) || fileName.match(/.bmp$/))
                    return true;
                else
                    return false;
            }
            function deselectAll()
            {
                    selectedCount=0;
                    selected={};
                    $("#selectedCount").html(selectedCount);
                    $("#selectedStatus").fadeOut(1000);
                    $(".card").find("input").prop("checked",false);
                    $("#downloadSelectedLink").fadeOut(1000);
                    $(".card-action").fadeIn(1000);
                    $(".checks").fadeOut(1000);
                    $("#deleteLink").fadeOut(1000);
                    $("#renameLink").fadeOut(1000);
            }
            function selectAll()
            {
                var dir = path.replace(/%/g,'\\');
                var files = $(".card").find(".card-title");
                selectedCount=0;
                selected={};
                $.each(files,function(index,value){
                    var v = dir +"\\"+$(value).html();
                    selected[v]=v;
                    selectedCount++;
                });
                $("#selectedCount").html(selectedCount);
                $("#selectedStatus").fadeIn(1000);
                $(".card").find("input").prop("checked",true);
                $("#downloadSelectedLink").fadeIn(1000);
                $(".checks").fadeIn(1000);     
            }
            function pingApp(pcId,status){
                setTimeout(function(){
                    $.ajax({      
                          type: 'POST', 
                          url: "pingClient",
                          dataType: "text",
                          data: { pc_id: pcId},
                          success: function(data) {
                              data = $.parseJSON(data);
                              if(data.connected){
                                  $(status).children("img").attr("src","resources/img/app-on.png");
                                  $(status).children("img").attr("title","Device Online");
                              }
                              else{
                                  $(status).children("img").attr("src","resources/img/app-off.png");
                                  $(status).children("img").attr("title","Device Offline");
                              }
                              pingApp(pcId,status);
                          } 
                      });
                },5000);
            }
            $(document).ready(function() {
                $(".link").contextPopup({
                    title: 'My Popup Menu',
                    items: [
                        {label:'Open',     icon:'icons/shopping-basket.png',             action:function() { alert(); alert('Ab jo mrzi kr idr '); } },
                        {label:'Another Thing', icon:'icons/receipt-text.png',                action:function() { alert('clicked 2'); } }
                         ]
                });
                $('#mythingy').contextPopup({
                    items: [
                        {label:'Refresh',     action:function() {$("#refreshLink").trigger("click"); } },
                        {label:'Back', action:function() { $("#backLink").trigger("click");  } },
                        {label:'Share',action:function() { $("#shareLink").trigger("click"); } },
                        {label:'Download this Directory',action:function() { $("#downloadDirLink").trigger("click"); } }
                         ]
                });
                
                $("#renameLink").click(function(){
                    var fpath = Object.keys(selected)[0];
                    
                    $("#fileToRename").html(fpath);
                });
                $("#renameFileBtn").click(function(){
                    var fPath = $("#fileToRename").html();
                    var newName = $("#newNameTxt").val();
                    $.ajax({
                        url: "renameData",
                        method: "POST",
                        data:{path:fPath,newName:newName,userId:LOGGED_IN_USER,pc_id:PC_ID},
                        success:function(data){
                            data = $.parseJSON(data);
                            if(data.success){
                                Materialize.toast("File Renamed.",4000);
                                $("#newNameTxt").val("");
                                $("#fileToRename").html("");
                                $("#renameModal").closeModal();
                                $("#refreshLink").trigger("click");
                            } else{
                                if(!data.loggedIn)
                                    Materialize.toast("Please Login to Continue",4000);
                                else if(!data.pcFound)
                                    Materialize.toast("Invalid PC",4000);
                                else
                                    Materialize.toast(data.errorMsg,4000);
                                $("#newNameTxt").val("");
                                $("#fileToRename").html("");
                                $("#renameModal").closeModal();
                            }
                            deselectAll();
                        },
                        failure: function(data){
                            Materialize.toast("File Cannot be renamed",4000);
                            $("#newNameTxt").val("");
                            $("#fileToRename").html("");
                            $("#renameModal").closeModal();
                            deselectAll();
                        }
                        
                    });
                });
                    var pcArray = $(".pcStatus");
                    for(var i =0;i<pcArray.length;i++){
                        var pcId = $(pcArray[i]).attr("id").split("_")[1];
//                        pingApp(pcId,pcArray[i]);
                        
                    }
                    $("#dirview").on("dblclick",".link",function(event){
                        if(selectedCount==0)
                        {
                            if(path!="")
                            {
                                path = path+"_";
                            }
                            path  = path + $(this).find(".card-title").html();
                            getChilds();
                            $("#navPaths").children().last().removeClass("active");
                            $("#navPaths").append("<a href=\"#\" class=\"breadcrumb active\" data-num=\""+num+"\" data-path=\""+path+"\">"+ $(this).find(".card-title").html()+"</a>");
                            $("#downloadDirLink").show();    
                            $("#uploadLink").show();    
                        }
                        
                 });
                $("#dirview").on("mouseenter",".link",function(event){
                     if(path!="")
                        {
                            $(this).children().find("label").fadeIn(1000);
                        }
                });
                $("#filesview").on("mouseenter",".link2",function(event){
                     if(path!="")
                        {
                            $(this).children().find("label").fadeIn(1000);
                        }
                });
                $("#dirview").on("change","input",function(event){
                     var p = path.replace(/_/g,'\\')+"\\"+$(this).parents(".card").find(".card-title").html();
                     if($(this).is(':checked'))
                     {      
                         selected[p]=p;
                         selectedCount++;
                         if(selectedCount==1)
                             $("#renameLink").fadeIn(1000);
                         else
                             $("#renameLink").fadeOut(1000);
                             
                         $(".checks").fadeIn(1000);
                         $("#selectedCount").html(selectedCount);
                         $("#selectedStatus").fadeIn(1000);
                         $("#deleteLink").fadeIn(1000);
                         $("#uploadLink").fadeOut(1000);
                         $("#downloadSelectedLink").fadeIn(1000);
                         $(".card-action").hide();
                     }
                     else
                     {
                        delete selected[p];
                        selectedCount--;
                        if(selectedCount==1)
                             $("#renameLink").fadeIn(1000);
                         else
                             $("#renameLink").fadeOut(1000);
                        $("#selectedCount").html(selectedCount);
                        if(selectedCount==0)
                        {
                            $("#selectedStatus").fadeOut(1000);
                            $("#downloadSelectedLink").fadeOut(1000);
                            $("#deleteLink").fadeOut(1000);
                            $("#uploadLink").fadeIn(1000);
                            $(".checks").fadeOut(100);
                            $(this).parents(".card").find("label").fadeIn(100);
                            $(".card-action").fadeIn(1000);
                            
                        }
                     }
                });
                
                $(".initialShareCloseBtn").click(function(){
                    deselectAll();
                });
                $("#downloadSelectedLink").on("click",function(){
                    
                    var totalItems = $(".card").length;
                    if(selectedCount===totalItems)
                    {
                        $("#downloadDirLink").trigger("click");               
                    }
                    else
                    {
                        
                        $("#loaderDiv").html("<img src=\"resources/img/mloader.gif\"  class=\"responsive-img\"/>");
                        var str = "";
                        for(var i =0;i<Object.keys(selected).length;i++){
                            str+=Object.keys(selected)[i];
                            if(i!==Object.keys(selected).length-1)
                                str+=" ## ";
                        }
                        $.ajax({
                            url: 'downloadMultiple',
                            type: 'POST',
                            
                            data: { path: str, userId: LOGGED_IN_USER,pc_id: PC_ID},
                            success: function(data, textStatus, jqXHR)
                            {
                                if(typeof data.error === 'undefined')
                                {
                                    // Success so call function to process the form
                                    if(selectedCount>0)
                                    {
                                        deselectAll();
                                    }
                                    $("#loaderDiv").html("done");
                                    $("#zipWaitModal").closeModal();
//                                    p = $("#navPaths").children().last().html()+".zip";
//                                    location='download?path='+p+'&userId=3';
                                }
                                else
                                {
                                    // Handle errors here
                                    alert(data);
                                    $("#zipWaitModal img").attr("src","resources/img/merror.jpg");
                                    $("#zipWaitModal p").html("Something Went Wrong. Please Try Again Later");
                                }
                            },
                            error: function(jqXHR, textStatus, errorThrown)
                            {
                                // Handle errors here
                                $("#zipWaitModal img").attr("src","resources/img/merror.jpg");
                                $("#zipWaitModal p").html("Something Went Wrong. Please Try Again Later");
                                // STOP LOADING SPINNER
                            }
                        });
                    }
                });
                $("#filesview").on("change","input",function(event){
                     var p = path.replace(/_/g,'\\')+"\\"+$(this).parents(".card").find(".card-title").html();
                     if($(this).is(':checked'))
                     {      
                         selected[p]=p;
                         selectedCount++;
                         if(selectedCount==1)
                             $("#renameLink").fadeIn(1000);
                         else
                             $("#renameLink").fadeOut(1000);
                         $(".checks").fadeIn(1000);
                         $("#selectedCount").html(selectedCount);
                         $("#selectedStatus").fadeIn(1000);
                         $("#deleteLink").fadeIn(1000);
                         $("#downloadSelectedLink").fadeIn(1000);
                         $(".card-action").hide();
                     }
                     else
                     {
                        delete selected[p];
                        selectedCount--;
                        if(selectedCount==1)
                             $("#renameLink").fadeIn(1000);
                         else
                             $("#renameLink").fadeOut(1000);
                        $("#selectedCount").html(selectedCount);
                        if(selectedCount==0)
                        {
                            $("#selectedStatus").fadeOut(1000);
                            $("#downloadSelectedLink").fadeOut(1000);
                            $(".checks").fadeOut(100);
                            $("#deleteLink").fadeOut(1000);
                            $(this).parents(".card").find("label").fadeIn(100);
                            $(".card-action").fadeIn(1000);
                        }
                     }
                });
                
                $("#dirview").on("mouseleave",".link",function(event){
                     if(path!="")
                        {
                            var checkbox = $(this).children().find("input");
                            if(!checkbox.is(':checked'))
                            {
                                if(selectedCount==0)
                                {
                                    $(this).children().find("label").fadeOut(1000);
                                }
                            }
                        }
                });
                
                $("#filesview").on("mouseleave",".link2",function(event){
                     if(path!="")
                        {
                            var checkbox = $(this).children().find("input");
                            if(!checkbox.is(':checked'))
                            {
                                if(selectedCount==0)
                                {
                                    $(this).children().find("label").fadeOut(1000);
                                }
                            }
                        }
                });
                
                 $("#downloadDirLink").on("click",function(){
                    $("#loaderDiv").html("<img src=\"resources/img/mloader.gif\"  class=\"responsive-img\"/>");
                    $.ajax({
                    url: 'downloadZippedDir',
                    type: 'POST',
                    dataType: "text",
                    data: { path: path.replace(/_/g,'\\'), userId: LOGGED_IN_USER,pc_id: PC_ID},
                    success: function(data, textStatus, jqXHR)
                    {
                        if(typeof data.error === 'undefined')
                        {
                            // Success so call function to process the form
                            if(selectedCount>0)
                            {
                                deselectAll();
                            }
                            $("#loaderDiv").html("done");
                            $("#zipWaitModal").closeModal();
                            p = $("#navPaths").children().last().html()+".zip";
                            var url = 'download';
                                var form = $('<form action="' + url + '" method="post">' +
                                  '<input type="text" name="path" value="' + p + '" />' +
                                  '<input type="text" name="userId" value="' + LOGGED_IN_USER + '" />' +
                                  '<input type="text" name="pc_id" value="' + PC_ID+ '" />' +
                                  '</form>');
                                $('body').append(form);
                                form.submit();
                                form.remove();
//                            location='download?path='+p+'&userId='+LOGGED_IN_USER+'&pc_id='+PC_ID;
                        }
                        else
                        {
                            // Handle errors here
                            $("#zipWaitModal img").attr("src","resources/img/merror.jpg");
                            $("#zipWaitModal p").html("Something Went Wrong. Please Try Again Later");
                        }
                    },
                    error: function(jqXHR, textStatus, errorThrown)
                    {
                        // Handle errors here
                        $("#zipWaitModal img").attr("src","resources/img/merror.jpg");
                        $("#zipWaitModal p").html("Something Went Wrong. Please Try Again Later");
                        // STOP LOADING SPINNER
                    }
                }); 
                 });
                 $("#dirview").on("contextmenu",".link",function(event){
                        event.preventDefault();
                        
                 });
                 
                 $("#refreshLink").on("click",function(){
                    num--;
                    getChilds();
                 });
                 $("#backLink").on("click",function(event){
                    
                        if(path!="")
                        {
                           var found = false;
                           for(var i = path.length-1;i>=0;i--)
                           {
                               if(path[i]=="_")
                               {
                                   path = path.substr(0,i);
                                   num=num-2;
                                   getChilds();
                                   found=true;
                                   break;
                               }
                           }
                           if(!found)
                           {
                               path="";
                               num=1;
                               getChilds();
                               $("#downloadDirLink").hide();
                               $("#uploadLink").hide();
                           }
                           $("#navPaths").children().last().remove();
                           $("#navPaths").children().last().addClass("active");
                           
                        }
                        deselectAll();
                 });
                 $('#navPaths').on('click', 'a.breadcrumb', function(e) {
                    num = $(this).data('num');
                    path = $(this).data('path');
                    getChilds();
                    var childs = $('#navPaths').children().length;
                    for(var i = num;i<childs;i++)
                    {
                        $('#navPaths').children().last().remove();
                    }
                    $('#navPaths').children().last().addClass("active");
                    if($('#navPaths').children().length==1)
                        $("#downloadDirLink").hide();
                });
                $('.modal-trigger').leanModal();
                $('#downloadDirLink.modal-trigger').leanModal({
                    dismissible: false
                });
                
                
                Dropzone.options.uploadWidget = {
                    paramName: 'file',
                    dictDefaultMessage: 'Drag a file here to upload',
                    clickable : false ,
                    init: function() {
                      dp=this;
                      this.on('success', function( file, resp ){
                        num--;
                        getChilds();
                         setTimeout(function() {
                            dp.removeFile(file);
                          },3000);
                      });
                      this.on("queuecomplete", function(file, xhr, formData) {
                        setTimeout(function() {
                            $("#upload-widget").fadeOut();
                          },3000);
                      });
                      this.on("sending", function(file, xhr, formData) {
                        var completePath = path.replace(/_/g,'\\')+"\\"+file.name;
                        formData.append("filePath",completePath);
                        formData.append("lastModified","0");
                        formData.append("pc_id",PC_ID);
                      });
                      this.on('thumbnail', function(file) {
//                          file.acceptDimensions();
                      });
                    },
                    accept: function(file, done) {
                      done();
                    }
                };

//                $("#fileview").on({
//                    ajaxStart: function() { $("body").addClass("loading");    },
//                     ajaxStop: function() { $("body").removeClass("loading"); }    
//                });
                $("#uploadLink").click(function(){
                if(path!=="")
                    $("#uploadField:hidden").trigger("click");
            }); 
            $("#uploadField:hidden").on('change', prepareUpload);

            // Grab the files and set them to our variable
            function prepareUpload(event)
            {
              filesToUpload = event.target.files;
              $("#uploadForm").trigger("submit");
            }

            $("#uploadForm").on('submit', uploadFiles);

            // Catch the form submit and upload the files
            var filesToUpload;
            function uploadFiles(event)
            {
              event.stopPropagation(); // Stop stuff happening
                event.preventDefault(); // Totally stop stuff happening
                // START A LOADING SPINNER HERE

                // Create a formdata object and add the files
                var data = new FormData();
                $.each(filesToUpload, function(key, value)
                {
                    data.append("file", value);
                });
                var completePath = path.replace(/_/g,'\\')+"\\"+$("#uploadField:hidden").val().split("\\").pop();
                data.append("filePath",completePath);
                data.append("lastModified","0");
                data.append("pc_id",PC_ID);
                $.ajax({
                    url: 'uploadWebFile',
                    type: 'POST',
                    data: data,
                    cache: false,
                    processData: false, // Don't process the files
                    contentType: false, // Set content type to false as jQuery will tell the server its a query string request
                    success: function(data, textStatus, jqXHR)
                    {
                        if(typeof data.error === 'undefined')
                        {
                            // Success so call function to process the form
                            num--;
                            getChilds();
                        }
                        else
                        {
                            // Handle errors here
                            console.log('ERRORS: ' + data.error);
                        }
                    },
                    error: function(jqXHR, textStatus, errorThrown)
                    {
                        // Handle errors here
                        console.log('ERRORS: ' + textStatus);
                        // STOP LOADING SPINNER
                    }
                });
            }  
            var inDrop=false;
            $(document).on('dragover', function(e) {
              var dt = e.originalEvent.dataTransfer;
              if (dt.types && (dt.types.indexOf ? dt.types.indexOf('Files') != -1 : dt.types.contains('Files'))) {
                  if(path!="")
                    $("#upload-widget").fadeIn();
                
              }
            });
            $("#selectAllLink").on("click",function(){
                selectAll();
            });
            $("#deselectAllLink").on("click",function(){
                deselectAll();
            });
            
            function deleteFile(target,filePath)
            {
                refreshed= false;
                    $.ajax({      
                          type: 'POST', 
                          
                          url: target,
                          dataType: "text",
                          data: { path: filePath, pc_id: PC_ID},

                          success: function(data) {
                              deletedCount++;
                              var json = jQuery.parseJSON(data);
                              if(json.loggedIn)
                              {
                                  if(json.pcFound)
                                  {
                                      if(json.success)
                                      {
                                            Materialize.toast("File: "+filePath+" Deleted.",4000);
                                          
                                      }
                                      else
                                      {
                                          Materialize.toast(json.error,4000);
                                          
                                      }
                                  }
                                  else
                                  {
                                      Materialize.toast("Invalid PC",4000);
                                      
                                  }
                              }
                              else
                              {
                                  Materialize.toast("User Not Logged in",4000);
                                  
                              }
                          }
                      });
            }
            $("#deleteAllYesBtn").on("click",function(){
                location='deletePc?userId='+LOGGED_IN_USER+'&pc_id='+PC_ID;
            });
            $("#confirmDeleteBtn").on("click",function(){
                $("#confirmDeleteModal").closeModal();
                var filePath;
                var target="deleteSingle";
                if(selectedCount==1)
                {
                    filePath = Object.keys(selected)[0];
                    deleteFile(target,filePath);
                    var interval = setInterval(function(){
                        if(deletedCount==selectedCount)
                        {
                            clearInterval(interval);
                            deselectAll();
                            num--;
                            getChilds();
                            deletedCount=0;
                        }
                    },1000);
                }
                else
                {
                    for(var i=0;i<Object.keys(selected).length;i++)
                    {
                        filePath = Object.keys(selected)[i];
                        deleteFile(target,filePath)   
                    }
                    var interval = setInterval(function(){
                        if(deletedCount==selectedCount)
                        {
                            clearInterval(interval);
                            deselectAll();
                            num--;
                            getChilds();
                            deletedCount=0;
                        }
                    },1000);
                }
            });
            $("#shareReadOnly").on("click",function(){
//                alert();
//                $("#shareModal").modal();
                $("#shareWaiting").removeClass("hide");
                $("#sharePublicLink").addClass("hide");
                $("#shareInput").val("");
                $(".shareCloseBtn").html("Cancel");
                if(selectedCount==0)
                {
                    share(path,0);
                }
                else
                {
                    var multiPath = "";
                    for(var i=0;i<Object.keys(selected).length;i++)
                    {
                        if(i==Object.keys(selected).length-1)
                            multiPath=multiPath+Object.keys(selected)[i];
                        else
                            multiPath=multiPath+Object.keys(selected)[i]+"<";
                    }
                    share(multiPath,0);
                    deselectAll();
                }
            });
            $("#shareFullAccess").on("click",function(){
//                alert();
//                $("#shareModal").modal();
                $("#shareWaiting").removeClass("hide");
                $("#sharePublicLink").addClass("hide");
                $("#shareInput").val("");
                $(".shareCloseBtn").html("Cancel");
                if(selectedCount==0)
                {
                    share(path,1);
                }
                else
                {
                    var multiPath = "";
                    for(var i=0;i<Object.keys(selected).length;i++)
                    {
                        if(i==Object.keys(selected).length-1)
                            multiPath=multiPath+Object.keys(selected)[i];
                        else
                            multiPath=multiPath+Object.keys(selected)[i]+"<";
                    }
                    share(multiPath,1);
                    deselectAll();
                }
            });
//                $('.chips-initial').material_chip({
//                  data: [{
//                    tag: 'Apple',
//                  }, {
//                    tag: 'Microsoft',
//                  }, {
//                    tag: 'Google',
//                  }],
//                });
                $('.chips-placeholder').material_chip({
                  placeholder: 'Enter Username or Email',
                  secondaryPlaceholder: '+Add Another',
                });
                $('select').material_select();
                $('#shareSearchTerm').on('input',function(e){
                    var st = $('#shareSearchTerm').val();
                    $("#shareSuggestions").html("");
                    if(st==="") return;
                    $.ajax({      
                          type: 'POST', 
                          
                          url: "getSimilarUsers",
                          
                          dataType: "text",
                          data: { pattern: st},

                          success: function(data) {
                              var json = jQuery.parseJSON(data);
                                if(json.success)
                                {
                                    if(json.hasUsers){
                                        var element ="";
                                        for(var i=0;i<json.users.length;i++){
                                            var element = "<div class=\"input-field col m12 s12\"><input type=\"checkbox\" class=\"chk_suggestion\" id=\"u"+json.users[i].id+"\"/><label for=\"u"+json.users[i].id+"\">"+json.users[i].uname+" ("+json.users[i].fullName+")</label></div>";
                                            $("#shareSuggestions").append(element);
                                            if(selectedShares[json.users[i].id]!=undefined){
                                                $("#u"+json.users[i].id).prop("checked",true);
                                            }
                                        }
                                        
                                        $(".chk_suggestion").on("click",function(){
                                            var id = $(this).attr("id")+"";
                                            id=id.substr(1);
                                            if($(this).prop("checked")){                                       
                                                selectedShares[id]=id;
                                                var uname = $("label[for='u"+id+"']").html();
                                                var html = "<span class=\"mychip white-text blue\" data-refId=\""+id+"\" id=\"cname"+id+"\">"+uname+"<a href=\"#\" class=\"red-text delChip\" id=\"c"+id+"\">&Cross;</a></span>";
                                                $("#toShareSelected").append(html);
                                            }
                                            else{
                                                delete selectedShares[id];
                                                id = "cname"+id;
                                                $("#"+id).remove();
                                            }
                                        $(".delChip").on("click",function(){
                                            var id = $(this).attr("id")+"";
                                            id=id.substr(1);
                                            delete selectedShares[id];
                                            $("#u"+id).prop("checked",false);
                                            id = "cname"+id;
                                            $("#"+id).remove();

                                        });    


                                        });    
                                    }

                                    
                                }
                                else
                                {
                                    Materialize.toast(json.error,4000);
                                }
                          }
                      });
                });
                $("#shareToAll").on("change",function(e){
                    if($("#shareToAll").prop("checked")){
                        $("#shareSearchTerm").attr("disabled","disabled");
                        
                        $("#shareSuggestions").hide();
                        $("#toShareSelected").hide();
                    }
                    else{
                        $("#shareSearchTerm").removeAttr("disabled");
                        $("#shareSuggestions").show();
                        $("#toShareSelected").show();
                    }
                });
                $("#shareFinalBtn").click(function(){
                    
                    if($("#shareAccess").val()=="1"){//Read Only Sharing
                        if($("#shareToAll").prop("checked")){
                            $("#shareModal").openModal();
                            $("#shareWaiting").removeClass("hide");
                            $("#sharePublicLink").addClass("hide");
                            $("#shareInput").val("");
                            $(".shareCloseBtn").html("Cancel");
                            if(selectedCount==0)
                            {
                                share(path,"All",0);
                            }
                            else
                            {
                                var multiPath = "";
                                for(var i=0;i<Object.keys(selected).length;i++)
                                {
                                    if(i==Object.keys(selected).length-1)
                                        multiPath=multiPath+Object.keys(selected)[i];
                                    else
                                        multiPath=multiPath+Object.keys(selected)[i]+"<";
                                }
                                share(multiPath,"All",0);
                                deselectAll();
                            }
                            $("#sharingToModal").closeModal();
                        }
                        else {
                            if(Object.keys(selectedShares).length>0){
                                var ids="";
                                for(var i=0;i<Object.keys(selectedShares).length;i++){
                                    var ids = ids+Object.keys(selectedShares)[i]+" ";
                                }
                                $("#shareModal").openModal();
                                $("#shareWaiting").removeClass("hide");
                                $("#sharePublicLink").addClass("hide");
                                $("#shareInput").val("");
                                $(".shareCloseBtn").html("Cancel");
                                if(selectedCount==0)
                                {
                                    share(path,ids,0);
                                }
                                else
                                {
                                    var multiPath = "";
                                    for(var i=0;i<Object.keys(selected).length;i++)
                                    {
                                        if(i==Object.keys(selected).length-1)
                                            multiPath=multiPath+Object.keys(selected)[i];
                                        else
                                            multiPath=multiPath+Object.keys(selected)[i]+"<";
                                    }
                                    share(multiPath,ids,0);
                                    deselectAll();
                                }
                                $("#sharingToModal").closeModal();
                            }
                        }
                    }
                    else{
                        if($("#shareToAll").prop("checked")){
                            $("#shareModal").openModal();
                            $("#shareWaiting").removeClass("hide");
                            $("#sharePublicLink").addClass("hide");
                            $("#shareInput").val("");
                            $(".shareCloseBtn").html("Cancel");
                            if(selectedCount==0)
                            {
                                share(path,"All",1);
                            }
                            else
                            {
                                var multiPath = "";
                                for(var i=0;i<Object.keys(selected).length;i++)
                                {
                                    if(i==Object.keys(selected).length-1)
                                        multiPath=multiPath+Object.keys(selected)[i];
                                    else
                                        multiPath=multiPath+Object.keys(selected)[i]+"<";
                                }
                                share(multiPath,"All",1);
                                deselectAll();
                            }
                            $("#sharingToModal").closeModal();
                        }
                        else {
                            if(Object.keys(selectedShares).length>0){
                                var ids="";
                                for(var i=0;i<Object.keys(selectedShares).length;i++){
                                    var ids = ids+Object.keys(selectedShares)[i]+" ";
                                }
                                $("#shareModal").openModal();
                                $("#shareWaiting").removeClass("hide");
                                $("#sharePublicLink").addClass("hide");
                                $("#shareInput").val("");
                                $(".shareCloseBtn").html("Cancel");
                                if(selectedCount==0)
                                {
                                    share(path,ids,1);
                                }
                                else
                                {
                                    var multiPath = "";
                                    for(var i=0;i<Object.keys(selected).length;i++)
                                    {
                                        if(i==Object.keys(selected).length-1)
                                            multiPath=multiPath+Object.keys(selected)[i];
                                        else
                                            multiPath=multiPath+Object.keys(selected)[i]+"<";
                                    }
                                    share(multiPath,ids,1);
                                    deselectAll();
                                }
                                $("#sharingToModal").closeModal();
                            }
                        }
                        
                    }           
                });
                
            
});
         