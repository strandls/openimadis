use mysql;
DELETE FROM user WHERE user = 'strand';
FLUSH PRIVILEGES;

-- SHOW CHARACTER SET to see the charater sets available in MySQL

/* master database contaning the principle tables */

create database imagedb character set utf8;
grant all privileges on imagedb.* to 'strand'@'localhost' identified by 'strand123';

/* project specific storage database */

create database image_storagedb character set utf8;
grant all privileges on image_storagedb.* to 'strand'@'localhost' identified by 'strand123';

-- IMPORTANT
-- all table names are in small letters to ensure portability between 
-- a windows (case insensitive) and a unix flavoured machine (case sensitive)
-- MySQL String Type Columns
-- Values in CHAR and VARCHAR columns are sorted and compared in case-insensitive fashion, 
-- unless the BINARY attribute was specified when the table was created. 
-- The BINARY attribute means that column values are sorted and compared in case-sensitive fashion according to the ASCII 
-- order of the machine where the MySQL server is running. 
-- BINARY doesn't affect how the column is stored or retrieved. ..

use imagedb;

/* list of all registered user */
/* this table will be filled irrespective of whether authentication is happing through LDAP (internal user) etc */
/* for LDAP user, the password field will be empty */
/* each registered user is either a project member, project manager, team leader or facility manager */
/* a user can be active, suspended or deleted state */
CREATE TABLE IF NOT EXISTS user_registry
(
/* login ID of the user */
user_login VARCHAR(64) NOT NULL,
/* christian name of the user */
name VARCHAR(128),
/* email address of the user */
email VARCHAR(64),
/* password of the user - needed for external user, internal user can be validated through LDAP etc */
password VARCHAR(32),
/* checks whether the user is internal or external user */
authentication_type ENUM ('External', 'Internal'),  
/* assigned roles with decreasing responsibilities */
user_rank ENUM ('Administrator','FacilityManager','TeamLeader', 'TeamMember') NOT NULL DEFAULT 'TeamMember',
/* status of the user, an user is either active, suspended or deleted */ 
status ENUM ('Active','Suspended','Deleted') NOT NULL DEFAULT 'Active',
/* last login time */
last_login TIMESTAMP,
/* number of active logins */
login_count INTEGER NOT NULL DEFAULT 0,
/* login name id unique */
PRIMARY KEY (user_login)
) ENGINE=MyISAM;


/* List of tickets */
CREATE TABLE IF NOT EXISTS ticket_registry
(
/* ticket id */
ticket_id BIGINT NOT NULL,
/* time when the ticket was created */
request_time TIMESTAMP NOT NULL,
/* time when the ticket was last modified */
last_modification_time TIMESTAMP NOT NULL,
/* each member has a specific role */
job_status ENUM ('WAITING','EXPIRED','UPLOADING','QUEUED','EXECUTING','SUCCESS','FAILURE','DUPLICATE') NOT NULL DEFAULT 'WAITING',
PRIMARY KEY (ticket_id)
) ENGINE=MyISAM;

/* records will be stored at various storage machines (NFS mounted etc) */
CREATE TABLE IF NOT EXISTS repository_registry 
(
repository_id INTEGER AUTO_INCREMENT,
/* ip address of the storage machine */
machine_ip VARCHAR(64) NOT NULL UNIQUE, 
/* absolute location within the storage machine, within this root folder a folder of name guid will be created to store the record */
/* additionally, the guid folder will contain a folder named attachments for storing attchments associated with the record */
root_folder VARCHAR(1024) NOT NULL,
/* description if any */
description TEXT,
/*checks whether the repository is online */
available BOOLEAN NOT NULL DEFAULT true,
/* primary key of a repository */
PRIMARY KEY (repository_id)
)ENGINE = MyISAM;


