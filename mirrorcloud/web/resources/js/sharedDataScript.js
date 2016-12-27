            var LOGGED_IN_USER = parseInt($("#magnify").html());
            var PC_ID = $("#pcd").data("pcid");
            var sharedPath = $("#shPth").val();
            var json = null;
            var num =parseInt($("#shNm").val());
            var link =$("#shareLink").val();
            var path = sharedPath;
            var dp;            
            var selected = {};
            var selectedCount =0;
            var refreshed = true;
            var deletedCount = 0;
            var sharedPaths = $("#shPths").val().split("<");
            var sharedArr = Array();
            for(var a = 0;a<sharedPaths.length;a++)
            {
                var temp = sharedPaths[a].split("\\");
                sharedArr[a]=temp[temp.length-1];
            }
            function getChilds()
            {
                $("body").addClass("loading");
                $.ajax({      
                          type: 'POST', 
                          url: "viewSharedDataAjax",
                          dataType: "text",
                          data: { path: path, num: num ,pc_id: PC_ID},

                          success: function(data) {
                              
                             json = $.parseJSON(data);
                                $("#dirview").empty();
                                $("#filesview").empty();
                                for(var i =1;$($(json)[0][i]).length>0;i++) { 
                                    if(path==sharedPath && path!="")
                                    {
                                        if(jQuery.inArray(json[i].fileName,sharedArr)!=-1)
                                        {
                                            if(json[i].isDirty)
                                            {
                                                var element = "<a href=\"#\"class=\"link\"><div class=\"col m3\" style=\"\"><div class=\"card blue hoverable\"><div class=\"blue right\"><form action=\"#\"><p><input type=\"checkbox\" class=\"filled-in\"  id=\"file"+i+"\"/><label for=\"file"+i+"\" class=\"white-text checks\" style=\"display:none;\"></label></p></form></div><div class=\"card-content white-text\"><span class=\"card-title\">"+json[i].fileName+"</span><p>Remote Directory</p></div><div class=\"card-action indigo darken-1\"><span class=\"white-text \">Double Click to Browse</span></div></div</div></a>";
                                                $("#dirview").append(element);
                                            }
                                            else
                                            {
                                                var element = "<a href=\"#\" title=\""+json[i].fileName+"\" class=\"link2\"><div class=\"col m3\" style=\"\"><div class=\"card indigo darken-1 hoverable\"><div class=\"indigo darken-1 right\"><form action=\"#\"><p><input type=\"checkbox\" class=\"filled-in\"  id=\"file"+i+"\"/><label for=\"file"+i+"\" class=\"white-text checks\" style=\"display:none;\"></label></p></form></div><div class=\"card-content white-text\"><span class=\"truncate card-title\">"+json[i].fileName+"</span><p>Remote File</p></div><div class=\"card-action blue\"><span class=\"white-text\">Double Click to Download</span></div></div</div></a>";
                                                $("#filesview").append(element);
                                            }
                                        }
                                    }
                                    else
                                    {
                                        if(sharedArr[0]!="" && path=="")
                                        {
                                            if(jQuery.inArray(json[i].fileName,sharedArr)!=-1)
                                            {
                                                if(json[i].isDirty)
                                                {
                                                    var element = "<a href=\"#\"class=\"link\"><div class=\"col m3\" style=\"\"><div class=\"card blue hoverable\"><div class=\"blue right\"><form action=\"#\"><p><input type=\"checkbox\" class=\"filled-in\"  id=\"file"+i+"\"/><label for=\"file"+i+"\" class=\"white-text checks\" style=\"display:none;\"></label></p></form></div><div class=\"card-content white-text\"><span class=\"card-title\">"+json[i].fileName+"</span><p>Remote Directory</p></div><div class=\"card-action indigo darken-1\"><span class=\"white-text \">Double Click to Browse</span></div></div</div></a>";
                                                    $("#dirview").append(element);
                                                }
                                                else
                                                {
                                                    var element = "<a href=\"#\" title=\""+json[i].fileName+"\" class=\"link2\"><div class=\"col m3\" style=\"\"><div class=\"card indigo darken-1 hoverable\"><div class=\"indigo darken-1 right\"><form action=\"#\"><p><input type=\"checkbox\" class=\"filled-in\"  id=\"file"+i+"\"/><label for=\"file"+i+"\" class=\"white-text checks\" style=\"display:none;\"></label></p></form></div><div class=\"card-content white-text\"><span class=\"truncate card-title\">"+json[i].fileName+"</span><p>Remote File</p></div><div class=\"card-action blue\"><span class=\"white-text\">Double Click to Download</span></div></div</div></a>";
                                                    $("#filesview").append(element);
                                                }
                                            }
                                        }
                                        else{
                                            if(json[i].isDirty)
                                            {
                                                var element = "<a href=\"#\"class=\"link\"><div class=\"col m3\" style=\"\"><div class=\"card blue hoverable\"><div class=\"blue right\"><form action=\"#\"><p><input type=\"checkbox\" class=\"filled-in\"  id=\"file"+i+"\"/><label for=\"file"+i+"\" class=\"white-text checks\" style=\"display:none;\"></label></p></form></div><div class=\"card-content white-text\"><span class=\"card-title\">"+json[i].fileName+"</span><p>Remote Directory</p></div><div class=\"card-action indigo darken-1\"><span class=\"white-text \">Double Click to Browse</span></div></div</div></a>";
                                                $("#dirview").append(element);
                                            }
                                            else
                                            {
                                                var element = "<a href=\"#\" title=\""+json[i].fileName+"\" class=\"link2\"><div class=\"col m3\" style=\"\"><div class=\"card indigo darken-1 hoverable\"><div class=\"indigo darken-1 right\"><form action=\"#\"><p><input type=\"checkbox\" class=\"filled-in\"  id=\"file"+i+"\"/><label for=\"file"+i+"\" class=\"white-text checks\" style=\"display:none;\"></label></p></form></div><div class=\"card-content white-text\"><span class=\"truncate card-title\">"+json[i].fileName+"</span><p>Remote File</p></div><div class=\"card-action blue\"><span class=\"white-text\">Double Click to Download</span></div></div</div></a>";
                                                $("#filesview").append(element);
                                            }
                                        }
                                        
                                        
                                    }
                                    $(".link2").on("dblclick",function(event){

                                   var p = path.replace(/_/g,'\\')+"\\"+ $(this).find(".card-title").html();
                                   
                                   var url = 'downloadShared';
                                   var form = $('<form action="' + url + '" method="post">' +
                                     '<input type="text" name="path" value="' + p + '" />' +
                                     '<input type="text" name="pc_id" value="' + PC_ID+ '" />' +
                                     '<input type="text" name="sharedLink" value="' + link+ '" />' +
                                     '</form>');
                                   $('body').append(form);
                                   form.submit();
                                   form.remove();
   //                                location='download?path='+p+'&userId='+LOGGED_IN_USER+'&pc_id='+PC_ID;
                               });
                                }
                               
                               if(dp.getQueuedFiles().length==0)
                                   $("#upload-widget").fadeOut();
                               num++;
//                               deselectAll();
                               $("body").removeClass("loading");
                            
                                 
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
            
            $(document).ready(function() {
                    
//                    getChilds();
//                    num--;
                $(".link2").on("dblclick",function(event){

                   var p = path.replace(/_/g,'\\')+"\\"+ $(this).find(".card-title").html();

                   var url = 'downloadShared';
                   var form = $('<form action="' + url + '" method="post">' +
                     '<input type="text" name="path" value="' + p + '" />' +
                     '<input type="text" name="pc_id" value="' + PC_ID+ '" />' +
                     '<input type="text" name="sharedLink" value="' + link+ '" />' +
                     '</form>');
                   $('body').append(form);
                   form.submit();
                   form.remove();
//                                location='download?path='+p+'&userId='+LOGGED_IN_USER+'&pc_id='+PC_ID;
               });
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
                            $(".checks").fadeOut(100);
                            $(this).parents(".card").find("label").fadeIn(100);
                            $(".card-action").fadeIn(1000);
                            
                        }
                     }
                });
                $("#downloadSelectedLink").on("click",function(){
                    var totalItems = $(".card").length;
                    if(selectedCount==totalItems)
                    {
                        $("#downloadDirLink").trigger("click");               
                    }
                    else
                    {
                        $("#loaderDiv").html("<img src=\"resources/img/mloader.gif\"  class=\"responsive-img\"/>");
                        $.ajax({
                            url: 'downloadMultiple',
                            type: 'GET',
                            datatype:json,
                            data: { path: selected, userId: LOGGED_IN_USER,pc_id: PC_ID},
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
                    url: 'downloadSharedZippedDir',
                    type: 'POST',
                    dataType: "text",
                    data: { path: path.replace(/_/g,'\\'), userId: LOGGED_IN_USER,pc_id: PC_ID,sharedLink:link},
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
                 
                 
                 $("#backLink").on("click",function(event){
                    
                        if(path!==sharedPath)
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
                               path=sharedPath;
                               num=1;
                               getChilds();
                               $("#downloadDirLink").hide();
                           }
                           $("#navPaths").children().last().remove();
                           $("#navPaths").children().last().addClass("active");
                           if($('#navPaths').children().length==1)
                                $("#downloadDirLink").hide();
                        }
                 });
                 $('#navPaths').on('click', 'a.breadcrumb', function(e) {
                    num = $(this).data('num');
                    path = $(this).data('path');
                    getChilds();
                    var childs = $('#navPaths').children().length;
                    if(path=="")
                    {
                        for(var i = num;i<childs;i++)
                        {
                            $('#navPaths').children().last().remove();
                        }
                    }
                    else{
                        for(var i = num-1;i<childs;i++)
                        {
                            $('#navPaths').children().last().remove();
                        }
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
                        var completePath = path.replace(/%/g,'\\')+"\\"+file.name;
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
                          data: { path: filePath, pc_id: PC_ID,link:link},

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
                                  Materialize.toast("Invalid User",4000);
                                  
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
                var target="deleteSingleShared";
                if(selectedCount==1)
                {
                    filePath = Object.keys(selected)[0];
                    deleteFile(target,filePath);
                    var interval = setInterval(function(){
                        if(deletedCount==selectedCount)
                        {
                            clearInterval(interval);
                            deselectAll();
//                            num--;
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
            $("#refreshLink").on("click",function(){
                    num--;
                    getChilds();
                 });
            
});
         