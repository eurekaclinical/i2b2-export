---
-- #%L
-- i2b2 Patient Data Export Service
-- %%
-- Copyright (C) 2013 Emory University
-- %%
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
-- 
--      http://www.apache.org/licenses/LICENSE-2.0
-- 
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
-- #L%
---

---
-- Database setup for Oracle 9i
---

drop table i2b2_concepts cascade constraints;
drop table output_column_configurations cascade constraints;
drop table output_configurations cascade constraints;
drop table output_configurations_output_column_configurations cascade constraints;
drop sequence I2B2_CONCEPT_SEQ;
drop sequence OUTPUT_COL_CONFIG_SEQ;
drop sequence OUTPUT_CONFIG_SEQ;
create table i2b2_concepts (id number(19,0) not null, columnName varchar2(255 char), dimensionCode varchar2(255 char) not null, displayName varchar2(255 char), hasChildren varchar2(255 char), i2b2Key varchar2(255 char) not null, icd9 varchar2(255 char), isSynonym varchar2(255 char) not null, level number(10,0) not null, name varchar2(255 char), operator varchar2(255 char), tableName varchar2(255 char) not null, tooltip varchar2(255 char), xmlOrig CLOB, primary key (id));
create table output_column_configurations (id number(19,0) not null, aggregation number(10,0), columnName varchar2(255 char) not null, columnOrder number(10,0) not null, displayFormat number(10,0) not null, howMany number(10,0), includeTimeRange number(1,0), includeUnits number(1,0), i2b2Concept_id number(19,0), primary key (id));
create table output_configurations (id number(19,0) not null, missingValue varchar2(255 char), name varchar2(255 char), quoteChar varchar2(1 char), rowDimension number(10,0) not null, separator varchar2(1 char), username varchar2(255 char) not null, whitespaceReplacement varchar2(255 char), primary key (id), unique (username, name));
create table output_configurations_output_column_configurations (output_configurations_id number(19,0) not null, columnConfigs_id number(19,0) not null, unique (columnConfigs_id));
alter table output_column_configurations add constraint FK78A321884808884F foreign key (i2b2Concept_id) references i2b2_concepts;
alter table output_configurations_output_column_configurations add constraint FK17265E0C6501D153 foreign key (columnConfigs_id) references output_column_configurations;
alter table output_configurations_output_column_configurations add constraint FK17265E0C5DF9429 foreign key (output_configurations_id) references output_configurations;
create sequence I2B2_CONCEPT_SEQ;
create sequence OUTPUT_COL_CONFIG_SEQ;
create sequence OUTPUT_CONFIG_SEQ;