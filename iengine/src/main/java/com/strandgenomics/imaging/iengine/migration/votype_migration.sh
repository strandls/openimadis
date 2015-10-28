#!/bin/bash
echo "use image_storagedb;"
# i varies over the project number
for i in {1..19}
do
echo "alter table project_"$i"_visual_object_registry modify column vo_type enum('Line','Ellipse','Rectangle','FreeHand','Circle','Polygon','Arrow') NOT NULL;"
done
