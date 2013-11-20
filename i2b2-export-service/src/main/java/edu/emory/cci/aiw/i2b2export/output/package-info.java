/*
 * #%L
 * i2b2 Export Service
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

/**
 * Consists of classes for formatting results from i2b2 into CSV files. Formatting depends on whether the
 * rows represents patients ({@link edu.emory.cci.aiw.i2b2export.output.PatientDataOutputFormatter}), visits
 * ({@link edu.emory.cci.aiw.i2b2export.output.VisitDataRowOutputFormatter}), or providers
 * ({@link edu.emory.cci.aiw.i2b2export.output.ProviderDataOutputFormatter}). Header rows are also formatted
 * invidually by {@link edu.emory.cci.aiw.i2b2export.output.HeaderRowOutputFormatter}.
 *
 * In addition, each column has a format configuration that must be applied. The column format depends on the display
 * type of the column: existence ({@link edu.emory.cci.aiw.i2b2export.output.ExistenceColumnOutputFormatter}),
 * value ({@link edu.emory.cci.aiw.i2b2export.output.ValueColumnOutputFormatter}), or aggregation
 * ({@link edu.emory.cci.aiw.i2b2export.output.AggregationColumnOutputFormatter}).
 *
 * @since 1.0
 */
package edu.emory.cci.aiw.i2b2export.output;
