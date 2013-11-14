--
-- Database setup for Oracle 8i
--

drop table i2b2_concepts cascade constraints;
drop table output_column_configurations cascade constraints;
drop table output_configurations cascade constraints;
drop table output_configurations_output_column_configurations cascade constraints;
drop sequence I2B2_CONCEPT_SEQ;
drop sequence OUTPUT_COL_CONFIG_SEQ;
drop sequence OUTPUT_CONFIG_SEQ;
create table i2b2_concepts (id number(19,0) not null, columnName varchar2(255), dimensionCode varchar2(255) not null, displayName varchar2(255), hasChildren varchar2(255), i2b2Key varchar2(255) not null, icd9 varchar2(255), isSynonym varchar2(255) not null, level number(10,0) not null, name varchar2(255), operator varchar2(255), tableName varchar2(255) not null, tooltip varchar2(255), xmlOrig CLOB, primary key (id));
create table output_column_configurations (id number(19,0) not null, aggregation number(10,0), columnName varchar2(255) not null, columnOrder number(10,0) not null, displayFormat number(10,0) not null, howMany number(10,0), includeTimeRange number(1,0), includeUnits number(1,0), i2b2Concept_id number(19,0), primary key (id));
create table output_configurations (id number(19,0) not null, missingValue varchar2(255), name varchar2(255), quoteChar varchar2(1), rowDimension number(10,0) not null, separator varchar2(1), username varchar2(255) not null, whitespaceReplacement varchar2(255), primary key (id), unique (username, name));
create table output_configurations_output_column_configurations (output_configurations_id number(19,0) not null, columnConfigs_id number(19,0) not null, unique (columnConfigs_id));
alter table output_column_configurations add constraint FK78A321884808884F foreign key (i2b2Concept_id) references i2b2_concepts;
alter table output_configurations_output_column_configurations add constraint FK17265E0C6501D153 foreign key (columnConfigs_id) references output_column_configurations;
alter table output_configurations_output_column_configurations add constraint FK17265E0C5DF9429 foreign key (output_configurations_id) references output_configurations;
create sequence I2B2_CONCEPT_SEQ;
create sequence OUTPUT_COL_CONFIG_SEQ;
create sequence OUTPUT_CONFIG_SEQ;
