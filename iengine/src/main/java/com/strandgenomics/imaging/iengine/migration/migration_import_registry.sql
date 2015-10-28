use image_storagedb;
CREATE TABLE IF NOT EXISTS project_1_import_registry
                (
		ticket_id BIGINT NOT NULL,
		request_time TIMESTAMP NOT NULL,
		request MEDIUMBLOB NOT NULL,
		job_status ENUM ('WAITING','EXPIRED','UPLOADING','QUEUED','EXECUTING','SUCCESS','FAILURE','DUPLICATE') NOT NULL DEFAULT 'WAITING',
		last_modification_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
		PRIMARY KEY (ticket_id)
		)ENGINE = MyISAM;
CREATE TABLE IF NOT EXISTS project_2_import_registry
                (
		ticket_id BIGINT NOT NULL,
		request_time TIMESTAMP NOT NULL,
		request MEDIUMBLOB NOT NULL,
		job_status ENUM ('WAITING','EXPIRED','UPLOADING','QUEUED','EXECUTING','SUCCESS','FAILURE','DUPLICATE') NOT NULL DEFAULT 'WAITING',
		last_modification_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
		PRIMARY KEY (ticket_id)
		)ENGINE = MyISAM;
CREATE TABLE IF NOT EXISTS project_3_import_registry
                (
		ticket_id BIGINT NOT NULL,
		request_time TIMESTAMP NOT NULL,
		request MEDIUMBLOB NOT NULL,
		job_status ENUM ('WAITING','EXPIRED','UPLOADING','QUEUED','EXECUTING','SUCCESS','FAILURE','DUPLICATE') NOT NULL DEFAULT 'WAITING',
		last_modification_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
		PRIMARY KEY (ticket_id)
		)ENGINE = MyISAM;
CREATE TABLE IF NOT EXISTS project_4_import_registry
                (
		ticket_id BIGINT NOT NULL,
		request_time TIMESTAMP NOT NULL,
		request MEDIUMBLOB NOT NULL,
		job_status ENUM ('WAITING','EXPIRED','UPLOADING','QUEUED','EXECUTING','SUCCESS','FAILURE','DUPLICATE') NOT NULL DEFAULT 'WAITING',
		last_modification_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
		PRIMARY KEY (ticket_id)
		)ENGINE = MyISAM;
CREATE TABLE IF NOT EXISTS project_5_import_registry
                (
		ticket_id BIGINT NOT NULL,
		request_time TIMESTAMP NOT NULL,
		request MEDIUMBLOB NOT NULL,
		job_status ENUM ('WAITING','EXPIRED','UPLOADING','QUEUED','EXECUTING','SUCCESS','FAILURE','DUPLICATE') NOT NULL DEFAULT 'WAITING',
		last_modification_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
		PRIMARY KEY (ticket_id)
		)ENGINE = MyISAM;
CREATE TABLE IF NOT EXISTS project_6_import_registry
                (
		ticket_id BIGINT NOT NULL,
		request_time TIMESTAMP NOT NULL,
		request MEDIUMBLOB NOT NULL,
		job_status ENUM ('WAITING','EXPIRED','UPLOADING','QUEUED','EXECUTING','SUCCESS','FAILURE','DUPLICATE') NOT NULL DEFAULT 'WAITING',
		last_modification_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
		PRIMARY KEY (ticket_id)
		)ENGINE = MyISAM;
CREATE TABLE IF NOT EXISTS project_7_import_registry
                (
		ticket_id BIGINT NOT NULL,
		request_time TIMESTAMP NOT NULL,
		request MEDIUMBLOB NOT NULL,
		job_status ENUM ('WAITING','EXPIRED','UPLOADING','QUEUED','EXECUTING','SUCCESS','FAILURE','DUPLICATE') NOT NULL DEFAULT 'WAITING',
		last_modification_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
		PRIMARY KEY (ticket_id)
		)ENGINE = MyISAM;
