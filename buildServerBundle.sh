gradle --rerun-tasks -p iserver build;
gradle --rerun-tasks -p iserver generateAllWSDLs;
gradle --rerun-tasks -p iserver generateAllSkeletons;
gradle --rerun-tasks -p iserver updateSkeletons;
gradle stopTomcat;
gradle --rerun-tasks -p iserver deployContext;
gradle --rerun-tasks -p iserver deployLibs;
gradle startTomcat;
gradle --rerun-tasks -p iserver deployAllServices;
gradle --rerun-tasks -p iclient generateAllStubs;
gradle stopTomcat;
gradle --rerun-tasks -p iacquisition createAcquisitionZip;
gradle --rerun-tasks -p iclient	createClientAPIJar;
gradle --rerun-tasks -p web deployWebApp;
gradle --rerun-tasks createServerBundle;

