#!/bin/bash
echo "use image_storagedb;"
# i varies over the project number
for i in {1..14}
do
echo "CREATE TABLE IF NOT EXISTS project_"$i"_project_client_registry
                (
                project_client_id BIGINT AUTO_INCREMENT,
                clientid_or_folder_name VARCHAR(64) NOT NULL,
                parentid BIGINT,
                is_directory SMALLINT(1) NOT NULL DEFAULT 0,
                PRIMARY KEY (project_client_id),     
                UNIQUE(clientid_or_folder_name,is_directory)
                ) ENGINE = MyISAM;"
done
