package edu.emory.cci.aiw.i2b2patientdataexport.i2b2.pdo;

import java.util.HashMap;

public class CustomNullHashMap<K, V> extends HashMap<K, V> {
	private final V missingKeyValue;

	public CustomNullHashMap(V missingKeyValue) {
		super();
		this.missingKeyValue = missingKeyValue;
	}

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
