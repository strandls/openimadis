# Script to deploy iManage, solr, tileviewer, cache and worker
# This script also starts cache, Tomcat and worker after deploying
# This script should be placed in the server_bundle root along with server.properties file
#
# Structure of server_bundle:
# ---------------------------
# server_bundle - 
#     WebApps -
#         iManage
#         solr
#         tileviewer
#     ServerApps -
#         cache-deploy
#         db
#         solr_data
#         worker-deploy
#     deploy.sh
#     server.properties

echo "Started iManage server deployment ..."
echo "Reading server.properties ..."
source server.properties

#Copy iManage from WebApps to <TOMCAT>/webapps
echo "Deploying iManage webapp to Tomcat ..."
cp -r WebApps/iManage $tomcatHome/webapps/
echo "Deployed iManage webapp to Tomcat."

#Create storage directories log_storage, storage_root, movie_storage, image_cache_storage, task_log_storage, backup_storage, export_storage, zoom_storage
echo "Creating log_storage directory: $log_storage" 
mkdir -p $log_storage
echo "Creating storage_root directory: $storage_root" 
mkdir -p $storage_root
echo "Creating movie_storage directory: $movie_storage" 
mkdir -p $movie_storage
echo "Creating image_cache_storage directory: $image_cache_storage" 
mkdir -p $image_cache_storage
echo "Creating task_log_storage directory: $task_log_storage" 
mkdir -p $task_log_storage
echo "Creating backup_storage directory: $backup_storage" 
mkdir -p $backup_storage
echo "Creating export_storage directory: $export_storage" 
mkdir -p $export_storage
echo "Creating zoom_storage directory: $zoom_storage" 
mkdir -p $zoom_storage

#update the corresponding locations in <TOMCAT>/iManage/META-INF/context.xml
echo "Updating log_storage path in $tomcatHome/webapps/iManage/META-INF/context.xml"
sed -i "s%LOG_STORAGE_PATH%$log_storage%" $tomcatHome/webapps/iManage/META-INF/context.xml
 
echo "Updating storage_root path in $tomcatHome/webapps/iManage/META-INF/context.xml"
sed -i "s%STORAGE_ROOT_PATH%$storage_root%" $tomcatHome/webapps/iManage/META-INF/context.xml

echo "Updating movie_storage path in $tomcatHome/webapps/iManage/META-INF/context.xml"
sed -i "s%MOVIE_STORAGE_PATH%$movie_storage%" $tomcatHome/webapps/iManage/META-INF/context.xml

echo "Updating image_cache_storage path in $tomcatHome/webapps/iManage/META-INF/context.xml"
sed -i "s%IMAGE_CACHE_STORAGE_PATH%$image_cache_storage%" $tomcatHome/webapps/iManage/META-INF/context.xml

echo "Updating task_log_storage path in $tomcatHome/webapps/iManage/META-INF/context.xml"
sed -i "s%TASK_LOGSTORAGE_PATH%$task_log_storage%" $tomcatHome/webapps/iManage/META-INF/context.xml

echo "Updating backup_storage path in $tomcatHome/webapps/iManage/META-INF/context.xml"
sed -i "s%BACKUP_STORAGE_PATH%$backup_storage%" $tomcatHome/webapps/iManage/META-INF/context.xml

echo "Updating export_storage path in $tomcatHome/webapps/iManage/META-INF/context.xml"
sed -i "s%EXPORT_STORAGE_PATH%$export_storage%" $tomcatHome/webapps/iManage/META-INF/context.xml

echo "Updating zoom_storage path in $tomcatHome/webapps/iManage/META-INF/context.xml"
sed -i "s%ZOOM_STORAGE_PATH%$zoom_storage%" $tomcatHome/webapps/iManage/META-INF/context.xml

#Copy tileviewer from WebApps to <TOMCAT>/webapps
#echo "Deploying tileviewer webapp to Tomcat ..."
#cp -r WebApps/tileviewer $tomcatHome/webapps
#echo "Deployed tileviewer webapp to Tomcat."

#Copy solr from WebApps to <TOMCAT>/webapps
echo "Deploying solr webapp to Tomcat ..."
cp -r WebApps/solr $tomcatHome/webapps
echo "Deployed solr webapp to Tomcat."

echo "Creating $solr_data directory: $solr_data"
mkdir -p $solr_data

#Copy solr_data from ServerApps $solr_data
echo "Deploying solr_data to $solr_data ..."
cp -r ServerApps/solr_data/* $solr_data
echo "Deployed solr_data to $solr_data."


#Update solr/home path in <TOMCAT>/solr/WEB-INF/web.xml with $solr_data path
echo "Updating solr_data path in " $tomcatHome/webapps/solr/WEB-INF/web.xml
sed -i "s%SOLR_DATA_PATH%$solr_data%" $tomcatHome/webapps/solr/WEB-INF/web.xml

#Copy cache-deploy from ServerApps to $cache_deploy
echo "Deploying cache to $cache_deploy ..."
cp -r ServerApps/cache-deploy $cache_deploy
echo "Deployed cache to $cache_deploy."

#Copy worker-deploy from ServerApps to $worker_deploy
echo "Deploying worker to $worker_deploy ..."
cp -r ServerApps/worker-deploy $worker_deploy
echo "Deployed worker to $worker_deploy."

#Update worker-deploy/iworker.properties for Logging, Cache, Service, Solr, Database properties
echo "Updating iworker.properties ..."
sed -i "s%iengine.log.dir=.*$%iengine.log.dir=$log_storage%" $worker_deploy/iworker.properties

sed -i "s%iengine.storage.location=.*$%iengine.storage.location=$storage_root%" $worker_deploy/iworker.properties

sed -i "s%iengine.movie.location=.*$%iengine.movie.location=$movie_storage%" $worker_deploy/iworker.properties

sed -i "s%iengine.zoom.location=.*$%iengine.zoom.location=$zoom_storage%" $worker_deploy/iworker.properties

sed -i "s%iengine.image.cache.storage=.*$%iengine.image.cache.storage=$image_cache_storage%" $worker_deploy/iworker.properties

sed -i "s%iengine.export.location=.*$%iengine.export.location=$export_storage%" $worker_deploy/iworker.properties
echo "Updated iworker.properties"

cd -
