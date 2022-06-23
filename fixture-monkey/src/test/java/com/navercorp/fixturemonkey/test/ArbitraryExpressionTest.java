package com.navercorp.fixturemonkey.test;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

import net.jqwik.api.Property;

import com.navercorp.fixturemonkey.arbitrary.ArbitraryExpression;
import com.navercorp.fixturemonkey.resolver.NodeFieldResolver;
import com.navercorp.fixturemonkey.resolver.NodeIndexResolver;
import com.navercorp.fixturemonkey.resolver.NodeKeyValueResolver;
import com.navercorp.fixturemonkey.resolver.NodeResolver;

public class ArbitraryExpressionTest {
	@Test
	void appendLeft() {
		ArbitraryExpression arbitraryExpression = ArbitraryExpression.from("fixturemonkey");

		ArbitraryExpression actual = arbitraryExpression.addFirst("navercorp");

		then(actual.toString()).isEqualTo("navercorp.fixturemonkey");
	}

	@Test
	void appendRight() {
		ArbitraryExpression arbitraryExpression = ArbitraryExpression.from("navercorp");

		ArbitraryExpression actual = arbitraryExpression.addLast("fixturemonkey");

		then(actual.toString()).isEqualTo("navercorp.fixturemonkey");
	}

	@Property
	void fieldToNodeResolver() {
		ArbitraryExpression arbitraryExpression = ArbitraryExpression.from("navercorp");

		NodeResolver actual = arbitraryExpression.toNodeResolver();

		then(actual).isInstanceOf(NodeFieldResolver.class);
	}

	@Property
	void indexToNodeResolver() {
		ArbitraryExpression arbitraryExpression = ArbitraryExpression.from("navercorp[0]");

		NodeResolver actual = arbitraryExpression.toNodeResolver();

		then(actual).isInstanceOf(NodeIndexResolver.class);
	}

	@Property
	void allToNodeResolver() {
		ArbitraryExpression arbitraryExpression = ArbitraryExpression.from("navercorp[*]");

		NodeResolver actual = arbitraryExpression.toNodeResolver();

		then(actual).isInstanceOf(NodeIndexResolver.class);
	}

	@Property
	void keyToNodeResolver() {
		ArbitraryExpression arbitraryExpression = ArbitraryExpression.from("navercorp[key]");

		NodeResolver actual = arbitraryExpression.toNodeResolver();

		then(actual).isInstanceOf(NodeKeyValueResolver.class);
	}


	// @Test
	// void popRight() {
	// 	ArbitraryExpression arbitraryExpression = ArbitraryExpression.from("navercorp.fixturemonkey");
	//
	// 	ArbitraryExpression actual = arbitraryExpression.pollLast();
	//
	// 	then(actual.toString()).isEqualTo("navercorp");
	// }
	//
	// @Test
	// void popRightWhenEmpty() {
	// 	ArbitraryExpression arbitraryExpression = ArbitraryExpression.from("");
	//
	// 	ArbitraryExpression actual = arbitraryExpression.pollLast();
	//
	// 	then(actual.toString()).isEqualTo("");
	// }
	//
	// @Test
	// void popRightNotAffectsOrigin() {
	// 	ArbitraryExpression arbitraryExpression = ArbitraryExpression.from("navercorp.fixturemonkey");
	//
	// 	arbitraryExpression.pollLast();
	//
	// 	then(arbitraryExpression.toString()).isEqualTo("navercorp.fixturemonkey");
	// }
}
