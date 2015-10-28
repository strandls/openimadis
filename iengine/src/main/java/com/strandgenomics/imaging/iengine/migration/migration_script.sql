/**
msql script to change record_registry table, thumbnail table and project_membership table
*/

use imagedb;

/* column job_status modified to have DUPLICATE enum*/
alter table ticket_registry modify column job_status  enum('WAITING','EXPIRED','UPLOADING','QUEUED','EXECUTING','SUCCESS','FAILURE','DUPLICATE');

/*change record_registry image width and height to INTEGER from smallint*/
alter table record_registry modify column image_width INTEGER UNSIGNED NOT NULL;
alter table record_registry modify column image_height INTEGER UNSIGNED NOT NULL;

/* removes multikey index (project_id, guid) and creates separate indices for project_id and guid*/
alter table record_registry drop index project_id;
alter table record_registry add index (guid);
alter table record_registry add index (project_id);

/*maintains output records as first class information*/
alter table archived_task_registry add column output_guids MEDIUMBLOB DEFAULT NULL;

/*legend location */
alter table user_legends add column location ENUM ('TOPLEFT','BOTTOMRIGHT','TOPRIGHT','BOTTOMLEFT') DEFAULT 'TOPLEFT';

/* maintains location raw data cache for image tile */
CREATE TABLE IF NOT EXISTS image_tile_cache
(
guid BIGINT NOT NULL REFERENCES record_registry (guid),
frame SMALLINT UNSIGNED NOT NULL,
slice SMALLINT UNSIGNED NOT NULL,
channel SMALLINT UNSIGNED NOT NULL,
site SMALLINT UNSIGNED NOT NULL,
start_x INTEGER UNSIGNED NOT NULL,
start_y INTEGER UNSIGNED NOT NULL,
end_x INTEGER UNSIGNED NOT NULL,
end_y INTEGER UNSIGNED NOT NULL,
filename VARCHAR(64) NOT NULL
)ENGINE = MyISAM;

/* Remove CORRECTION from x_type */
alter table acquisition_profile modify column x_type enum('VALUE','FACTOR') DEFAULT 'VALUE';
/* Remove CORRECTION from y_type */
alter table acquisition_profile modify column y_type enum('VALUE','FACTOR') DEFAULT 'VALUE';
/* Remove CORRECTION from z_type */
alter table acquisition_profile modify column z_type enum('VALUE','FACTOR') DEFAULT 'VALUE';


/* changes time_added for default value as current time and update value as provided*/ 
alter table user_comments_on_records modify column time_added timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP;


/* clients tags */
CREATE TABLE IF NOT EXISTS client_tags
(
/*clientid of client*/
clientid VARCHAR(64) NOT NULL REFERENCES client_registry(clientid),
/* tag for client */
tag VARCHAR(64) NOT NULL,
UNIQUE(clientid,tag)
)ENGINE = MyISAM;