/* list of available projects - projects are created by a team leader, have a project manager */
/* a project will have a unique name across all projects */
/* a project can be in Active/Archived/Pending Archival/Pending Restoration/Pending Deletion/Deleted State */
/* project is a tag that is used by the system to split the storage (of records and associated data) into different physical database tables */
/* the actual table name may be derived from a convention - have the project id appended to the template table name */
/* each project will have a storage quota in kilo bytes, */
CREATE TABLE IF NOT EXISTS project_registry
(
/* db generated unique identifier */
project_id INTEGER AUTO_INCREMENT,
/* name of the project */
name VARCHAR(64) NOT NULL,
/* short description of the text */
notes TEXT,
/* the date and time when the project was created */
creation_date TIMESTAMP NOT NULL,
/* a project can be in Active/Archived/Pending Archival/Pending Restoration/Pending Deletion/Deleted State */
status ENUM ('Active','Archived','Deleted', 'ArchiveQ','Archiving','Restoring','RestorationQ', 'DeletionQ') NOT NULL DEFAULT 'Active',
/* the guy who created this project */
created_by VARCHAR(64) NOT NULL REFERENCES user_registry (user_login),
/* number of records in this project */
no_of_records INTEGER NOT NULL DEFAULT 0,
/* disk usage in gb */
space_usage DOUBLE NOT NULL DEFAULT 0,
/* disk quota in gb */
storage_quota DOUBLE NOT NULL DEFAULT 100,
/* project location, basically the name of the project-folder for storage - after removing special characters from project name */
storage_location VARCHAR(80) NOT NULL,
/* project name is unique across all projects */
UNIQUE(name),
UNIQUE(storage_location),
PRIMARY KEY (project_id)
) ENGINE=MyISAM;

/* a user can be member of many projects */
/* a project will have many user */
CREATE TABLE IF NOT EXISTS project_membership
(
/* project id */
project_id INTEGER NOT NULL REFERENCES project_registry (project_id),
/* login id of the project member */
user_login VARCHAR(64) NOT NULL REFERENCES user_registry (user_login),
/* each member has a specific role */
user_role ENUM ('Administrator', 'FacilityManager', 'Manager','Upload','Write','Export','Read','None') NOT NULL DEFAULT 'Write',
PRIMARY KEY (project_id, user_login),
INDEX(project_id, user_role)
) ENGINE=MyISAM;


/* storage information associated with a record */
/* a folder of name guid will be created to contain the record within the repository folder */
CREATE TABLE IF NOT EXISTS archive_registry
(
/* keep the md5 hash of the archive files (after sorting them by size followed by names) */
/* 16 byte for the md5 hash - two (hex number) char for per byte - total exactly 32 byte */
archive_signature char(32),
/* name of the folder containing the record-archive, typically project-name/user-name */
root_folder_name VARCHAR(512) NOT NULL,
/* name of record specific folder - typically record file-name as the folder name */
/* this folder will contain the record source files */
/* this folder will also contain an attachment folder containing the attachments */
/* in case of an existing folder of the same name, will create folder by suffixing with number, till a unique folder is created */
archive_folder_name VARCHAR(512) NOT NULL,
/* source files description */
source_reference MEDIUMBLOB NOT NULL,
/* one archive across the system */
PRIMARY KEY (archive_signature)
) ENGINE = MyISAM;

