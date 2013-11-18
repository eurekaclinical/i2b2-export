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
-- Database setup for Oracle 10g
---

drop table column_configs cascade constraints;
drop table i2b2_concepts cascade constraints;
drop table output_configs cascade constraints;
drop sequence I2B2_CONCEPT_SEQ;
drop sequence OUTPUT_COL_CONFIG_SEQ;
drop sequence OUTPUT_CONFIG_SEQ;
create table column_configs (column_config_id number(19,0) not null, aggregation number(10,0), column_name varchar2(255 char) not null, column_order number(10,0) not null, display_format number(10,0) not null, how_many number(10,0), include_time_range number(1,0), include_units number(1,0), i2b2_concept_id number(19,0), config_id number(19,0), primary key (column_config_id));
create table i2b2_concepts (concept_id number(19,0) not null, c_columnname varchar2(255 char), c_dimcode varchar2(255 char) not null, c_displayname varchar2(255 char), c_children varchar2(255 char), i2b2_key varchar2(255 char) not null, c_icd9 varchar2(255 char), c_synonym_cd varchar2(255 char) not null, c_hlevel number(10,0) not null, c_name varchar2(255 char), c_operator varchar2(255 char), c_tablename varchar2(255 char) not null, c_tooltip varchar2(255 char), c_xml_orig CLOB, primary key (concept_id));
create table output_configs (config_id number(19,0) not null, missing_value varchar2(255 char), config_name varchar2(255 char), quote_char varchar2(1 char), row_dimension number(10,0) not null, separator varchar2(1 char), username varchar2(255 char) not null, whitespace_replacement varchar2(255 char), primary key (config_id), unique (username, config_name));
alter table column_configs add constraint FK8CF9A9881EA8BCC2 foreign key (config_id) references output_configs;
alter table column_configs add constraint FK8CF9A988FD66E1C foreign key (i2b2_concept_id) references i2b2_concepts;
create sequence I2B2_CONCEPT_SEQ;
create sequence OUTPUT_COL_CONFIG_SEQ;
create sequence OUTPUT_CONFIG_SEQ;
