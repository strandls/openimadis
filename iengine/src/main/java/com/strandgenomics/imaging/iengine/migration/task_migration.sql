use imagedb;
/* adding project_id column to the active_task_registry, archived_task_registry table; */
alter table active_task_registry add project_id int(11);
alter table archived_task_registry add project_id int(11);
alter table active_task_registry add is_monitored BOOLEAN NOT NULL DEFAULT FALSE;
alter table archived_task_registry add is_monitored BOOLEAN NOT NULL DEFAULT FALSE;