/* guid generator, also a way to remove duplicate records */
CREATE TABLE IF NOT EXISTS record_registry 
(
/* auto increment number */
guid BIGINT AUTO_INCREMENT,
/* project id */
project_id INTEGER NOT NULL REFERENCES project_registry (project_id),
/* a record is uploaded by a specific user */
uploaded_by VARCHAR(64) NOT NULL REFERENCES user_registry (user_login),
/* number of slices (Z-positions) */
number_of_slices SMALLINT UNSIGNED NOT NULL,
/* number of frames (time samples)  */
number_of_frames SMALLINT UNSIGNED NOT NULL,
/* numbers of channels (wavelengths) */
number_of_channels SMALLINT UNSIGNED NOT NULL,
/* numbers of sites (location) */
number_of_sites SMALLINT UNSIGNED NOT NULL,
/* image width in pixels  */
image_width INTEGER UNSIGNED NOT NULL,
/* image height in pixels */
image_height INTEGER UNSIGNED NOT NULL,
/* md5 hash of the sites (list of series number in sorted order) */
site_hash char(32) NOT NULL,
/* the archive reference, part of record signature */
archive_signature char(32) NOT NULL REFERENCES archive_registry (archive_signature),
/* the time when this record was uploaded */
upload_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, 
/* timestamp according the last modification time of the source files */
source_file_time TIMESTAMP NOT NULL,
/* time when the record was created by the acquisition clint */
creation_time TIMESTAMP NOT NULL,
/* the time when the record was generated from the microscopes, may not be available */
/* acquired time is changed from TIMESTAMP to DATETIME because mysql doesnot allow TIMESTAMP to be null*/
acquired_time DATETIME DEFAULT NULL,
/* pixel depth, number of bytes per pixel, typically 1,2 and 4 */
pixel_depth SMALLINT NOT NULL,
/* pixel size in microns in x dimension - float - default: 1.0 */
pixel_size_x DOUBLE NOT NULL default 1.0,
/* pixel size in microns in y dimension - float - default: 1.0  */
pixel_size_y DOUBLE NOT NULL default 1.0,
/* pixel size in microns in y dimension - float - default: 1.0  */
pixel_size_z DOUBLE NOT NULL default 1.0,
/* source type (formats) */
source_type VARCHAR(32) NOT NULL,
/* image type - a fixed set of values (Grayscale, RGB, ) - default: Grayscale */
image_type ENUM ('GRAYSCALE','RGB') NOT NULL DEFAULT 'GRAYSCALE',
/* ip address of the acquisition machine */
machine_ip CHAR(64) NOT NULL, 
/* MAC address - A Media Access Control address - (ethernet card) - network hardware address of the acquisition computer  */
mac_address VARCHAR(32) NOT NULL,
/* source directory */
source_folder VARCHAR(256) NOT NULL,
/* source file name */
source_filename VARCHAR(128) NOT NULL,
/* dump of the list of channels of this record */
channels MEDIUMBLOB NOT NULL,
/* dump of list of sites of this record */
sites MEDIUMBLOB NOT NULL,
/* status of this record true if marked for deletion, false otherwise*/
record_marker enum('Active','Deleted','Archived', 'UNDER_CONSTRUCTION') NOT NULL DEFAULT 'Active',
/* dump of acquisition profile of this record */
acq_profile MEDIUMBLOB NOT NULL,
/* the signature is unique for a record */
UNIQUE (number_of_slices, number_of_frames, number_of_channels, number_of_sites, image_width, image_height, site_hash, archive_signature),
INDEX (archive_signature),
INDEX (project_id),
INDEX (guid),
PRIMARY KEY (guid)
) ENGINE=MyISAM;

/* comments added on records by user */
CREATE TABLE IF NOT EXISTS user_comments_on_records
(
/* auto increment number */
comment_id BIGINT AUTO_INCREMENT,
/* GUID of the record */
guid BIGINT NOT NULL REFERENCES record_registry (guid),
/* who added the comment */
added_by VARCHAR(64) NOT NULL REFERENCES user_registry (user_login),
/* when the comment was added */
time_added TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
/* comment  associated with this record */
comments TEXT NOT NULL,
PRIMARY KEY (comment_id),
INDEX (guid, added_by)
) ENGINE=MyISAM;

/* a record can one or more attachments - files of any type */
/* attachments will be stored within the attachment folder of the record folder */
CREATE TABLE IF NOT EXISTS record_attachments
(
/* global unique identifier */
guid BIGINT NOT NULL REFERENCES record_registry(guid) ON DELETE CASCADE, 
/* the guy how have added this attachment */
uploaded_by VARCHAR(64) NOT NULL REFERENCES user_registry (user_login),
/* attachments can be global - applicable to all record of an experiment (archive) and local, meaning applicable to this record only */
attachment_type ENUM ('Global', 'Local') NOT NULL DEFAULT 'Local',  
/* name of the attachment */
attachment_name VARCHAR(255) NOT NULL,
/* description of the attachment */
notes TEXT,
/* unique list of attachments for a given record */
PRIMARY KEY (guid, attachment_name)
) ENGINE = MyISAM;

/* explicit permission associated with a record for users */
CREATE TABLE IF NOT EXISTS record_permission
(
/* user id of the user */
user_login VARCHAR(64) NOT NULL REFERENCES user_registry (user_login),
/* global unique identifier of the record */
guid BIGINT NOT NULL REFERENCES record_registry (guid),
/* permission READ 0, WRITE 1*/
permission SMALLINT UNSIGNED NOT NULL,
PRIMARY KEY (user_login, guid, permission)
)ENGINE = MyISAM;