CREATE TABLE IF NOT EXISTS project_8_import_registry
                (
		ticket_id BIGINT NOT NULL,
		request_time TIMESTAMP NOT NULL,
		request MEDIUMBLOB NOT NULL,
		job_status ENUM ('WAITING','EXPIRED','UPLOADING','QUEUED','EXECUTING','SUCCESS','FAILURE','DUPLICATE') NOT NULL DEFAULT 'WAITING',
		last_modification_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
		PRIMARY KEY (ticket_id)
		)ENGINE = MyISAM;
CREATE TABLE IF NOT EXISTS project_9_import_registry
                (
		ticket_id BIGINT NOT NULL,
		request_time TIMESTAMP NOT NULL,
		request MEDIUMBLOB NOT NULL,
		job_status ENUM ('WAITING','EXPIRED','UPLOADING','QUEUED','EXECUTING','SUCCESS','FAILURE','DUPLICATE') NOT NULL DEFAULT 'WAITING',
		last_modification_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
		PRIMARY KEY (ticket_id)
		)ENGINE = MyISAM;
CREATE TABLE IF NOT EXISTS project_10_import_registry
                (
		ticket_id BIGINT NOT NULL,
		request_time TIMESTAMP NOT NULL,
		request MEDIUMBLOB NOT NULL,
		job_status ENUM ('WAITING','EXPIRED','UPLOADING','QUEUED','EXECUTING','SUCCESS','FAILURE','DUPLICATE') NOT NULL DEFAULT 'WAITING',
		last_modification_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
		PRIMARY KEY (ticket_id)
		)ENGINE = MyISAM;
CREATE TABLE IF NOT EXISTS project_11_import_registry
                (
		ticket_id BIGINT NOT NULL,
		request_time TIMESTAMP NOT NULL,
		request MEDIUMBLOB NOT NULL,
		job_status ENUM ('WAITING','EXPIRED','UPLOADING','QUEUED','EXECUTING','SUCCESS','FAILURE','DUPLICATE') NOT NULL DEFAULT 'WAITING',
		last_modification_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
		PRIMARY KEY (ticket_id)
		)ENGINE = MyISAM;
CREATE TABLE IF NOT EXISTS project_12_import_registry
                (
		ticket_id BIGINT NOT NULL,
		request_time TIMESTAMP NOT NULL,
		request MEDIUMBLOB NOT NULL,
		job_status ENUM ('WAITING','EXPIRED','UPLOADING','QUEUED','EXECUTING','SUCCESS','FAILURE','DUPLICATE') NOT NULL DEFAULT 'WAITING',
		last_modification_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
		PRIMARY KEY (ticket_id)
		)ENGINE = MyISAM;
CREATE TABLE IF NOT EXISTS project_13_import_registry
                (
		ticket_id BIGINT NOT NULL,
		request_time TIMESTAMP NOT NULL,
		request MEDIUMBLOB NOT NULL,
		job_status ENUM ('WAITING','EXPIRED','UPLOADING','QUEUED','EXECUTING','SUCCESS','FAILURE','DUPLICATE') NOT NULL DEFAULT 'WAITING',
		last_modification_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
		PRIMARY KEY (ticket_id)
		)ENGINE = MyISAM;
CREATE TABLE IF NOT EXISTS project_14_import_registry
                (
		ticket_id BIGINT NOT NULL,
		request_time TIMESTAMP NOT NULL,
		request MEDIUMBLOB NOT NULL,
		job_status ENUM ('WAITING','EXPIRED','UPLOADING','QUEUED','EXECUTING','SUCCESS','FAILURE','DUPLICATE') NOT NULL DEFAULT 'WAITING',
		last_modification_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
		PRIMARY KEY (ticket_id)
		)ENGINE = MyISAM;
CREATE TABLE IF NOT EXISTS project_15_import_registry
                (
		ticket_id BIGINT NOT NULL,
		request_time TIMESTAMP NOT NULL,
		request MEDIUMBLOB NOT NULL,
		job_status ENUM ('WAITING','EXPIRED','UPLOADING','QUEUED','EXECUTING','SUCCESS','FAILURE','DUPLICATE') NOT NULL DEFAULT 'WAITING',
		last_modification_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
		PRIMARY KEY (ticket_id)
		)ENGINE = MyISAM;
