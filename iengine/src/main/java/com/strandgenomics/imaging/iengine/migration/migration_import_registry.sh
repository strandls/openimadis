#!/bin/bash
echo "use image_storagedb;"
# i varies over the project number
for i in {1..29}
do
echo           "CREATE TABLE IF NOT EXISTS project_"$i"_import_registry
                (
		ticket_id BIGINT NOT NULL,
		request_time TIMESTAMP NOT NULL,
		request MEDIUMBLOB NOT NULL,
		job_status ENUM ('WAITING','EXPIRED','UPLOADING','QUEUED','EXECUTING','SUCCESS','FAILURE','DUPLICATE') NOT NULL DEFAULT 'WAITING',
		last_modification_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
		PRIMARY KEY (ticket_id)
		)ENGINE = MyISAM;"
done