/* list of recent projects used by the user */
CREATE TABLE IF NOT EXISTS user_recent_projects
(
/* user id of the user */
user_login VARCHAR(64) NOT NULL REFERENCES user_registry (user_login),
/* project id */
project_id INTEGER NOT NULL REFERENCES project_registry (project_id),
/* list of search columns */
spreadsheet_columns MEDIUMBLOB NULL,
/* list of navigation columns */
navigation_columns MEDIUMBLOB NULL,
/* list of overlay columns */
overlay_columns MEDIUMBLOB NULL,
/* number of bins */
no_of_bins SMALLINT NOT NULL DEFAULT 10,
/* ascending or descending order of bins */
ascending_bins BOOLEAN NOT NULL DEFAULT true,
/* when the project was recently accessed */
last_access_time TIMESTAMP NOT NULL,
PRIMARY KEY (user_login, project_id)
)ENGINE = MyISAM;

/* channel color preference for the user */
CREATE TABLE IF NOT EXISTS user_record_channel_colors 
(
/* user id of the user */
user_login VARCHAR(64) NOT NULL REFERENCES user_registry (user_login),
/* global unique identifier of the record */
guid BIGINT NOT NULL REFERENCES record_registry (guid),
/* colors and contrast for channels */
channels MEDIUMBLOB,
PRIMARY KEY (user_login, guid)
)ENGINE = MyISAM;

/* thumbnail for the record */
CREATE TABLE IF NOT EXISTS thumbnail_registry 
(
/* global unique identifier of the record */
guid BIGINT NOT NULL REFERENCES record_registry (guid),
/* thumbnail for the record */
thumbnail MEDIUMBLOB,
revision BIGINT NOT NULL DEFAULT 0,
PRIMARY KEY (guid)
)ENGINE = MyISAM;

/* table containing description of movie */
CREATE TABLE IF NOT EXISTS movie_registry
(
/* identifier for the movie */
movieid BIGINT NOT NULL,
/* blob of movie ticket associated with movie */
movieticket MEDIUMBLOB,
PRIMARY KEY (movieid)
)ENGINE = MyISAM;

/* table containing images for movie  */
CREATE TABLE IF NOT EXISTS movie_images 
(
/* identifier for the movie */
movieid BIGINT NOT NULL REFERENCES movie_registry (movieid),
/* counter specifying frame/slice value of image */
counter INTEGER NOT NULL,
/* absolute path to location where images are stored*/
path VARCHAR(1024) NOT NULL,
PRIMARY KEY (movieid, counter)
)ENGINE = MyISAM;

/* table containing clients registered */
CREATE TABLE IF NOT EXISTS client_registry
(
/* unique identifier for the client*/
clientid VARCHAR(64) NOT NULL,
/* name of the client*/
name VARCHAR(64) NOT NULL,
/* version of the client*/
version VARCHAR(20),
/* description of the client*/
description TEXT,
/* user who created the client*/
user_login VARCHAR(64) NOT NULL REFERENCES user_registry (user_login),
/* status of the client*/
status ENUM ('Active','Disabled') NOT NULL DEFAULT 'Active',
/* is this client a workflow or not*/
is_workflow BOOLEAN NOT NULL DEFAULT TRUE,
/* url in case of web client*/
url VARCHAR(1024) DEFAULT NULL,
/* clientid must be unique for every client */
UNIQUE(clientid),
/* name, version uniquely identify a client */
PRIMARY KEY (name, version)
)ENGINE = MyISAM;

/* table containing authcodes and access tokens */
CREATE TABLE IF NOT EXISTS authcode_registry
(
/* auto increment id for tokens. this has no significance as such. just a way
to reference a particular row without using actual id */
auth_id BIGINT AUTO_INCREMENT,
/* unique identifier */
id VARCHAR(64) NOT NULL,
/* user associated with the authcode */
user_login VARCHAR(64) NOT NULL REFERENCES user_registry (user_login),
/* client associated with the authcode */
clientid VARCHAR(64) NOT NULL REFERENCES client_registry (clientid),
/* Encodes the level of access for this authcode */
services INTEGER NOT NULL DEFAULT 0,
/* creation time for this authcode */
creation TIMESTAMP NOT NULL DEFAULT 0,
/* last access time for this authcode */
last_access TIMESTAMP NOT NULL DEFAULT 0,
/* expiry time for this authcode */
expiry  TIMESTAMP NOT NULL DEFAULT 0,
/* is the authcode valid */
valid BOOLEAN NOT NULL DEFAULT TRUE,
/* ip filters for the authcode */
filters MEDIUMBLOB,
/* has the authcode been delivered, when it turns a access token */
delivered BOOLEAN NOT NULL DEFAULT FALSE,
/* id of the authcode should be unique */
UNIQUE(id),
PRIMARY KEY (auth_id)
)ENGINE = MyISAM;

