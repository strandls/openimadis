use imagedb;
alter table acquisition_profile change column time_unit elapsed_time_unit ENUM ('SECONDS', 'MILISECONDS', 'MICROSECONDS', 'NANOSECONDS') DEFAULT 'MILISECONDS';
alter table acquisition_profile add column exposure_time_unit ENUM ('SECONDS', 'MILISECONDS', 'MICROSECONDS', 'NANOSECONDS') DEFAULT 'MILISECONDS';