CREATE TABLE IF NOT EXISTS project_16_import_registry
                (
		ticket_id BIGINT NOT NULL,
		request_time TIMESTAMP NOT NULL,
		request MEDIUMBLOB NOT NULL,
		job_status ENUM ('WAITING','EXPIRED','UPLOADING','QUEUED','EXECUTING','SUCCESS','FAILURE','DUPLICATE') NOT NULL DEFAULT 'WAITING',
		last_modification_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
		PRIMARY KEY (ticket_id)
		)ENGINE = MyISAM;
CREATE TABLE IF NOT EXISTS project_17_import_registry
                (
		ticket_id BIGINT NOT NULL,
		request_time TIMESTAMP NOT NULL,
		request MEDIUMBLOB NOT NULL,
		job_status ENUM ('WAITING','EXPIRED','UPLOADING','QUEUED','EXECUTING','SUCCESS','FAILURE','DUPLICATE') NOT NULL DEFAULT 'WAITING',
		last_modification_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
		PRIMARY KEY (ticket_id)
		)ENGINE = MyISAM;
CREATE TABLE IF NOT EXISTS project_18_import_registry
                (
		ticket_id BIGINT NOT NULL,
		request_time TIMESTAMP NOT NULL,
		request MEDIUMBLOB NOT NULL,
		job_status ENUM ('WAITING','EXPIRED','UPLOADING','QUEUED','EXECUTING','SUCCESS','FAILURE','DUPLICATE') NOT NULL DEFAULT 'WAITING',
		last_modification_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
		PRIMARY KEY (ticket_id)
		)ENGINE = MyISAM;
CREATE TABLE IF NOT EXISTS project_19_import_registry
                (
		ticket_id BIGINT NOT NULL,
		request_time TIMESTAMP NOT NULL,
		request MEDIUMBLOB NOT NULL,
		job_status ENUM ('WAITING','EXPIRED','UPLOADING','QUEUED','EXECUTING','SUCCESS','FAILURE','DUPLICATE') NOT NULL DEFAULT 'WAITING',
		last_modification_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
		PRIMARY KEY (ticket_id)
		)ENGINE = MyISAM;
CREATE TABLE IF NOT EXISTS project_20_import_registry
                (
		ticket_id BIGINT NOT NULL,
		request_time TIMESTAMP NOT NULL,
		request MEDIUMBLOB NOT NULL,
		job_status ENUM ('WAITING','EXPIRED','UPLOADING','QUEUED','EXECUTING','SUCCESS','FAILURE','DUPLICATE') NOT NULL DEFAULT 'WAITING',
		last_modification_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
		PRIMARY KEY (ticket_id)
		)ENGINE = MyISAM;
CREATE TABLE IF NOT EXISTS project_21_import_registry
                (
		ticket_id BIGINT NOT NULL,
		request_time TIMESTAMP NOT NULL,
		request MEDIUMBLOB NOT NULL,
		job_status ENUM ('WAITING','EXPIRED','UPLOADING','QUEUED','EXECUTING','SUCCESS','FAILURE','DUPLICATE') NOT NULL DEFAULT 'WAITING',
		last_modification_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
		PRIMARY KEY (ticket_id)
		)ENGINE = MyISAM;
CREATE TABLE IF NOT EXISTS project_22_import_registry
                (
		ticket_id BIGINT NOT NULL,
		request_time TIMESTAMP NOT NULL,
		request MEDIUMBLOB NOT NULL,
		job_status ENUM ('WAITING','EXPIRED','UPLOADING','QUEUED','EXECUTING','SUCCESS','FAILURE','DUPLICATE') NOT NULL DEFAULT 'WAITING',
		last_modification_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
		PRIMARY KEY (ticket_id)
		)ENGINE = MyISAM;
CREATE TABLE IF NOT EXISTS project_23_import_registry
                (
		ticket_id BIGINT NOT NULL,
		request_time TIMESTAMP NOT NULL,
		request MEDIUMBLOB NOT NULL,
		job_status ENUM ('WAITING','EXPIRED','UPLOADING','QUEUED','EXECUTING','SUCCESS','FAILURE','DUPLICATE') NOT NULL DEFAULT 'WAITING',
		last_modification_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
		PRIMARY KEY (ticket_id)
		)ENGINE = MyISAM;
