use imagedb;
/*adding overlay_columns column to the user_recent_projects*/
alter table user_recent_projects add overlay_columns MEDIUMBLOB NULL;