use imagedb;
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

