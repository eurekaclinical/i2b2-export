---
-- #%L
-- i2b2 Export Service
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
alter table column_configs drop constraint FK8CF9A98899BE0907;
alter table column_configs drop constraint FK8CF9A9887A6ACF61;
drop table column_configs;
drop table i2b2_concepts;
drop table output_configs;
create table column_configs (column_config_id numeric(19,0) identity not null, aggregation int null, column_name varchar(255) not null, column_order int not null, display_format int not null, how_many int null, include_time_range tinyint null, include_units tinyint null, i2b2_concept_id numeric(19,0) null, config_id numeric(19,0) null, primary key (column_config_id));
create table i2b2_concepts (concept_id numeric(19,0) identity not null, c_columnname varchar(255) null, c_dimcode varchar(255) not null, c_displayname varchar(255) null, c_children varchar(255) null, i2b2_key varchar(255) not null, c_icd9 varchar(255) null, c_synonym_cd varchar(255) not null, c_hlevel int not null, c_name varchar(255) null, c_operator varchar(255) null, c_tablename varchar(255) not null, c_tooltip varchar(255) null, c_xml_orig CLOB null, primary key (concept_id));
create table output_configs (config_id numeric(19,0) identity not null, missing_value varchar(255) null, config_name varchar(255) null, quote_char varchar(1) null, row_dimension int not null, separator varchar(1) null, username varchar(255) not null, whitespace_replacement varchar(255) null, primary key (config_id), unique (username, config_name));
alter table column_configs add constraint FK8CF9A98899BE0907 foreign key (config_id) references output_configs;
alter table column_configs add constraint FK8CF9A9887A6ACF61 foreign key (i2b2_concept_id) references i2b2_concepts;
