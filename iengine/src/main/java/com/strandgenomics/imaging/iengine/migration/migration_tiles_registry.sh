#!/bin/bash
echo "use imagedb;"
# i varies over the project number
echo "CREATE TABLE IF NOT EXISTS tiles_registry (guid bigint(20) NOT NULL,zoom_reverse_level int(11) NOT NULL,isReady tinyint(1) NOT NULL DEFAULT '0',storage_path varchar(1024) NOT NULL,PRIMARY KEY (guid,zoom_reverse_level)) ENGINE=MyISAM;"