/* table containing information about all the finished(terminated, failed, successful) tasks */
CREATE TABLE IF NOT EXISTS archived_task_registry
(
/* unique identifier for work representation */
id BIGINT NOT NULL,
/* owner of the task*/
owner VARCHAR(128) NOT NULL,
/* auth code required for the task */
/* currently authcode is 40 character string */
authcode VARCHAR(40) NOT NULL,
/* priority of the task */
priority ENUM ('HIGH', 'MEDIUM', 'LOW') NOT NULL,
/* name of application */
app_name VARCHAR(128) NOT NULL,
/* version of application */
app_veriosn VARCHAR(128) NOT NULL,
/* project_id on which task is executed*/
project_id int(11) NOT NULL,
/*is task is under owner's task monitor*/
is_monitored BOOLEAN NOT NULL DEFAULT FALSE,
/* the time when the task is scheduled*/
schedule_time BIGINT NOT NULL,
/* state of the task */
/* the archived registry will contain tasks that are not currently active */
state ENUM ('DELETED', 'ERROR', 'SUCCESS', 'TERMINATED') NOT NULL,
/* parameters required for the task */
parameters MEDIUMBLOB NOT NULL,
/* input guids */
input_guids MEDIUMBLOB NOT NULL,
/* identifier for auth code*/
auth_identifier BIGINT, 
/* output guids */
output_guids MEDIUMBLOB DEFAULT NULL,
PRIMARY KEY (id),
INDEX(auth_identifier)
)ENGINE = MyISAM;

/* table containing information about all the active(allocated, paused, running, waiting, terminating) tasks */
CREATE TABLE IF NOT EXISTS active_task_registry
(
/* unique identifier for work representation */
id BIGINT NOT NULL,
/* owner of the task*/
owner VARCHAR(128) NOT NULL,
/* auth code required for the task, will be converted to accessToken at time of login*/
/* currently authcode is 40 character string */
authcode VARCHAR(40) NOT NULL,
/* priority of the task */
priority ENUM ('HIGH', 'MEDIUM', 'LOW') NOT NULL,
/* name of application */
app_name VARCHAR(128) NOT NULL,
/* version of application */
app_veriosn VARCHAR(128) NOT NULL,
/* project_id on which task is executed*/
project_id int(11) NOT NULL,
/*is task is under owner's task monitor*/
is_monitored BOOLEAN NOT NULL DEFAULT FALSE,
/* the time when the task is scheduled*/
schedule_time BIGINT NOT NULL,
/* state of the task */
/* the archived registry will contain tasks that are not currently active */
state ENUM ('SCHEDULED', 'WAITING', 'PAUSED', 'ALLOCATED', 'EXECUTING', 'TERMINATING') NOT NULL,
/* parameters required for the task */
parameters MEDIUMBLOB NOT NULL,
/* input guids */
input_guids MEDIUMBLOB NOT NULL,
/* task progress */
progress INTEGER,
/* identifier for auth code*/
auth_identifier BIGINT, 
PRIMARY KEY (id),
INDEX(auth_identifier)
)ENGINE = MyISAM;

/* table containing information about all the authorized publishers */
CREATE TABLE IF NOT EXISTS publisher_registry
(
/* name of the publisher */
name VARCHAR(128) NOT NULL,
/* description of the publisher */
description TEXT NOT NULL,
/* publisher code granted by the server */
publisher_code VARCHAR(128) NOT NULL,
/* ip filter associated with the publisher*/
ip_filter VARCHAR(1024),
PRIMARY KEY (name)
)ENGINE = MyISAM;

