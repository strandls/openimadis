#!/bin/bash
echo "use image_storagedb;"
# i varies over the project number
for i in {1..44}
do
echo           "ALTER TABLE project_"$i"_import_registry add column 
		last_modification_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00';"
done
