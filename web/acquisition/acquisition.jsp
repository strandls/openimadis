<%@	page contentType="application/x-java-jnlp-file"%>
<%@	page import="java.io.File,
			java.io.FileInputStream,
			java.io.InputStream,
			java.util.Properties" %>
<%	// get list of all files in lib dir
	String catalina = System.getProperty("catalina.base");
	catalina = (new File(catalina + "/wtpwebapps")).list()==null ? catalina + "/webapps" : catalina + "/wtpwebapps";
	String appName = "/" + request.getRequestURI().split("/")[1];
	String acqRoot = appName + "/" + request.getRequestURI().split("/")[2];
	String libDir = acqRoot + "/lib";
	String[] fileList = new File(catalina + libDir).list();
	String serverScheme = request.getScheme();
	String serverIP = request.getServerName();
	String serverPort = ((Integer) request.getServerPort()).toString();
	String codebase = serverScheme + "://" + serverIP + ":" + serverPort + libDir;
	String version = request.getParameter("version");
	String j3dLib = acqRoot + "/j3d/1.5.2"; %>
<?xml version="1.0" encoding="UTF-8"?>
<jnlp spec="1.0+" codebase="<%=codebase%>" version="<%=version%>">
	<information>
		<title>iManage Acquisition</title>
		<vendor>Strand Life Sciences</vendor>
		<description>Index and upload microscopy images from any desktop to the iManage server.</description>
		<icon kind="shortcut" href="<%=acqRoot%>/images/largeIcon.png" width="32" height="32" />
		<icon kind="shortcut" href="<%=acqRoot%>/images/smallicon.png" width="16" height="16" />
	</information>
	<resources>
		<!-- Application Resources -->
		<j2se version="1.6+" href="http://java.sun.com/products/autodl/j2se" java-vm-args="-client -Xmx1024m" />
		<property name="jnlp.versionEnabled" value="true" />
		<property name="iManageServerIP" value="<%=serverIP%>" />
		<property name="iManageServerPort" value="<%=serverPort%>" />
<%	for(String jarFile : fileList) if(jarFile.endsWith(".jar")) { %>
		<jar href="<%=jarFile%>" download="lazy" main="<%=jarFile.contains("acquisition")%>" />
<%	} %>
	</resources>
	<component-desc />
	<update check="always" policy="always" />
	<security>
		<all-permissions />
	</security> 
	<resources os="Windows" arch="x86">
		<nativelib href="<%=j3dLib%>/windows-i586/j3dcore-ogl-chk_dll.jar" download="eager" />
		<nativelib href="<%=j3dLib%>/windows-i586/j3dcore-ogl_dll.jar" download="eager" />
		<nativelib href="<%=j3dLib%>/windows-i586/j3dcore-d3d_dll.jar" download="eager" />
	</resources>
	<resources os="Windows" arch="amd64">
		<nativelib href="<%=j3dLib%>/windows-amd64/j3dcore-ogl_dll.jar" download="eager" />
	</resources>
	<resources os="Linux" arch="i386">
		<nativelib href="<%=j3dLib%>/linux-i586/lib_j3dcore-ogl_so.jar" download="eager" />
	</resources>
	<resources os="Linux" arch="x86">
		<nativelib href="<%=j3dLib%>/linux-i586/lib_j3dcore-ogl_so.jar" download="eager" />
	</resources>
	<resources os="Linux" arch="amd64">
		<nativelib href="<%=j3dLib%>/linux-amd64/lib_j3dcore-ogl_so.jar" download="eager" />
	</resources>
	<resources os="Linux" arch="x86_64">
		<nativelib href="<%=j3dLib%>/linux-amd64/lib_j3dcore-ogl_so.jar" download="eager"/>
	</resources>
	<resources os="SunOS" arch="sparc">
		<nativelib href="<%=j3dLib%>/solaris-sparc/lib_j3dcore-ogl_so.jar" download="eager"/>
	</resources>
	<resources os="SunOS" arch="sparcv9">
		<nativelib href="<%=j3dLib%>/solaris-sparc-v9/lib_j3dcore-ogl_so.jar" download="eager"/>
	</resources>
	<resources os="SunOS" arch="x86">
		<nativelib href="<%=j3dLib%>/solaris-x86/lib_j3dcore-ogl_so.jar" download="eager"/>
	</resources>
	<resources os="SunOS" arch="amd64">
		<nativelib href="<%=j3dLib%>/solaris-x86-amd64/lib_j3dcore-ogl_so.jar" download="eager" />
	</resources>
	<resources os="SunOS" arch="x86_64">
		<nativelib href="<%=j3dLib%>/solaris-x86-amd64/lib_j3dcore-ogl_so.jar" download="eager" />
	</resources>
	<resources os="Mac OS X" arch="ppc">
		<property name="j3d.rend" value="jogl" />
		<jar href="<%=j3dLib%>/mac/jogl.jar" />
		<jar href="<%=j3dLib%>/mac/gluegen-rt.jar" /> 
		<nativelib href="<%=j3dLib%>/mac/jogl-natives-macosx-ppc.jar" />
		<nativelib href="<%=j3dLib%>/mac/gluegen-rt-natives-macosx-ppc.jar" />
	</resources>
	<resources os="Mac OS X" arch="i386">
		<property name="j3d.rend" value="jogl" />
		<jar href="<%=j3dLib%>/mac/jogl.jar" />
		<jar href="<%=j3dLib%>/mac/gluegen-rt.jar" /> 
		<nativelib href="<%=j3dLib%>/mac/jogl-natives-macosx-universal.jar" />
		<nativelib href="<%=j3dLib%>/mac/gluegen-rt-natives-macosx-universal.jar" />
	</resources>
	<resources os="Mac OS X" arch="x86_64">
		<property name="j3d.rend" value="jogl" />
		<jar href="<%=j3dLib%>/mac/jogl.jar" />
		<jar href="<%=j3dLib%>/mac/gluegen-rt.jar" />
		<nativelib href="<%=j3dLib%>/mac/jogl-natives-macosx-universal.jar" />
		<nativelib href="<%=j3dLib%>/mac/gluegen-rt-natives-macosx-universal.jar" />
	</resources>
	<application-desc name="Acquisition Client" main-class="com.strandgenomics.imaging.acquisition.AcquisitionUI" width="300" height="300">
		<argument><%out.print(request.getParameter("authcode"));%></argument>
		<argument><%out.print(serverIP);%></argument>
		<argument><%out.print(serverPort);%></argument>
		<argument><%out.print(version);%></argument>
		<argument><%out.print(serverScheme);%></argument>
	</application-desc>
	<update check="always" policy="always" />
</jnlp>
