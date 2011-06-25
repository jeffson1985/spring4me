package org.osforce.spring4me.commons.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.2.2
 * @create Jun 19, 2011 - 11:05:33 AM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
public final class CollectionUtil {
	
	private CollectionUtil() {
		throw new AssertionError();
	}

	public static <K, V> Map<K, V> newHashMap() {
		return new HashMap<K, V>();
	}
	
	public static <K, V> Map<K, V> newHashMap(Map<K, V> map) {
		return new HashMap<K, V>(map);
	}
	
	public static <K, V> Map<K, V> newTreeMap() {
		return new TreeMap<K, V>();
	}
	
	public static <K, V> Map<K, V> newTreeMap(Map<K, V> map) {
		return new TreeMap<K, V>(map);
	}
	
	public static <K, V> Map<K, V> newTreeMap(Comparator<? super K> comparator) {
		return new TreeMap<K, V>(comparator);
	}
	
	public static <K, V> Map<K, V> newTreeMap(Map<K, V> map, Comparator<? super K> comparator) {
		return new TreeMap<K, V>(map);
	}
	
	public static <T> List<T> newArrayList() {
		return new ArrayList<T>();
	}
	
	public static <T> List<T> newArrayList(Collection<? extends T> collection) {
		return new ArrayList<T>(collection);
	}
	
	public static <T> List<T> newLinkedList() {
		return new LinkedList<T>();
	}
	
	public static <T> List<T> newLinkedList(Collection<? extends T> collection) {
		return new LinkedList<T>(collection);
	}
	
}
