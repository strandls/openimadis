<%@	page
	import="com.strandgenomics.imaging.icore.Permission,
			com.strandgenomics.imaging.icore.Rank,
			com.strandgenomics.imaging.iengine.models.Project,
			com.strandgenomics.imaging.iengine.system.SysManagerFactory,
			java.util.List" %>
<%	if (session == null || session.getAttribute("loginUsername") == null) {
		response.sendRedirect("login.html");
	} %>
<html>
	<head>
		<meta name="layout" content="main" />
		
		<title>Welcome to iManage</title>
		<link rel="icon" href="images/icon.png" type="image/png" />
		
		<link rel="stylesheet" type="text/css" href="css/custom.css">
		<link rel="stylesheet" type="text/css" href="css/icons.css">
		<link rel="stylesheet" type="text/css" href="js/extjs/resources/new-all.css">
		
		<script src="js/extjs/ext-all-debug.js"></script>
		
		<script type="text/javascript">
<%			boolean showAdmin = false, canDeleteRecord = false, canUpload = false;
			if (session != null && session.getAttribute("loginUsername") != null) {
				String user = session.getAttribute("loginUsername").toString();
				Rank rank = SysManagerFactory.getUserManager().getUser(user).getRank();
				switch (rank) {
					case Administrator:
						showAdmin = true;
						// All views in administrator
						out.println("viewsToShow = ['userList', 'projectList', 'membership', 'userProjects', 'userAnnotation', 'usageLogs', 'unitList', 'association', 'logList', 'clientList', 'publisherList', 'downloadsList', 'authCodesList', 'licenseList', 'uploadsList', 'microscopeList', 'microscopeProfiles','cacheStatus','workerStatus']");
						break;
					case FacilityManager:
						showAdmin = true;
						// ProjectList, membership in administrator
						out.println("viewsToShow = ['projectList', 'membership', 'userAnnotation', 'association', 'microscopeList', 'microscopeProfiles']");
						break;
					case TeamLeader:
					case TeamMember:
						List<Project> projects = SysManagerFactory.getProjectManager().getAllProjectsByManager(user);
						if (projects != null && projects.size() > 0)
							showAdmin = true;
						// Only membership view in administrator
						out.println("viewsToShow = ['membership', 'userAnnotation']");
						break;
				}
				
				canUpload = SysManagerFactory.getUserPermissionManager().listProjects(user, Permission.Upload).size() > 0;
				canDeleteRecord = SysManagerFactory.getUserPermissionManager().listProjects(user, Permission.Manager).size() > 0;
				
				out.println("showAcqLink = " + canUpload + ";");
				out.println("showAdminLink = " + showAdmin + ";");
				out.println("canDeleteRecord = " + canDeleteRecord + ";");
				out.println("canAddPublisher = " + canDeleteRecord + ";");
			} %>
		</script>
		
		<script src="js/app/app.js"></script>
		
		<script type="text/javascript" src="js/external/CTemplate1.1.js"></script>
		<script type="text/javascript" src="js/external/Component1.1.js"></script>
		
		<script type="text/javascript" src="js/jquery.min.js"></script>
		<script type="text/javascript" src="js/jquery.loadmask.min.js"></script>
		<script type="text/javascript" src="js/jquery.miniColors.js"></script>
		<script type="text/javascript" src="js/jquery.sizes.min.js"></script>
		<script type="text/javascript" src="js/jquery-ui.min.js"></script>
		<script type="text/javascript" src="js/raphael.js"></script>
		<script type="text/javascript" src="js/raphael.overlay.js"></script>
		<script type="text/javascript" src="js/pens.js"></script>
		<script type="text/javascript" src="js/scale.raphael.js"></script>
		<script type="text/javascript" src="js/underscore.js"></script>
		<script type="text/javascript" src="js/kinetic-v4.6.0.min.js"></script>
		<script type="text/javascript" src="js/kinetic.draw.js"></script>
		<script type="text/javascript" src="js/utils.js"></script>
	</head>
	<body>
		<div id="welcomeMessage" style="display:none"><% out.print(session.getAttribute("loginName")); %></div>
		<div id="userName" style="display:none"><% out.print(session.getAttribute("loginUsername")); %></div>
		<div id="loadingMessage" style="position: absolute; width: 200px; height: 50px; top: 50%; left: 50%; margin-left: -50px; margin-top: -25px; font-size: 20px">
			loading...
		</div>
	</body>
</html>
