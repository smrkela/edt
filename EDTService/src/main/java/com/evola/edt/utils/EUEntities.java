package com.evola.edt.utils;

import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

import com.evola.edt.model.BaseEntity;

public class EUEntities {

	// set kombinacija klasa i polja koja nisu definisana, ovako izbegavamo
	// exception-e
	private static Set<String> noFieldsSet = new HashSet<String>();

	// set kombinacija polja koja su definisana, ovako izbegavamo refleksiju
	private static Set<String> yesFieldsSet = new HashSet<String>();

	public static Long getId(BaseEntity entity) {

		Long id = null;

		if (entity != null)
			id = entity.getId();

		return id;
	}

	public static Class<? extends BaseEntity> getEntityClass(String entityName) {

		Class<? extends BaseEntity> entityClass = null;

		try {

			Class<?> clazz = Class.forName("com.evola.edt.model." + entityName);

			if (BaseEntity.class.isAssignableFrom(clazz))
				entityClass = clazz.asSubclass(BaseEntity.class);

		} catch (ClassNotFoundException e) {

			throw new RuntimeException(e.getMessage());
		}

		return entityClass;
	}

	public static boolean isEntityClass(String entityName) {

		boolean isEntity = false;

		try {

			Class<?> clazz = Class.forName("com.evola.edt.model." + entityName);

			if (BaseEntity.class.isAssignableFrom(clazz))
				isEntity = true;

		} catch (ClassNotFoundException e) {

		}

		return isEntity;
	}

	@SuppressWarnings("rawtypes")
	public static List<Map<String, Object>> createMockMapList(Collection baseEntities, EUMockMapper mapper) {

		List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();

		try {

			if (baseEntities != null) {

				for (Object o : baseEntities) {

					list.add(createMockMap((BaseEntity) o, mapper));
				}
			}

		} catch (Exception e) {

			throw new RuntimeException(e.getMessage());
		}

		return list;
	}

	public static Map<String, Object> createMockMap(BaseEntity entity, EUMockMapper mapper) {

		Map<String, Object> data = new HashMap<String, Object>();

		for (String key : mapper.getKeys()) {

			data.put(key, resolveChainedValue(entity, mapper.getPropertyChainArray(key)));
		}

		return data;
	}

	public static Object resolveChainedValue(Object object, String propertyChain) {

		String[] chain = propertyChain.split("\\.");

		return resolveChainedValue(object, chain);
	}

	public static Object resolveFieldValue(Object object, String fieldName) {

		if (object == null || fieldName == null) {
			return null;
		}

		if (hasNoField(object, fieldName))
			return null;

		try {

			return PropertyUtils.getProperty(object, fieldName);

		} catch (NoSuchMethodException nsme) {

			addNoField(object, fieldName);

			return null;

		} catch (Exception e) {

			throw new java.lang.RuntimeException(e);
		}
	}

	@SuppressWarnings("rawtypes")
	public static boolean hasProperty(Class clazz, String fieldName) {

		if (hasNoField(clazz, fieldName))
			return false;

		if (hasYesField(clazz, fieldName))
			return true;

		PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(clazz);

		boolean hasProperty = false;

		for (PropertyDescriptor descriptor : propertyDescriptors) {

			if (descriptor.getName().equals(fieldName)) {

				hasProperty = true;
				break;
			}
		}

		if (hasProperty)
			addYesField(clazz, fieldName);
		else
			addNoField(clazz, fieldName);

		return hasProperty;
	}

	private static boolean hasNoField(Object object, String fieldName) {

		return hasNoField(object.getClass(), fieldName);
	}

	private static boolean hasNoField(Class<?> clazz, String fieldName) {

		String key = clazz.getName() + "###" + fieldName;

		return noFieldsSet.contains(key);
	}

	private static boolean hasYesField(Class<?> clazz, String fieldName) {

		String key = clazz.getName() + "###" + fieldName;

		return yesFieldsSet.contains(key);
	}

	private static void addNoField(Object object, String fieldName) {

		addNoField(object.getClass(), fieldName);
	}

	private static void addNoField(Class<?> clazz, String fieldName) {

		String key = clazz.getName() + "###" + fieldName;

		noFieldsSet.add(key);
	}

	private static void addYesField(Class<?> clazz, String fieldName) {

		String key = clazz.getName() + "###" + fieldName;

		yesFieldsSet.add(key);
	}

	private static Object resolveChainedValue(Object entity, String[] propertyChain) {

		Object currentValue = entity;

		for (String property : propertyChain) {

			if (currentValue == null)
				break;

			try {

				currentValue = PropertyUtils.getProperty(currentValue, property);

			} catch (Exception e) {

				throw new java.lang.RuntimeException(e);
			}
		}

		return currentValue;
	}

	public static boolean containsEntity(Set<? extends BaseEntity> items, BaseEntity targetEntity) {

		if (items == null || targetEntity == null)
			return false;

		for (BaseEntity e : items) {

			if (e.equals(targetEntity))
				return true;
		}

		return false;
	}

	public static BaseEntity initializeAssociation(BaseEntity entity) {

		if (entity == null) {
			return null;
		}

		if (entity instanceof HibernateProxy) {

			Hibernate.initialize(entity);

			entity = (BaseEntity) ((HibernateProxy) entity).getHibernateLazyInitializer().getImplementation();
		}

		return entity;
	}

	public static void initialize(BaseEntity entity, String... propertyChains) {

		if (entity == null || propertyChains == null)
			return;

		try {

			for (String propertyChain : propertyChains) {

				Object value = resolveChainedValue(entity, propertyChain);

				if (value instanceof BaseEntity)
					initializeAssociation((BaseEntity) value);
				else
					Hibernate.initialize(value);
			}

		} catch (Exception e) {

			e.printStackTrace();

			throw new RuntimeException(e.getMessage());
		}
	}

	public static Boolean areEqual(BaseEntity entity1, BaseEntity entity2, Boolean ifBothNull) {

		if (entity1 == null && entity2 == null)
			return ifBothNull;

		if (entity1 == null && entity2 != null)
			return false;

		return entity1.equals(entity2);
	}

	public static Boolean areNotEqual(BaseEntity entity1, BaseEntity entity2, Boolean ifBothNull) {

		return !areEqual(entity1, entity2, !ifBothNull);
	}
}
