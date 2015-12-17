<%@	page
	import="com.strandgenomics.imaging.icore.Permission,
			com.strandgenomics.imaging.icore.Rank,
			com.strandgenomics.imaging.iengine.models.Project,
			com.strandgenomics.imaging.iengine.system.SysManagerFactory,
			java.util.List" %>
<%	boolean admin = request.getParameter("admin") != null;
	if (session == null || session.getAttribute("loginUsername") == null) {
		response.sendRedirect("login.html" + (admin? "?admin" : ""));
	} %>
<html>
	<head>
		<meta name="layout" content="main" />
		
		<title>Welcome to iManage</title>
		<link rel="icon" href="images/icon.png" type="image/png" />
		
		<link rel="stylesheet" type="text/css" href="css/custom.css" />
		<link rel="stylesheet" type="text/css" href="css/icons.css" />
		<link rel="stylesheet" type="text/css" href="css/jquery.miniColors.css" />
		<link rel="stylesheet" type="text/css" href="css/overlay.css" />
		<link rel="stylesheet" type="text/css" href="js/extjs/resources/css/ext-all.css" />
		<link rel="stylesheet" type="text/css" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.7.1/themes/base/jquery-ui.css" />
		
		<script type="text/javascript" src="js/extjs/bootstrap.js"></script>
		<script type="text/javascript">
			Ext.Loader.setPath({
				'Admin': '$(createLink(url:"js/admin"))',
				'Manage': '$(createLink(url:"js/app"))'
			});
		</script>
		
		<script type="text/javascript">
<%			if (session != null && session.getAttribute("loginUsername") != null) {
				String user = session.getAttribute("loginUsername").toString();
				Rank rank = SysManagerFactory.getUserManager().getUser(user).getRank();
				if (admin) {
					switch (rank) {
						case Administrator:
							// All views
							out.println("viewsToShow = ['userList', 'projectList', 'membership', 'userProjects', 'userAnnotation', 'usageLogs', 'unitList', 'association', 'logList', 'clientList', 'publisherList', 'downloadsList', 'authCodesList', 'licenseList', 'uploadsList', 'microscopeList', 'microscopeProfiles']");
							break;
						case FacilityManager:
							// ProjectList, membership
							out.println("viewsToShow = ['projectList', 'membership', 'userAnnotation', 'association', 'microscopeList', 'microscopeProfiles']");
							break;
						case TeamLeader:
						case TeamMember:
							// Only membership
							out.println("viewsToShow = ['membership', 'userAnnotation']");
							break;
					}
				} else {
					boolean canDeleteRecord = false, canUpload = false;
					switch (rank) {
						case Administrator:
						case FacilityManager:
							break;
						case TeamLeader:
						case TeamMember:
							List<Project> projects = SysManagerFactory.getProjectManager().getAllProjectsByManager(user);
							break;
					}
					
					canUpload = SysManagerFactory.getUserPermissionManager().listProjects(user, Permission.Upload).size() > 0;
					canDeleteRecord = SysManagerFactory.getUserPermissionManager().listProjects(user, Permission.Manager).size() > 0;
					
					out.println("showAcqLink = " + canUpload + ";");
					out.println("canDeleteRecord = " + canDeleteRecord + ";");
					out.println("canAddPublisher = " + canDeleteRecord + ";");
				}
			} %>
		</script>
		
		<% if (admin) { %><script type="text/javascript" src="js/admin/Application.js"></script>
		<% } else { %><script type="text/javascript" src="js/app/Application.js"></script><% } %>
		
		<script type="text/javascript" src="js/external/CTemplate.js"></script>
		<script type="text/javascript" src="js/external/Component.js"></script>
		<script type="text/javascript" src="js/external/HeaderToolTip.js"></script>
		<script type="text/javascript" src="js/external/PagingMemoryProxy.js"></script>
		<script type="text/javascript" src="js/external/PagingOptions.js"></script>
		
		<script type="text/javascript" src="js/jquery.min.js"></script>
		<script type="text/javascript" src="js/jquery.loadmask.min.js"></script>
		<script type="text/javascript" src="js/jquery.miniColors.js"></script>
		<script type="text/javascript" src="js/jquery.sizes.min.js"></script>
		<script type="text/javascript" src="js/jquery-ui.min.js"></script>
		<script type="text/javascript" src="js/raphael.js"></script>
		<script type="text/javascript" src="js/raphael.overlay.js"></script>
		<script type="text/javascript" src="js/pens.js"></script>
		<script type="text/javascript" src="js/scale.raphael.js"></script>
		<script type="text/javascript" src="js/json2.js"></script>
		<script type="text/javascript" src="js/image_handle.js"></script>
		<script type="text/javascript" src="js/underscore.js"></script>
		
		<script type="text/javascript" src="js/utils.js"></script>
	</head>
	<body>
		<div id="welcomeMessage" style="display:none"><% out.print("Welcome " + session.getAttribute("loginName")); %></div>
		<div id="userName" style="display:none"><% out.print(session.getAttribute("loginUsername")); %></div>
	</body>
</html>
