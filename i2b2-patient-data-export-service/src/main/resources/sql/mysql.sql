--
-- Database setup for MySQL 4.x and earlier
--

alter table output_column_configurations drop foreign key FK78A321884808884F;
alter table output_configurations_output_column_configurations drop foreign key FK17265E0C6501D153;
alter table output_configurations_output_column_configurations drop foreign key FK17265E0C5DF9429;
drop table if exists i2b2_concepts;
drop table if exists output_column_configurations;
drop table if exists output_configurations;
drop table if exists output_configurations_output_column_configurations;
create table i2b2_concepts (id bigint not null auto_increment, columnName varchar(255), dimensionCode varchar(255) not null, displayName varchar(255), hasChildren varchar(255), i2b2Key varchar(255) not null, icd9 varchar(255), isSynonym varchar(255) not null, level integer not null, name varchar(255), operator varchar(255), tableName varchar(255) not null, tooltip varchar(255), xmlOrig CLOB, primary key (id));
create table output_column_configurations (id bigint not null auto_increment, aggregation integer, columnName varchar(255) not null, columnOrder integer not null, displayFormat integer not null, howMany integer, includeTimeRange bit, includeUnits bit, i2b2Concept_id bigint, primary key (id));
create table output_configurations (id bigint not null auto_increment, missingValue varchar(255), name varchar(255), quoteChar varchar(1), rowDimension integer not null, separator varchar(1), username varchar(255) not null, whitespaceReplacement varchar(255), primary key (id), unique (username, name));
create table output_configurations_output_column_configurations (output_configurations_id bigint not null, columnConfigs_id bigint not null, unique (columnConfigs_id));
alter table output_column_configurations add index FK78A321884808884F (i2b2Concept_id), add constraint FK78A321884808884F foreign key (i2b2Concept_id) references i2b2_concepts (id);
alter table output_configurations_output_column_configurations add index FK17265E0C6501D153 (columnConfigs_id), add constraint FK17265E0C6501D153 foreign key (columnConfigs_id) references output_column_configurations (id);
alter table output_configurations_output_column_configurations add index FK17265E0C5DF9429 (output_configurations_id), add constraint FK17265E0C5DF9429 foreign key (output_configurations_id) references output_configurations (id);
