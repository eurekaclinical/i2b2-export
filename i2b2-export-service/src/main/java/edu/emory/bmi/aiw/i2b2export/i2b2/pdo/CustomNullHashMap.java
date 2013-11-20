package edu.emory.bmi.aiw.i2b2export.i2b2.pdo;

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

import java.util.HashMap;

/**
 * A hash map that will return a specified value if a key is not in the map.
 *
 * @param <K> the type of the map's keys
 * @param <V> the type of the map's values
 *
 * @author Michel Mansour
 * @since 1.0
 */
public class CustomNullHashMap<K, V> extends HashMap<K, V> {
	private final V missingKeyValue;

	/**
	 * Accepts the value that should be returned if {@link #get(Object)} is called with a key that the map does not contain.
	 *
	 * @param missingKeyValue the value that {@link #get(Object)} should return if a key is not in the map
	 */
	public CustomNullHashMap(V missingKeyValue) {
		super();
		this.missingKeyValue = missingKeyValue;
	}

	/**
	 * Default constructor. Sets the missing value to <code>null</code>.
	 */
	public CustomNullHashMap() {
		this(null);
	}

	@Override
	public V get(Object key) {
		if (containsKey(key)) {
			return super.get(key);
		} else {
			return missingKeyValue;
		}
	}
}