/* table containing information about record under creation */
CREATE TABLE IF NOT EXISTS record_creation_registry
(
/* unique identifier for record builder */
guid BIGINT NOT NULL REFERENCES record_registry (guid),
/* identifier for parent record */
parentGuid BIGINT DEFAULT NULL,
/* location where pixel data is stored */
pixel_data_location VARCHAR(1024) NOT NULL,
/* number of slices (Z-positions) */
number_of_slices SMALLINT UNSIGNED NOT NULL,
/* number of frames (time samples)  */
number_of_frames SMALLINT UNSIGNED NOT NULL,
/* numbers of channels (wavelengths) */
number_of_channels SMALLINT UNSIGNED NOT NULL,
/* numbers of sites (location) */
number_of_sites SMALLINT UNSIGNED NOT NULL,
/* image width in pixels  */
image_width SMALLINT UNSIGNED NOT NULL,
/* image height in pixels */
image_height SMALLINT UNSIGNED NOT NULL,
/* pixel depth, number of bytes per pixel, typically 1,2 and 4 */
pixel_depth SMALLINT NOT NULL,
/* list of dimensions for which pixel data is received */
dimensions MEDIUMBLOB,
/* last modification time */
last_access_time TIMESTAMP NOT NULL,
PRIMARY KEY (guid)
)ENGINE = MyISAM;

/* table containing information about all the user logins */
CREATE TABLE IF NOT EXISTS login_history
(
/* logged in user */
user_login VARCHAR(128) NOT NULL,
/* name of the client used for login */
client_name VARCHAR(128) NOT NULL,
/* version of the client used for login */
client_version VARCHAR(128) NOT NULL,
/* time of login*/
login_time TIMESTAMP NOT NULL,
INDEX(login_time)
)ENGINE = MyISAM;

/* table containing description of zoom */
CREATE TABLE IF NOT EXISTS zoom_registry
(
/* identifier for the movie */
zoomid BIGINT NOT NULL,
/* last access time */
last_access TIMESTAMP NOT NULL,
/* blob of zoom request */
zoom_request MEDIUMBLOB,
PRIMARY KEY (zoomid),
INDEX(last_access)
)ENGINE = MyISAM;

/* table containing description of zoom */
CREATE TABLE IF NOT EXISTS zoom_tiles_registry
(
/* identifier for the movie */
zoomid BIGINT NOT NULL,
/* zoom level */
zoom_level INTEGER NOT NULL, 
/* xTile */
x_tile INTEGER NOT NULL,
/* yTile */
y_tile INTEGER NOT NULL,
/* path for storage directory */
storage_path VARCHAR(1024) NOT NULL,
INDEX(zoomid)
)ENGINE = MyISAM;

/* table containing description of export requests */
CREATE TABLE IF NOT EXISTS export_registry
(
/* identifier of export request */
exportid BIGINT NOT NULL, 
/* record ids */
guids MEDIUMBLOB NOT NULL,
/* format in which record is exported*/
export_format ENUM ('ORIGINAL_FORMAT', 'OME_TIFF_FORMAT') NOT NULL,
/* user id who submitted the request*/
submitted_by VARCHAR(128) NOT NULL,
/* time till which the request is valid*/
validity TIMESTAMP NOT NULL DEFAULT 0,
/* name given to export */
name VARCHAR(128) NOT NULL,
/* export location */
export_location VARCHAR(1024) DEFAULT NULL,
/* time when this request was submitted */
submitted_on TIMESTAMP NOT NULL DEFAULT 0,
/* status of the request */
status ENUM ('SUBMITTED','SUCCESSFUL','FAILED','TERMINATED') NOT NULL,
/* size of export request */
size BIGINT NOT NULL,
PRIMARY KEY (exportid),
INDEX(submitted_by)
)ENGINE = MyISAM;

/* table containing description of units */
CREATE TABLE IF NOT EXISTS unit_registry
(
/*name of the unit*/
unit_name VARCHAR(128) NOT NULL,
/*type of the unit*/
unit_type ENUM ('INTERNAL', 'EXTERNAL') NOT NULL,
/* global storage space available to unit*/
storage_space DOUBLE NOT NULL,
/*email of point of contact*/
contact VARCHAR(1024),
PRIMARY KEY (unit_name)
)ENGINE = MyISAM;

