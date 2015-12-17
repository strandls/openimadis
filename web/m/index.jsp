<!DOCTYPE html> 
<%  
  if (session == null || session.getAttribute("loginUsername") == null)  
  {  
    response.sendRedirect("login.html");
  }  
%>

<html> 
    <head> 
    <title>iManage</title> 
    
    <meta name="viewport" content="width=device-width, initial-scale=1"> 

    <link rel="icon" href="css/images/icon.png" type="image/png" />
    <link rel="stylesheet" href="css/jquery.mobile-1.0.css" />
    <link rel="stylesheet" href="css/jquery.mobile.css" />
    <link rel="stylesheet" href="css/photoswipe.css" />
    <script type="text/javascript" src="js/klass.min.js"></script>
    <script type="text/javascript" src="js/jquery-1.6.4.min.js"></script>
    <script type="text/javascript" src="js/jquery.mobile-1.0.js"></script>
    <script type="text/javascript" src="js/ICanHaz.js"></script>
    <script type="text/javascript" src="js/underscore.js"></script>
    <script type="text/javascript" src="js/code.photoswipe.jquery-3.0.4.js"></script>

    <script type="text/javascript">
    /*
     * IMPORTANT!!!
     * REMEMBER TO ADD  rel="external"  to your anchor tags. 
     * If you don't this will mess with how jQuery Mobile works
     */
    (function(window, $, PhotoSwipe){
      $(document).ready(function(){
        var options = {
          getToolbar : function() {
            return '<div class="ps-toolbar-close" style="padding-top: 12px;">Close</div><div class="viewrecord" style="padding-top: 12px; display: table-cell;">View</div><div class="metadatabutton" style="padding-top: 12px; display: table-cell;">Metadata</div>';
          }
          //is2D : true
        };
        
        $('div.gallery-page')
          .live('pageshow', function(e, prev){
            $.mobile.showPageLoadingMsg();
            $('div.gallery-page a').click(function(e){e.preventDefault();})
            var delayed = function() {
                if( $('ul.gallery a', e.target).length <= 0 ){
                  return true;
                }
                var currentPage = $(e.target), photoSwipeInstance = $("ul.gallery a", e.target).photoSwipe(options,  currentPage.attr('id'));
                var metadataButton, viewrecordButton;

                // onShow - store a reference to our "say hi" button
                photoSwipeInstance.addEventHandler(PhotoSwipe.EventTypes.onShow, function(e){
                  metadataButton = window.document.querySelectorAll('.metadatabutton')[0];
                  viewrecordButton = window.document.querySelectorAll('.viewrecord')[0];
                });
                
                // onToolbarTap - listen out for when the toolbar is tapped
                photoSwipeInstance.addEventHandler(PhotoSwipe.EventTypes.onToolbarTap, function(e){
                  if (e.toolbarAction === PhotoSwipe.Toolbar.ToolbarAction.none){
                    var currentIndex = e.currentTarget.currentIndex;
                    var record = $("#thumbnailList").children()[currentIndex];
                    var recordid = record.getAttribute('data-recordid');
                    // Handler for view metadata button
                    if (e.tapTarget === metadataButton || Code.Util.DOM.isChildOf(e.tapTarget, metadataButton)){
                      $.getJSON('../record/getAllAnnotations?recordid='+recordid, function(resp) {
                        var mdlist = $("#metadataList");
                        mdlist.html("");
                        mdlist.attr("data-recordindex", currentIndex);
                        var user = resp["user"];
                        if (_.keys(user).length > 0) {
                            mdlist.append('<div style="margin-bottom:10px; margin-top: 5px;"><b>User Annotations</b></div>');
                            _.each(user, function(key, value) {
                              if ((key instanceof Object) || (value instanceof Object))
                                return;
                              var metadataItem = ich.metadataItem({key:key, value:value});
                              mdlist.append(metadataItem);
                            });
                            mdlist.append('<br/>');
                            mdlist.append('<br/>');
                            mdlist.append('<br/>');
                        }
                        mdlist.append('<div style="margin-bottom:10px; margin-top: 5px;"><b>Image Annotations</b></div>');
                        var system = resp["system"];
                        _.each(system, function(key, value) {
                          if ((key instanceof Object) || (value instanceof Object))
                            return;
                          var metadataItem = ich.metadataItem({key:key, value:value});
                          mdlist.append(metadataItem);
                        });  
                        PhotoSwipe.getInstance('thumbnails').hide();
                        $.mobile.changePage("#metadata");
                      });
                    }
                    // Handler for view record button
                    if (e.tapTarget === viewrecordButton || Code.Util.DOM.isChildOf(e.tapTarget, viewrecordButton)){
                      $.getJSON('../record/getSiteList?recordid='+recordid, function(resp) {
                        var sitelist = $("#siteList");
                        var oldList = sitelist.html();
                        sitelist.html("");
                        sitelist.attr("data-recordindex", currentIndex);
                        _.each(resp, function(item) {
                          var siteItem = ich.siteItem(item);
                          sitelist.append(siteItem);
                        });
                        if (oldList !== "")
                            sitelist.listview("refresh");
                        PhotoSwipe.getInstance('thumbnails').hide();
                        $.mobile.changePage("#siteListPage");
                      });
                    }
                  }
                });
                
                // onBeforeHide - clean up
                photoSwipeInstance.addEventHandler(PhotoSwipe.EventTypes.onBeforeHide, function(e){
                  metadataButton = null;
                  viewrecordButton = null;
                }); 
                
                // If the transition is from metadata page, choose appropriate record automatically
                var prevPageID = $(prev.prevPage).attr('id');
                if (prevPageID === "metadata") {
                  photoSwipeInstance.show($("#metadataList").attr('data-recordindex')-0);
                }
                $.mobile.hidePageLoadingMsg();
                return true;
            };
            setTimeout(delayed, 2000);
          })
          .live('pagehide', function(e){
            var currentPage = $(e.target), photoSwipeInstance = PhotoSwipe.getInstance(currentPage.attr('id'));
            if (typeof photoSwipeInstance != "undefined" && photoSwipeInstance != null) {
              PhotoSwipe.detatch(photoSwipeInstance);
            }
            return true;
          });
        });
    }(window, window.jQuery, window.Code.PhotoSwipe));

  </script>

