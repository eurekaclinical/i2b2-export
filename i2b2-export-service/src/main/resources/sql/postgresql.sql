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
drop table column_configs cascade;
drop table i2b2_concepts cascade;
drop table output_configs cascade;
drop sequence I2B2_CONCEPT_SEQ;
drop sequence OUTPUT_COL_CONFIG_SEQ;
drop sequence OUTPUT_CONFIG_SEQ;
create table column_configs (column_config_id int8 not null, aggregation int4, column_name varchar(255) not null, column_order int4 not null, display_format int4 not null, how_many int4, include_time_range bool, include_units bool, i2b2_concept_id int8, config_id int8, primary key (column_config_id));
create table i2b2_concepts (concept_id int8 not null, c_columnname varchar(255), c_dimcode varchar(255) not null, c_displayname varchar(255), c_children varchar(255), i2b2_key varchar(255) not null, c_icd9 varchar(255), c_synonym_cd varchar(255) not null, c_hlevel int4 not null, c_name varchar(255), c_operator varchar(255), c_tablename varchar(255) not null, c_tooltip varchar(255), c_xml_orig CLOB, primary key (concept_id));
create table output_configs (config_id int8 not null, missing_value varchar(255), config_name varchar(255), quote_char varchar(1), row_dimension int4 not null, separator varchar(1), username varchar(255) not null, whitespace_replacement varchar(255), primary key (config_id), unique (username, config_name));
alter table column_configs add constraint FK8CF9A98899BE0907 foreign key (config_id) references output_configs;
alter table column_configs add constraint FK8CF9A9887A6ACF61 foreign key (i2b2_concept_id) references i2b2_concepts;
create sequence I2B2_CONCEPT_SEQ;
create sequence OUTPUT_COL_CONFIG_SEQ;
create sequence OUTPUT_CONFIG_SEQ;
