package com.evola.edt.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.evola.edt.model.BaseEntity;

public class EUMockMapper {

	public static final EUMockMapper BASIC = new EUMockMapper().put("id").put("name");

	public Map<String, String[]> propertyMappings;

	public EUMockMapper() {

		propertyMappings = new HashMap<String, String[]>();
	}

	public Set<String> getKeys() {

		return propertyMappings.keySet();
	}

	public String[] getPropertyChainArray(String key) {

		return propertyMappings.get(key);
	}

	public EUMockMapper put(String keyAndPropertyChain) {

		return put(keyAndPropertyChain, keyAndPropertyChain);
	}

	public EUMockMapper put(String key, String propertyChain) {

		String[] chain = propertyChain.split("\\.");

		propertyMappings.put(key, chain);

		return this;
	}

	@SuppressWarnings("rawtypes")
	public List<Map<String, Object>> map(Collection entities) {

		List<Map<String, Object>> list = EUEntities.createMockMapList(entities, this);

		return list;
	}

	public Map<String, Object> map(BaseEntity entity) {

		if (entity == null)
			return null;

		Map<String, Object> map = null;

		try {

			map = EUEntities.createMockMap(entity, this);

		} catch (Exception e) {

			e.printStackTrace();

			throw new RuntimeException(e.getMessage());
		}

		return map;
	}
}
