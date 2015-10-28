use imagedb;

alter table record_registry modify column record_marker enum('Active','Deleted','Archived','UNDER_CONSTRUCTION') default 'Active';

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
