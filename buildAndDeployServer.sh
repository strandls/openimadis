gradle -p iserver build;
gradle -p iserver generateAllWSDLs;
gradle -p iserver generateAllSkeletons;
gradle -p iserver updateSkeletons;
gradle stopTomcat;
gradle -p iserver deployContext;
gradle -p iserver deployLibs;
gradle startTomcat;
gradle -p iserver deployAllServices;
gradle -p iclient generateAllStubs;
gradle stopTomcat;
gradle -p web deployWebApp;
gradle startTomcat;

