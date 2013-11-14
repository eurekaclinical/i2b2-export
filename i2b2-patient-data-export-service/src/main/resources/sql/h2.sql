--
-- Database setup for H2
--

alter table output_column_configurations drop constraint FK78A321884808884F;
alter table output_configurations_output_column_configurations drop constraint FK17265E0C6501D153;
alter table output_configurations_output_column_configurations drop constraint FK17265E0C5DF9429;
drop table i2b2_concepts if exists;
drop table output_column_configurations if exists;
drop table output_configurations if exists;
drop table output_configurations_output_column_configurations if exists;
drop sequence I2B2_CONCEPT_SEQ;
drop sequence OUTPUT_COL_CONFIG_SEQ;
drop sequence OUTPUT_CONFIG_SEQ;
create table i2b2_concepts (id bigint not null, columnName varchar(255), dimensionCode varchar(255) not null, displayName varchar(255), hasChildren varchar(255), i2b2Key varchar(255) not null, icd9 varchar(255), isSynonym varchar(255) not null, level integer not null, name varchar(255), operator varchar(255), tableName varchar(255) not null, tooltip varchar(255), xmlOrig CLOB, primary key (id));
create table output_column_configurations (id bigint not null, aggregation integer, columnName varchar(255) not null, columnOrder integer not null, displayFormat integer not null, howMany integer, includeTimeRange boolean, includeUnits boolean, i2b2Concept_id bigint, primary key (id));
create table output_configurations (id bigint not null, missingValue varchar(255), name varchar(255), quoteChar varchar(1), rowDimension integer not null, separator varchar(1), username varchar(255) not null, whitespaceReplacement varchar(255), primary key (id), unique (username, name));
create table output_configurations_output_column_configurations (output_configurations_id bigint not null, columnConfigs_id bigint not null, unique (columnConfigs_id));
alter table output_column_configurations add constraint FK78A321884808884F foreign key (i2b2Concept_id) references i2b2_concepts;
alter table output_configurations_output_column_configurations add constraint FK17265E0C6501D153 foreign key (columnConfigs_id) references output_column_configurations;
alter table output_configurations_output_column_configurations add constraint FK17265E0C5DF9429 foreign key (output_configurations_id) references output_configurations;
create sequence I2B2_CONCEPT_SEQ;
create sequence OUTPUT_COL_CONFIG_SEQ;
create sequence OUTPUT_CONFIG_SEQ;
