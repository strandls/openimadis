#!/bin/bash
echo "use image_storagedb;"
# i varies over the project number
for i in {1..19}
do
echo "alter table project_"$i"_visual_overlays modify column image_height MEDIUMINT NOT NULL;"
echo "alter table project_"$i"_visual_overlays modify column image_width MEDIUMINT NOT NULL;"
done