CREATE TABLE IF NOT EXISTS project_24_import_registry
                (
		ticket_id BIGINT NOT NULL,
		request_time TIMESTAMP NOT NULL,
		request MEDIUMBLOB NOT NULL,
		job_status ENUM ('WAITING','EXPIRED','UPLOADING','QUEUED','EXECUTING','SUCCESS','FAILURE','DUPLICATE') NOT NULL DEFAULT 'WAITING',
		last_modification_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
		PRIMARY KEY (ticket_id)
		)ENGINE = MyISAM;
CREATE TABLE IF NOT EXISTS project_25_import_registry
                (
		ticket_id BIGINT NOT NULL,
		request_time TIMESTAMP NOT NULL,
		request MEDIUMBLOB NOT NULL,
		job_status ENUM ('WAITING','EXPIRED','UPLOADING','QUEUED','EXECUTING','SUCCESS','FAILURE','DUPLICATE') NOT NULL DEFAULT 'WAITING',
		last_modification_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
		PRIMARY KEY (ticket_id)
		)ENGINE = MyISAM;
CREATE TABLE IF NOT EXISTS project_26_import_registry
                (
		ticket_id BIGINT NOT NULL,
		request_time TIMESTAMP NOT NULL,
		request MEDIUMBLOB NOT NULL,
		job_status ENUM ('WAITING','EXPIRED','UPLOADING','QUEUED','EXECUTING','SUCCESS','FAILURE','DUPLICATE') NOT NULL DEFAULT 'WAITING',
		last_modification_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
		PRIMARY KEY (ticket_id)
		)ENGINE = MyISAM;
CREATE TABLE IF NOT EXISTS project_27_import_registry
                (
		ticket_id BIGINT NOT NULL,
		request_time TIMESTAMP NOT NULL,
		request MEDIUMBLOB NOT NULL,
		job_status ENUM ('WAITING','EXPIRED','UPLOADING','QUEUED','EXECUTING','SUCCESS','FAILURE','DUPLICATE') NOT NULL DEFAULT 'WAITING',
		last_modification_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
		PRIMARY KEY (ticket_id)
		)ENGINE = MyISAM;
CREATE TABLE IF NOT EXISTS project_28_import_registry
                (
		ticket_id BIGINT NOT NULL,
		request_time TIMESTAMP NOT NULL,
		request MEDIUMBLOB NOT NULL,
		job_status ENUM ('WAITING','EXPIRED','UPLOADING','QUEUED','EXECUTING','SUCCESS','FAILURE','DUPLICATE') NOT NULL DEFAULT 'WAITING',
		last_modification_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
		PRIMARY KEY (ticket_id)
		)ENGINE = MyISAM;
CREATE TABLE IF NOT EXISTS project_29_import_registry
                (
		ticket_id BIGINT NOT NULL,
		request_time TIMESTAMP NOT NULL,
		request MEDIUMBLOB NOT NULL,
		job_status ENUM ('WAITING','EXPIRED','UPLOADING','QUEUED','EXECUTING','SUCCESS','FAILURE','DUPLICATE') NOT NULL DEFAULT 'WAITING',
		last_modification_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
		PRIMARY KEY (ticket_id)
		)ENGINE = MyISAM;
CREATE TABLE IF NOT EXISTS project_30_import_registry
                (
		ticket_id BIGINT NOT NULL,
		request_time TIMESTAMP NOT NULL,
		request MEDIUMBLOB NOT NULL,
		job_status ENUM ('WAITING','EXPIRED','UPLOADING','QUEUED','EXECUTING','SUCCESS','FAILURE','DUPLICATE') NOT NULL DEFAULT 'WAITING',
		last_modification_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
		PRIMARY KEY (ticket_id)
		)ENGINE = MyISAM;