/* table containing association between project and unit */
CREATE TABLE IF NOT EXISTS unit_association_registry
(
/*name of the unit*/
unit_name VARCHAR(128) NOT NULL,
/*project*/
project_id INTEGER NOT NULL,
/* global storage space available to unit*/
storage_space DOUBLE NOT NULL,
PRIMARY KEY (unit_name, project_id),
INDEX (unit_name, project_id)
)ENGINE = MyISAM;

/* table containing information about shortcuts*/
CREATE TABLE IF NOT EXISTS shortcut_registry
(
/*id of the shortcut record*/
shortcut_signature char(32) not NULL,
/*original archive signature*/
original_archive_sign char(32) not NULL,
PRIMARY KEY (shortcut_signature),
INDEX (shortcut_signature)
)ENGINE = MyISAM;

/* legend fields for the user */
CREATE TABLE IF NOT EXISTS user_legends 
(
/* user id of the user */
user_login VARCHAR(64) NOT NULL REFERENCES user_registry (user_login),
/* legend fields */
legends MEDIUMBLOB,
/*location to display the legend*/
location ENUM ('TOPLEFT','BOTTOMRIGHT','TOPRIGHT','BOTTOMLEFT') DEFAULT 'TOPLEFT',
PRIMARY KEY (user_login)
)ENGINE = MyISAM;

/* backup information for the project */
CREATE TABLE IF NOT EXISTS project_backup_registry
(
/* specified project */
project_id INTEGER NOT NULL REFERENCES project_registry (project_id),
/* location where project is backed up */
backup_location VARCHAR(256) NOT NULL,
/* signature computed at the time of archiving project data*/
sign VARCHAR(64) NOT NULL,
PRIMARY KEY (project_id)
)ENGINE = MyISAM;

/* information for the microscopes */
CREATE TABLE IF NOT EXISTS microscope_registry
(
/* name of the microscope */
microscope_name VARCHAR(256) NOT NULL,
/* ip address of the microscope */
machine_ip CHAR(64) NOT NULL, 
/* MAC address - A Media Access Control address - (ethernet card) - network hardware address of the acquisition microscope  */
mac_address VARCHAR(32) NOT NULL,
/* number of acquisition licenses reserved for this microscope*/
licenses INTEGER DEFAULT 1,
UNIQUE(microscope_name),
INDEX (microscope_name)
)ENGINE = MyISAM;

/* acquisition profiles */
CREATE TABLE IF NOT EXISTS acquisition_profile
(
/* name of the profile */
profile_name VARCHAR(1024) NOT NULL,
/* name of the microscope */
microscope_name VARCHAR(1024) NOT NULL,
/* pixel size in microns in x dimension default: NULL */
pixel_size_x DOUBLE DEFAULT NULL,
/* pixel size in microns in y dimension default: NULL  */
pixel_size_y DOUBLE DEFAULT NULL,
/* pixel size in microns in y dimension default: NULL  */
pixel_size_z DOUBLE DEFAULT NULL,
/* source type (formats) */
source_type VARCHAR(32) DEFAULT NULL,
/* time unit for elapsed time*/
elapsed_time_unit ENUM ('SECONDS', 'MILISECONDS', 'MICROSECONDS', 'NANOSECONDS') DEFAULT 'MILISECONDS',
/* time unit for exposure time*/
exposure_time_unit ENUM ('SECONDS', 'MILISECONDS', 'MICROSECONDS', 'NANOSECONDS') DEFAULT 'MILISECONDS',
/* length unit*/
length_unit ENUM ('MILIMETER', 'MICROMETER', 'NANOMETER') DEFAULT 'MICROMETER',
/* X type*/
x_type ENUM ('VALUE', 'FACTOR') DEFAULT 'VALUE',
/* Y type*/
y_type ENUM ('VALUE', 'FACTOR') DEFAULT 'VALUE',
/* Z type*/
z_type ENUM ('VALUE', 'FACTOR') DEFAULT 'VALUE',
INDEX(microscope_name)
)ENGINE = MyISAM;

/* record creation requsts*/
CREATE TABLE IF NOT EXISTS creation_request_registry
(
/* ticket id */
ticket_id BIGINT NOT NULL,
/* hash of the records being uploaded*/
request_hash char(32) NOT NULL,
/* serialized version of request object */
request MEDIUMBLOB NOT NULL,
INDEX(ticket_id, request_hash)
)ENGINE = MyISAM;

