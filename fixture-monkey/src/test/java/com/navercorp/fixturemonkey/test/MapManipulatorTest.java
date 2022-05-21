package com.navercorp.fixturemonkey.test;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Property;

import com.navercorp.fixturemonkey.LabMonkey;
import com.navercorp.fixturemonkey.arbitrary.ArbitraryExpression;
import com.navercorp.fixturemonkey.arbitrary.ArbitraryExpression.Cursor;
import com.navercorp.fixturemonkey.test.MapManipulatorTestSpecs.ComplexObject;

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
	void setNestedMapKeyValue() {
		Map<String, String> strMap = new HashMap<>();
		strMap.put("key1", "value1");

		Map<String, Map<String, String>> strMapMap = new HashMap<>();
		strMapMap.put("key2", strMap);

		List<String> key = new ArrayList<>();
		key.add("key2");
		key.add("key1");

		ComplexObject actual = SUT.giveMeBuilder(ComplexObject.class)
			.set("strMapMap", strMapMap)
			.set("strMapMap.?.?", key, "newVal")
			.sample();

		System.out.println(actual.getStrMapMap().toString());
		then(actual.getStrMapMap()).isEqualTo(strMapMap);
	}
}
