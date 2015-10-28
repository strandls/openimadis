use imagedb;
/* adding revision in thumbnail_registry; */
alter table thumbnail_registry add revision BIGINT NOT NULL DEFAULT 0;