</head> 
<body> 

<!-- Landing page. Just holds the welcome message -->
<div data-role="page" id="landing">

  <div data-role="header">
    <h1>iManage</h1>
  </div><!-- /header -->

  <div data-role="content">
    <h2 style='text-align : center;'>Welcome to iManage Mobile</h2>
    <a href="" id="browseProjects" data-role="button">Browse Projects</a>
  </div><!-- /content -->

  <div data-role="footer">
    <h4>&copy; Strand LS</h4>
  </div><!-- /footer -->
</div><!-- /page -->

<!-- Projects div. Holds a list of projects. Each project has a thumbnail
along with some basic information about the project -->
<div data-role="page" id="projects">

  <div data-role="header">
    <h1>Projects</h1>
  </div><!-- /header -->

  <!-- This div is populated on the fly using the json from ajax call -->
  <div data-role="content">
    <ul data-role="listview" id="projectList"></ul>
  </div><!-- /content -->

  <div data-role="footer">
    <h4>&copy; Strand LS</h4>
  </div><!-- /footer -->
</div><!-- /page -->

<!-- Gallery of thumbnails. Top level view where all the thumbnails of a project
are shown -->
<div data-role="page" data-add-back-btn="true" id="thumbnails" class="gallery-page">

  <div data-role="header">
    <h1>Records</h1>
  </div><!-- /header -->

  <!-- This div is populated on the fly using the json from ajax call -->
  <div data-role="content">
    <ul class="gallery" id="thumbnailList"></ul>
  </div><!-- /content -->

  <div data-role="footer">
    <h4>&copy; Strand LS</h4>
  </div><!-- /footer -->
</div><!-- /page -->

<!-- Meta data page. Shows a list of key value pairs -->
<div data-role="page" data-add-back-btn="true" id="metadata">

  <div data-role="header">
    <h1>Image Metadata</h1>
  </div><!-- /header -->

  <div data-role="content">
    <div class="ui-grid-b" id="metadataList"></div>
  </div><!-- /content -->

  <div data-role="footer">
    <h4>&copy; Strand LS</h4>
  </div><!-- /footer -->
</div><!-- /page -->

<!-- Page which lists the sites of a record -->
<div data-role="page" data-add-back-btn="true" id="siteListPage">

  <div data-role="header">
    <h1>Choose Site</h1>
  </div><!-- /header -->

  <div data-role="content">
    <div data-role="listview" id="siteList"></div>
  </div><!-- /content -->

  <div data-role="footer">
    <h4>&copy; Strand LS</h4>
  </div><!-- /footer -->
</div><!-- /page -->

<script>

/**
 * Fetch all the projects and populate the project page
 */
$('#browseProjects').click(function(){
  $.mobile.showPageLoadingMsg();
  $.getJSON('../project/recentList', function(resp) {
    var items = resp.items;
    var thumbnails = resp.thumbnails;
    var projectList = $("#projectList");
    var oldList = projectList.html();
    projectList.html("");
    for (var i=0; i<items.length; ++i) {
      var nextData = items[i];
      nextData["thumbnail"] = thumbnails[i];
      var project = ich.project(items[i]);
      projectList.append(project);
      if (oldList !== "")
        projectList.listview("refresh");
    }
    $.mobile.hidePageLoadingMsg();
    $.mobile.changePage("#projects");
  });
});

/**
 * Fill up the thumbnails view based on the project chosen
 * from the project list
 */