/* active acquisition licenses */
CREATE TABLE IF NOT EXISTS acq_license_registry
(
/*license id*/
id BIGINT NOT NULL,
/* ip address of the acquisition machine */
machine_ip CHAR(64) NOT NULL, 
/* MAC address - A Media Access Control address - (ethernet card) - network hardware address of the acquisition computer  */
mac_address VARCHAR(32) NOT NULL,
/* time when the license was issued */
issue_time TIMESTAMP NOT NULL,
/* accessToken used for requesting the license*/
access_token VARCHAR(40) NOT NULL,
INDEX(access_token)
)ENGINE = MyISAM;

/* maintains location of task logs for ever task */
CREATE TABLE IF NOT EXISTS task_log_registry
(
/*task id*/
task_id BIGINT NOT NULL,
/* task logs location */
task_location VARCHAR(64) NOT NULL,
/* log file name */
filename VARCHAR(64) NOT NULL,
INDEX(task_id)
)ENGINE = MyISAM;

/* maintains location raw data cache for image tile */
CREATE TABLE IF NOT EXISTS image_tile_cache
(
/* GUID of the record */
guid BIGINT NOT NULL REFERENCES record_registry (guid),
/* frame of the image */
frame SMALLINT UNSIGNED NOT NULL,
/* slice of the image */
slice SMALLINT UNSIGNED NOT NULL,
/* channel of the image */
channel SMALLINT UNSIGNED NOT NULL,
/* site of the image */
site SMALLINT UNSIGNED NOT NULL,
/* x coordinate of top-left of tile*/
start_x INTEGER UNSIGNED NOT NULL,
/* y coordinate of top-left of tile*/
start_y INTEGER UNSIGNED NOT NULL,
/* end x coordinate of tile*/
end_x INTEGER UNSIGNED NOT NULL,
/* end y coordinate of tile*/
end_y INTEGER UNSIGNED NOT NULL,
/* raw data file name */
filename VARCHAR(64) NOT NULL,
/* last access time */
access_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
)ENGINE = MyISAM;

/* clients tags */
CREATE TABLE IF NOT EXISTS client_tags
(
/* clientid of client*/
clientid VARCHAR(64) NOT NULL REFERENCES client_registry(clientid),
/* tag for client */
tag VARCHAR(64) NOT NULL,
UNIQUE(clientid,tag)
)ENGINE = MyISAM;

/* signature mappings */
CREATE TABLE IF NOT EXISTS signature_mapping
(
/* old signature*/
old_archive_signature char(32) NOT NULL,
/* new signature */
new_archive_signature char(32) NOT NULL
)ENGINE = MyISAM;

/* tiles registry*/
CREATE TABLE IF NOT EXISTS tiles_registry 
(
/*guid*/
guid bigint(20) NOT NULL,
/*zoom reverse level i.e (max zoom-current zoom)*/
zoom_reverse_level int(11) NOT NULL,
/* is tile ready*/
isReady tinyint(1) NOT NULL DEFAULT '0',
/*storage path of tile*/
storage_path varchar(1024) NOT NULL,
PRIMARY KEY (guid,zoom_reverse_level)
) ENGINE=MyISAM;

/* tiles execution status*/
CREATE TABLE IF NOT EXISTS tiles_executionStatus (
/*guid*/
guid bigint(20) NOT NULL,
/*estimated time for tile creation*/
estimated_time double NOT NULL,
/*elapsed time for tile creation*/
elapsed_time double NOT NULL,
/*size of tile in memory*/
size bigint(20) NOT NULL,
PRIMARY KEY (guid)
) ENGINE=MyISAM;

/* worker_status_registry*/
CREATE TABLE IF NOT EXISTS worker_status_registry
(
worker_id smallint(6) NOT NULL,

service_type enum('MOVIE_SERVICE','EXPORT_SERVICE','EXTRACTION_SERVICE','TILING_SERVICE','REINDEX_SERVICE','BACKUP_SERVICE','CACHE_CLEANING_SERVICE') NOT NULL,

service_status_object mediumblob NOT NULL,

last_modification_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

to_be_restarted tinyint(1) DEFAULT '0',
  
PRIMARY KEY (worker_id,service_type)
) ENGINE=MyISAM;