CREATE TABLE IF NOT EXISTS project_31_import_registry
                (
		ticket_id BIGINT NOT NULL,
		request_time TIMESTAMP NOT NULL,
		request MEDIUMBLOB NOT NULL,
		job_status ENUM ('WAITING','EXPIRED','UPLOADING','QUEUED','EXECUTING','SUCCESS','FAILURE','DUPLICATE') NOT NULL DEFAULT 'WAITING',
		last_modification_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
		PRIMARY KEY (ticket_id)
		)ENGINE = MyISAM;
CREATE TABLE IF NOT EXISTS project_32_import_registry
                (
		ticket_id BIGINT NOT NULL,
		request_time TIMESTAMP NOT NULL,
		request MEDIUMBLOB NOT NULL,
		job_status ENUM ('WAITING','EXPIRED','UPLOADING','QUEUED','EXECUTING','SUCCESS','FAILURE','DUPLICATE') NOT NULL DEFAULT 'WAITING',
		last_modification_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
		PRIMARY KEY (ticket_id)
		)ENGINE = MyISAM;
CREATE TABLE IF NOT EXISTS project_33_import_registry
                (
		ticket_id BIGINT NOT NULL,
		request_time TIMESTAMP NOT NULL,
		request MEDIUMBLOB NOT NULL,
		job_status ENUM ('WAITING','EXPIRED','UPLOADING','QUEUED','EXECUTING','SUCCESS','FAILURE','DUPLICATE') NOT NULL DEFAULT 'WAITING',
		last_modification_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
		PRIMARY KEY (ticket_id)
		)ENGINE = MyISAM;
CREATE TABLE IF NOT EXISTS project_34_import_registry
                (
		ticket_id BIGINT NOT NULL,
		request_time TIMESTAMP NOT NULL,
		request MEDIUMBLOB NOT NULL,
		job_status ENUM ('WAITING','EXPIRED','UPLOADING','QUEUED','EXECUTING','SUCCESS','FAILURE','DUPLICATE') NOT NULL DEFAULT 'WAITING',
		last_modification_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
		PRIMARY KEY (ticket_id)
		)ENGINE = MyISAM;
CREATE TABLE IF NOT EXISTS project_35_import_registry
                (
		ticket_id BIGINT NOT NULL,
		request_time TIMESTAMP NOT NULL,
		request MEDIUMBLOB NOT NULL,
		job_status ENUM ('WAITING','EXPIRED','UPLOADING','QUEUED','EXECUTING','SUCCESS','FAILURE','DUPLICATE') NOT NULL DEFAULT 'WAITING',
		last_modification_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
		PRIMARY KEY (ticket_id)
		)ENGINE = MyISAM;
CREATE TABLE IF NOT EXISTS project_36_import_registry
                (
		ticket_id BIGINT NOT NULL,
		request_time TIMESTAMP NOT NULL,
		request MEDIUMBLOB NOT NULL,
		job_status ENUM ('WAITING','EXPIRED','UPLOADING','QUEUED','EXECUTING','SUCCESS','FAILURE','DUPLICATE') NOT NULL DEFAULT 'WAITING',
		last_modification_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
		PRIMARY KEY (ticket_id)
		)ENGINE = MyISAM;
CREATE TABLE IF NOT EXISTS project_37_import_registry
                (
		ticket_id BIGINT NOT NULL,
		request_time TIMESTAMP NOT NULL,
		request MEDIUMBLOB NOT NULL,
		job_status ENUM ('WAITING','EXPIRED','UPLOADING','QUEUED','EXECUTING','SUCCESS','FAILURE','DUPLICATE') NOT NULL DEFAULT 'WAITING',
		last_modification_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
		PRIMARY KEY (ticket_id)
		)ENGINE = MyISAM;
CREATE TABLE IF NOT EXISTS project_38_import_registry
                (
		ticket_id BIGINT NOT NULL,
		request_time TIMESTAMP NOT NULL,
		request MEDIUMBLOB NOT NULL,
		job_status ENUM ('WAITING','EXPIRED','UPLOADING','QUEUED','EXECUTING','SUCCESS','FAILURE','DUPLICATE') NOT NULL DEFAULT 'WAITING',
		last_modification_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
		PRIMARY KEY (ticket_id)
		)ENGINE = MyISAM;
