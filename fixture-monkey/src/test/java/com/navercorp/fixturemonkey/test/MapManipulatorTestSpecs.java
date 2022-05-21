package com.navercorp.fixturemonkey.test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.navercorp.fixturemonkey.test.FixtureMonkeyV04TestSpecs.SimpleEnum;
import com.navercorp.fixturemonkey.test.FixtureMonkeyV04TestSpecs.SimpleObject;

public class MapManipulatorTestSpecs {
	@Setter
	@Getter
	public static class ComplexObject {
		// private String str;
		// private int integer;
		// private Long wrapperLong;
		// private List<String> strList;
		// private Set<String> strSet;
		// private FixtureMonkeyV04TestSpecs.SimpleEnum enumValue;
		// private LocalDateTime localDateTime;
		// private FixtureMonkeyV04TestSpecs.SimpleObject object;
		// private List<FixtureMonkeyV04TestSpecs.SimpleObject> list;
		// private Map<String, FixtureMonkeyV04TestSpecs.SimpleObject> map;
		// private Map.Entry<String, FixtureMonkeyV04TestSpecs.SimpleObject> mapEntry;

		private Map<String, Map<String, String>> strMapMap;

		@Override
		public String toString() {
			return "ComplexObject{" +
				"strMapMap=" + strMapMap +
				'}';
		}
	}

	public enum SimpleEnum {
		ENUM_1, ENUM_2, ENUM_3, ENUM_4
	}

	@Getter
	@Setter
	@EqualsAndHashCode
	public static class SimpleObject {
		private String str;
		private Optional<String> optionalString;
		private OptionalInt optionalInt;
		private OptionalLong optionalLong;
		private OptionalDouble optionalDouble;
		private Instant instant;
	}
}
