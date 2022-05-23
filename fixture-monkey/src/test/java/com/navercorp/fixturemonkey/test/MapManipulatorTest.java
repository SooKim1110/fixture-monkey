package com.navercorp.fixturemonkey.test;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jqwik.api.Property;

import com.navercorp.fixturemonkey.LabMonkey;
import com.navercorp.fixturemonkey.arbitrary.ArbitraryExpression;
import com.navercorp.fixturemonkey.arbitrary.ArbitraryExpression.Cursor;
import com.navercorp.fixturemonkey.test.MapManipulatorTestSpecs.MapObject;

public class MapManipulatorTest {
	private static final LabMonkey SUT = LabMonkey.labMonkey();

	@Property(tries = 1)
	void mapExpression() {
		String expression = "list[0].?.?";
		ArbitraryExpression ae = ArbitraryExpression.from(expression);
		for (Cursor c : ae.toCursors()){
			System.out.println(c.getName());
		}
	}

	@Property(tries = 1)
	void setMapKeyNestedValue() {
		Map<String, String> strMap = new HashMap<>();
		strMap.put("key1", "value1");

		Map<String, Map<String, String>> strMapMap = new HashMap<>();
		strMapMap.put("key2", strMap);

		List<String> key = new ArrayList<>();
		key.add("key2");
		key.add("key1");


		MapObject actual = SUT.giveMeBuilder(MapObject.class)
			.set("strKeyMap", strMapMap)
			.set("strKeyMap.?.?", key, "newValue")
			.sample();

		System.out.println(actual.getStrKeyMap().toString());
		then(actual.getStrKeyMap().get("key2").get("key1")).isEqualTo("newValue");
	}

	@Property(tries = 1)
	void setMapNestedKeyValue() {
		Map<String, String> keyMap = new HashMap<>();
		keyMap.put("key1", "value1");

		Map<Map<String, String>, String> mapKeyMap = new HashMap<>();
		mapKeyMap.put(keyMap, "value2");

		List<Object> key = new ArrayList<>();
		key.add(keyMap);


		MapObject actual = SUT.giveMeBuilder(MapObject.class)
			.set("mapKeyMap", mapKeyMap)
			.set("mapKeyMap.?", key, "newValue")
			.sample();

		System.out.println(actual.getMapKeyMap().toString());
		then(actual.getMapKeyMap().get(keyMap)).isEqualTo("newValue");
	}

	// @Property(tries = 1)
	// void setMapKeyNotExistMakesNewMapEntry() {
	// 	Map<String, String> keyMap = new HashMap<>();
	// 	keyMap.put("key1", "value1");
	//
	// 	List<Object> key = new ArrayList<>();
	// 	key.add(keyMap);
	//
	//
	// 	MapObject actual = SUT.giveMeBuilder(MapObject.class)
	// 		.set("mapKeyMap.?", key, "newValue")
	// 		.sample();
	//
	// 	System.out.println(actual.getMapKeyMap().toString());
	// 	System.out.println(actual.getMapKeyMap().size());
	// }
	//
	// @Property(tries = 1)
	// void setSimpleMapKeyNotExistMakesNewMapEntry() {
	// 	List<Object> key = new ArrayList<>();
	// 	key.add("key");
	//
	//
	// 	MapObject actual = SUT.giveMeBuilder(MapObject.class)
	// 		.size("strMap", 5)
	// 		.set("strMap.?", key, "val")
	// 		.sample();
	//
	// 	System.out.println(actual.getStrMap().toString());
	// 	System.out.println(actual.getStrMap().size());
	// }
}
