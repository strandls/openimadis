#!/bin/bash
echo "use image_storagedb;"
# i varies over the project number
for i in {1..19}
do
echo "alter table project_"$i"_visual_object_registry add column rotation float default 0 NOT NULL;"
done