$('#projectList').delegate('li', 'click', function(e) {
  $.mobile.showPageLoadingMsg();
  var projectName = this.getAttribute('data-name');
  $.getJSON('../project/getThumbnailsForProject?projectName='+projectName, function(resp) {
    var thumbnailList = $("#thumbnailList");
    var oldList = thumbnailList.html();
    thumbnailList.html("");
    _.each(resp, function(item) {
      var nextThumbnail = ich.thumbnail(item);
      thumbnailList.append(nextThumbnail);
    });
  });
  // Slightly tricky. loadPage returns a promise of finishing the loading.
  // To perform something after the loading is complete, use the done hook on the promise
  var promise = $.mobile.loadPage("#thumbnails");
  promise.done(function() {
    $.mobile.hidePageLoadingMsg();
    $.mobile.changePage("#thumbnails");
    /*$.mobile.showPageLoadingMsg();
    var f = function() {
        $.mobile.hidePageLoadingMsg();
    }
    setTimeout(f, 3000);*/
  });
});

/**
 * Launch slices and frames viewer for the chosen site
 */
$('#siteList').delegate('li', 'click', function(e) {
  $.mobile.showPageLoadingMsg();
  var recordid = this.getAttribute('data-recordid');
  var siteNumber = this.getAttribute('data-siteNumber');
  var sliceCount = parseInt(this.getAttribute('data-sliceCount'), 10);
  var frameCount = parseInt(this.getAttribute('data-frameCount'), 10);
  var images = new Array();
  var si, fi;
  var baseURL = "../project/getDefaultImage?recordid="+recordid+"&siteNumber="+siteNumber+"&height=512";
  for (fi=0; fi < frameCount; ++fi) {
    var nextFrames = new Array();
    for (si=0; si < sliceCount; ++si) {
        nextFrames.push({url : baseURL+"&frameNumber="+fi+"&sliceNumber="+si, caption : 'Slice: '+si+'/'+(sliceCount-1)+' Frame: '+fi+'/'+(frameCount-1)});
    }
    images.push(nextFrames);
  }

  var options = {
    getToolbar : function() {
      return '<div class="ps-toolbar-close" style="padding-top: 12px;">Close</div><div class="toggleoverlay" style="padding-top: 12px; display: table-cell;">Show Overlay</div>';
    },
    is2D : true,
    getImageSource: function(obj){
        return obj.url;
    },
    getImageCaption: function(obj){
        return obj.caption;
    }
  };
        
  var photoSwipeInstance = Code.PhotoSwipe.attach(images, options);
  photoSwipeInstance.show([0,0]);
  var toggleButton;
  photoSwipeInstance.addEventHandler(Code.PhotoSwipe.EventTypes.onShow, function(e){
    toggleButton = window.document.querySelectorAll('.toggleoverlay')[0];
  });

  photoSwipeInstance.addEventHandler(Code.PhotoSwipe.EventTypes.onToolbarTap, function(e){
    if (e.toolbarAction === Code.PhotoSwipe.Toolbar.ToolbarAction.none){
      if (e.tapTarget === toggleButton || Code.Util.DOM.isChildOf(e.tapTarget, toggleButton)){
        var images = new Array();
        _.each(photoSwipeInstance.cache.images, function(item) {
            _.each(item, function(image) {
                images.push(image);
            });
        });
        var button = $(toggleButton);
        var oldHtml = button.html();
        if (oldHtml === "Show Overlay") {
            _.each(images, function(item) {
                item.appendParameter("overlay", "true");
            });
            button.html("Hide Overlay");
        } else {
            _.each(images, function(item) {
                item.removeParameter("overlay");
            });
            button.html("Show Overlay");
        }
      }
    }
  });

  $.mobile.hidePageLoadingMsg();
});

</script>

<!-- Template for a project -->
<script id="project" type="text/html">
  <li data-name="{{name}}"><a href="#">
    <img src="{{thumbnail}}" />
    <h3>{{name}}</h3>
    <p>{{notes}}</p>
    <p>Record Count: {{noOfRecords}}</p>
    <span class="ui-li-count">{{noOfRecords}}</span>
  </a></li>
</script>

<!-- Template for a thumbnail-->
<script id="thumbnail" type="text/html">
  <li data-recordid="{{id}}">
    <a href="../project/getDefaultImage?recordid={{id}}&height=512" rel="external">
      <img src="{{imagesource}}" alt="Record {{id}}" />
    </a>
  </li>
</script>

<!-- Template for a metadata entry-->
<script id="metadataItem" type="text/html">
  <div class="ui-block-a">{{ value }}</div>
  <div class="ui-block-b">{{ key }}</div>
</script>

<!-- Template for a site -->
<script id="siteItem" type="text/html">
  <li data-name="{{name}}" data-siteNumber="{{siteNumber}}" data-recordid="{{recordid}}" data-sliceCount="{{sliceCount}}" data-frameCount="{{frameCount}}"><a href="#">
    <img src="{{thumbnail}}" />
    <h3>{{name}}</h3>
  </a></li>
</script>

</body>
</html>
