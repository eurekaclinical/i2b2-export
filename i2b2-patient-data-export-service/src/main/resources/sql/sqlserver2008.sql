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
-- Database setup for SQL Server 2008
---

alter table output_column_configurations drop constraint FK78A321884808884F;
alter table output_configurations_output_column_configurations drop constraint FK17265E0C6501D153;
alter table output_configurations_output_column_configurations drop constraint FK17265E0C5DF9429;
drop table i2b2_concepts;
drop table output_column_configurations;
drop table output_configurations;
drop table output_configurations_output_column_configurations;
create table i2b2_concepts (id bigint identity not null, columnName varchar(255) null, dimensionCode varchar(255) not null, displayName varchar(255) null, hasChildren varchar(255) null, i2b2Key varchar(255) not null, icd9 varchar(255) null, isSynonym varchar(255) not null, level int not null, name varchar(255) null, operator varchar(255) null, tableName varchar(255) not null, tooltip varchar(255) null, xmlOrig CLOB null, primary key (id));
create table output_column_configurations (id bigint identity not null, aggregation int null, columnName varchar(255) not null, columnOrder int not null, displayFormat int not null, howMany int null, includeTimeRange bit null, includeUnits bit null, i2b2Concept_id bigint null, primary key (id));
create table output_configurations (id bigint identity not null, missingValue varchar(255) null, name varchar(255) null, quoteChar varchar(1) null, rowDimension int not null, separator varchar(1) null, username varchar(255) not null, whitespaceReplacement varchar(255) null, primary key (id), unique (username, name));
create table output_configurations_output_column_configurations (output_configurations_id bigint not null, columnConfigs_id bigint not null, unique (columnConfigs_id));
alter table output_column_configurations add constraint FK78A321884808884F foreign key (i2b2Concept_id) references i2b2_concepts;
alter table output_configurations_output_column_configurations add constraint FK17265E0C6501D153 foreign key (columnConfigs_id) references output_column_configurations;
alter table output_configurations_output_column_configurations add constraint FK17265E0C5DF9429 foreign key (output_configurations_id) references output_configurations;
