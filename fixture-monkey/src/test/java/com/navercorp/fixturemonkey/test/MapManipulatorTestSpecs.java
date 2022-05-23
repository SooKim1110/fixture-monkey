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
	public static class MapObject {

		private Map<String, Map<String, String>> strKeyMap;
		private Map<Map<String, String>, String> mapKeyMap;
		private Map<String, String> strMap;
		private Map<List<String>, String> listMap;

		@Override
		public String toString() {
			return "MapObject{" +
				"strKeyMap=" + strKeyMap +
				", mapKeyMap=" + mapKeyMap +
				", strMap=" + strMap +
				'}';
		}


	}
}
