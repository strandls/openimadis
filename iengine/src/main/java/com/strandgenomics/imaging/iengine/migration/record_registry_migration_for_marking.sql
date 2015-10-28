use imagedb;
alter table record_registry drop column deleted;
alter table record_registry add column record_marker enum('Active','Deleted','Archived') NOT NULL DEFAULT 'Active';
update record_registry set record_marker = 'Active';
