you can use docker to compile/run openimadis.

* Compile :
  - Go to "build" directory and build image (docker build -t openimadis_build .)
  - Run container : docker run -it --rm -v PATH_ON_HOST_WHERE_YOU_CLONE_REPOSITORY:/source openimadis_build bash
  - Inside container : "cd /source", and "sh buildServerBundle.sh"
  - Take one (or more) coffee
  - Quit container, make an archive for "PATH_ON_HOST_WHERE_YOU_CLONE_REPOSITORY/openimadis/build/distributions" (tar czf take ~500M)

* Run :
  - Take official Docker tomcat:7 image, and run with folder containing build obtained previously (docker run -it -p 8080 -v PATH_ON_HOST_WITH_BUILD:/app tomcat:7 bash
  - Read https://github.com/strandls/openimadis/wiki/Deployment-QuickStart and :
    - Don't do 4 & 5 (already in image)
    - 6 = tomcatHome=/usr/local/tomcat (warning on cache_deploy & worker_deploy, only type base directory, subdir with name cache/worker will be created)
    - Adapt permission (chmod + on deploy.sh, and cache/worker scripts)
    - 8 = "catalina.sh run &"

Enjoy :)
