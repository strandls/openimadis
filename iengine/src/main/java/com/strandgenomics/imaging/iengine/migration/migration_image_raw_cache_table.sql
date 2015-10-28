use imagedb;
alter table image_tile_cache add column access_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
update image_tile_cache set access_time = NOW();
