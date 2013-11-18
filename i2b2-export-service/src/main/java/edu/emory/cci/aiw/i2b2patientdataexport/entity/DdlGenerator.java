package edu.emory.cci.aiw.i2b2patientdataexport.entity;

/*
 * #%L
 * i2b2 Patient Data Export Service
 * %%
 * Copyright (C) 2013 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

/**
 * Generates a DDL for the entities used by this project. The generator can be
 * run via this class's main method, which accepts two parameters: the name of
 * the Hibernate dialect to generate the DDL for, and the location of the output
 * file.
 */
final class DdlGenerator {

	/**
	 * The class level logger.
	 */
	private static final Logger LOGGER =
			LoggerFactory.getLogger(DdlGenerator.class);

	/**
	 * Prevent the utility class from being instantiated.
	 */
	private DdlGenerator() {
		// prevent instantiation.
	}

	/**
	 * Given a list of classes annotated with the {@link javax.persistence.Entity}
	 * annotation, a Hibernate database dialect, and an output file name, generates
	 * the SQL needed to persist the given entities to a database and writes the
	 * SQL out to the given file.
	 *
	 * @param classes A list of classes to generate SQL for.
	 * @param outFile The location of the output file.
	 * @param dialect The name of the Hibernate dialect to use for the DDL.
	 * */
	private static void generate(List<Class<?>> classes, String outFile, String dialect) {
		final Configuration configuration = new Configuration();
		configuration.setProperty("hibernate.dialect", dialect);
		configuration.setProperty("hibernate.hbm2ddl.auto", "create");

		for (Class<?> c : classes) {
			Entity annotation = c.getAnnotation(Entity.class);
			if (annotation != null) {
				configuration.addAnnotatedClass(c);
			} else {
				LOGGER.warn("{} is not annotated with @Entity", c.getName());
			}
		}

		SchemaExport export = new SchemaExport(configuration);
		export.setDelimiter(";");
		export.setOutputFile(outFile);
		export.execute(true, false, false, false);
	}

	private static void generateDdl(String outputFile, String dialect) {
		final List<Class<?>> classes = new ArrayList<Class<?>>();
		classes.add(I2b2Concept.class);
		classes.add(OutputColumnConfiguration.class);
		classes.add(OutputConfiguration.class);

		generate(classes, outputFile, dialect);
	}

	public static void main(String[] args) {
		if (args.length < 2) {
			System.err.println("Usage: java DdlGenerator <hibernate-dialect> <output-file>");
			System.exit(1);
		} else {
			generateDdl(args[1], args[0]);
		}
	}
}
