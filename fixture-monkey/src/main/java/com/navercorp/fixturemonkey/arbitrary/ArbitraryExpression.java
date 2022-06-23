/*
 * Fixture Monkey
 *
 * Copyright (c) 2021-present NAVER Corp.
 *
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
 */

package com.navercorp.fixturemonkey.arbitrary;

import static com.navercorp.fixturemonkey.Constants.ALL_INDEX_STRING;
import static com.navercorp.fixturemonkey.Constants.HEAD_NAME;
import static com.navercorp.fixturemonkey.Constants.NO_OR_ALL_INDEX_INTEGER_VALUE;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.navercorp.fixturemonkey.api.generator.ArbitraryProperty;
import com.navercorp.fixturemonkey.customizer.ContainerSpec;
import com.navercorp.fixturemonkey.resolver.ArbitraryManipulator;
import com.navercorp.fixturemonkey.resolver.ArbitraryTraverser;
import com.navercorp.fixturemonkey.resolver.ExpressionNodeResolver;
import com.navercorp.fixturemonkey.resolver.NodeFieldResolver;
import com.navercorp.fixturemonkey.resolver.NodeIndexResolver;
import com.navercorp.fixturemonkey.resolver.NodeKeyValueResolver;
import com.navercorp.fixturemonkey.resolver.NodeResolver;

public final class ArbitraryExpression implements Comparable<ArbitraryExpression> {
	private final List<Exp> expList;

	private ArbitraryExpression(List<Exp> expList) {
		this.expList = expList;
	}

	private ArbitraryExpression(String expression) {
		expList = Arrays.stream(expression.split("\\."))
			.map(Exp::new)
			.collect(toList());
	}

	public static ArbitraryExpression from(String expression) {
		return new ArbitraryExpression(expression);
	}

	public ArbitraryExpression addFirst(String expression) {
		String newStringExpression = expression + "." + this;
		return new ArbitraryExpression(newStringExpression);
	}

	public ArbitraryExpression addLast(String expression) {
		String newStringExpression = this + "." + expression;
		return new ArbitraryExpression(newStringExpression);
	}

	public NodeResolver toNodeResolver() {
		NodeResolver nodeResolver = new ExpressionNodeResolver(ArbitraryExpression.from(HEAD_NAME));
		for (Exp exp : expList) {
			nodeResolver = exp.toNodeResolver(nodeResolver);
		}
		return nodeResolver;
	}

	public List<ArbitraryManipulator> toArbitraryManipulatorList() {
		List<ArbitraryManipulator> arbitraryManipulators = new ArrayList<>();
		for (Exp exp : expList) {
			for (ExpElement expElement : exp.elements) {
				if (expElement instanceof ExpKey) {

				}
			}
			arbitraryManipulators.add();
		}
	}

	// @API(since = "0.4.0", status = Status.EXPERIMENTAL)
	// public ArbitraryExpression pollLast() {
	// 	if (expList.isEmpty()) {
	// 		return this;
	// 	}
	//
	// 	List<Exp> newExpList = new ArrayList<>(this.expList);
	// 	int lastIndex = newExpList.size() - 1;
	// 	Exp lastExp = newExpList.get(lastIndex);
	// 	newExpList.remove(lastIndex);
	//
	// 	if (!lastExp.index.isEmpty()) {
	// 		List<ExpIndex> newExpIndexList = new ArrayList<>(lastExp.index);
	// 		newExpIndexList.remove(newExpIndexList.size() - 1);
	// 		lastExp = new Exp(lastExp.name, newExpIndexList);
	// 		newExpList.add(lastExp);
	// 	}
	// 	return new ArbitraryExpression(newExpList);
	// }

	@Override
	public int compareTo(ArbitraryExpression arbitraryExpression) {
		List<Exp> oExpList = arbitraryExpression.expList;

		if (expList.size() != oExpList.size()) {
			return Integer.compare(expList.size(), oExpList.size());
		}

		for (int i = 0; i < expList.size(); i++) {
			Exp exp = expList.get(i);
			Exp oExp = oExpList.get(i);
			int expCompare = exp.compareTo(oExp);
			if (expCompare != 0) {
				return expCompare;
			}
		}

		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		ArbitraryExpression other = (ArbitraryExpression)obj;
		return expList.equals(other.expList);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.expList);
	}

	public String toString() {
		return expList.stream()
			.map(Exp::toString)
			.collect(Collectors.joining("."));
	}

	public List<Cursor> toCursors() {
		return this.expList.stream()
			.flatMap(it -> it.toCursors().stream())
			.filter(Cursor::isNotHeadName)
			.collect(toList());
	}

	private interface ExpElement {
		String toString();

		NodeResolver toNodeResolver(NodeResolver prevResolver);
		// Cursor toCursor(); // IndexCursor, NameCursor, MapCursor
	}

	private static final class ExpIndex implements Comparable<ExpIndex>, ExpElement {
		public static final ExpIndex ALL_INDEX_EXP_INDEX = new ExpIndex(NO_OR_ALL_INDEX_INTEGER_VALUE);

		private final int index;

		public ExpIndex(int index) {
			this.index = index;
		}

		public int getIndex() {
			return index;
		}

		public boolean equalsIgnoreAllIndex(ExpIndex expIndex) {
			return this.index == expIndex.index;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null || getClass() != obj.getClass()) {
				return false;
			}
			ExpIndex expIndex = (ExpIndex)obj;
			return index == expIndex.index || index == NO_OR_ALL_INDEX_INTEGER_VALUE
				|| expIndex.index == NO_OR_ALL_INDEX_INTEGER_VALUE;
		}

		@Override
		public int hashCode() {
			return 0; // for allIndex, hash always return 0.
		}

		public String toString() {
			return index == NO_OR_ALL_INDEX_INTEGER_VALUE ? ALL_INDEX_STRING : String.valueOf(index);
		}

		@Override
		public NodeResolver toNodeResolver(NodeResolver prevResolver) {
			return new NodeIndexResolver(index, prevResolver);
		}

		@Override
		public int compareTo(ExpIndex obj) {
			return Integer.compare(index, obj.index);
		}
	}

	private static final class ExpKey implements ExpElement {
		private final String key;

		public ExpKey(String key) {
			this.key = key;
		}

		@Override
		public NodeResolver toNodeResolver(NodeResolver prevResolver) {
			if (prevResolver instanceof NodeKeyValueResolver) {
				return new NodeKeyValueResolver(false, prevResolver);
			}
			return new NodeKeyValueResolver(true, prevResolver);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null || getClass() != obj.getClass()) {
				return false;
			}
			ExpKey expKey = (ExpKey)obj;
			return key.equals(expKey.key);
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(key);
		}

		public String toString() {
			return key;
		}

	}

	private static final class Exp implements Comparable<Exp> {
		private final String name;
		private final List<ExpElement> elements;

		public NodeResolver toNodeResolver(NodeResolver prevResolver) {
			NodeResolver nodeResolver = new NodeFieldResolver(
				name,
				prevResolver
			);
			for (ExpElement expElement : elements) {
				nodeResolver = expElement.toNodeResolver(nodeResolver);
			}
			return nodeResolver;
		}

		public Exp(String expression) {
			elements = new ArrayList<>();
			int li = expression.indexOf('[');
			int ri = expression.indexOf(']');

			if ((li != -1 && ri == -1) || (li == -1 && ri != -1)) {
				throw new IllegalArgumentException("expression is invalid. expression : " + expression);
			}

			if (li == -1) {
				this.name = expression;
			} else {
				this.name = expression.substring(0, li);
				while (li != -1 && ri != -1) {
					if (ri - li > 1) {
						String elementString = expression.substring(li + 1, ri);
						if (elementString.equals(ALL_INDEX_STRING)) {
							this.elements.add(new ExpIndex(NO_OR_ALL_INDEX_INTEGER_VALUE));
						} else {
							try {
								int indexValue = Integer.parseInt(elementString);
								this.elements.add(new ExpIndex(indexValue));
							} catch (NumberFormatException e) {
								this.elements.add(new ExpKey(elementString));
							}
						}
					}
					expression = expression.substring(ri + 1);
					li = expression.indexOf('[');
					ri = expression.indexOf(']');
				}
			}
		}

		@Deprecated
		public List<Cursor> toCursors() {
			List<Cursor> steps = new ArrayList<>();
			String expName = this.getName();
			steps.add(new ExpNameCursor(expName));
			steps.addAll(this.getElements().stream()
				.filter(it -> it instanceof ExpIndex)
				.map(it -> new ExpIndexCursor(expName, ((ExpIndex)it).getIndex()))
				.collect(toList()));
			return steps;
		}

		public String getName() {
			return name;
		}

		public List<ExpElement> getElements() {
			return elements;
		}

		public String toString() {
			String elementsBrackets = elements.stream()
				.map(i -> "[" + i.toString() + "]")
				.collect(Collectors.joining());
			return name + elementsBrackets;
		}

		@Override
		public int compareTo(Exp exp) {
			List<ExpElement> elements = this.getElements();
			List<ExpElement> oElements = exp.getElements();

			if (exp.name.equals(this.name)) {
				int elementLength = Math.min(oElements.size(), elements.size());
				for (int i = 0; i < elementLength; i++) {
					ExpElement element = elements.get(i);
					ExpElement oElement = oElements.get(i);
					boolean elementEquals = oElement.equals(element);
					if (!elementEquals) {
						return 0;
					}
				}
			}
			return Integer.compare(elements.size(), oElements.size());
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null || getClass() != obj.getClass()) {
				return false;
			}
			Exp exp = (Exp)obj;
			return name.equals(exp.name) && elements.equals(exp.elements);
		}

		@Override
		public int hashCode() {
			return Objects.hash(name, elements);
		}
	}

	public abstract static class Cursor {
		private final String name;
		private final int index;

		public Cursor(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public boolean match(ArbitraryProperty arbitraryProperty) {
			String resolvePropertyName = arbitraryProperty.getResolvePropertyName();
			boolean samePropertyName;
			if (resolvePropertyName == null) {
				samePropertyName = true; // ignore property name equivalence.
			} else {
				samePropertyName = nameEquals(resolvePropertyName);
			}

			boolean sameIndex = true;
			if (arbitraryProperty.getElementIndex() != null) {
				sameIndex = indexEquals(arbitraryProperty.getElementIndex()); // notNull
			}
			return samePropertyName && sameIndex;
		}

		public boolean isNotHeadName() {
			return !(this instanceof ExpNameCursor) || !HEAD_NAME.equals(this.getName());
		}

		private boolean indexEquals(int index) {
			return this.index == index
				|| index == NO_OR_ALL_INDEX_INTEGER_VALUE
				|| this.index == NO_OR_ALL_INDEX_INTEGER_VALUE;
		}

		private boolean nameEquals(String name) {
			return this.name.equals(name)
				|| ALL_INDEX_STRING.equals(name)
				|| ALL_INDEX_STRING.equals(this.name);
		}

		public String getName() {
			return name;
		}

		public int getIndex() {
			return index;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (!(obj instanceof Cursor)) {
				return false;
			}
			Cursor cursor = (Cursor)obj;

			boolean indexEqual = indexEquals(cursor.getIndex());
			boolean nameEqual = nameEquals(cursor.getName());
			return nameEqual && indexEqual;
		}

		@Override
		public int hashCode() {
			return Objects.hash(name);
		}
	}

	static final class ExpIndexCursor extends Cursor {
		ExpIndexCursor(String name, int index) {
			super(name, index);
		}
	}

	public static final class ExpNameCursor extends Cursor {
		ExpNameCursor(String name) {
			super(name, NO_OR_ALL_INDEX_INTEGER_VALUE);
		}
	}

	// public static final class SpecConverter {
	// 	private final ArbitraryTraverser traverser;
	//
	// 	public SpecConverter(ArbitraryTraverser traverser) {
	// 		this.traverser = traverser;
	// 	}
	//
	// 	public String getContainerExpression(List<Exp> expList) {
	// 		StringBuilder builder = new StringBuilder();
	// 		for (Exp exp : expList) {
	// 			builder.append(exp.name);
	// 			for (ExpElement expElement : exp.elements) {
	// 				if (expElement instanceof ExpIndex) {
	// 					builder.append(expElement);
	// 				} else if (expElement instanceof ExpKey) {
	// 					return builder.toString();
	// 				}
	// 			}
	// 		}
	// 		return builder.toString();
	// 	}
	//
	// 	public ContainerSpec convertToContainerSpec(String expression, Object value) {
	// 		// 0. expression 파싱하기
	// 		List<Exp> expList = ArbitraryExpression.from(expression).expList;
	// 		// 1. 첫 Key 앞까지 Expression 사용해서 CollectionSpec 만들기
	// 		String containerExpression = getContainerExpression(expList);
	// 		ContainerSpec containerSpec = new ContainerSpec(
	// 			traverser,
	// 			new ExpressionNodeResolver(ArbitraryExpression.from(containerExpression))
	// 		);
	// 		// 2. 마지막 표현식을 토대로 연산 만들기
	// 		Exp lastExp = expList.get(expList.size() - 1);
	// 		ExpElement lastElement = lastExp.elements.get(lastExp.elements.size() - 1);
	// 		Consumer<ContainerSpec> consumer = entry(((ExpKey)lastElement).getKey(), value);
	//
	// 		// 2. key 개수에 따라 key()
	// 		// for (int i = expressions.size() - 2; i > 0; i--) {
	// 		// 	consumer = key(consumer);
	// 		// }
	//
	// 		consumer.accept(containerSpec);
	// 		return containerSpec;
	// 	}
	//
	// 	public Consumer<ContainerSpec> key(Consumer<ContainerSpec> consumer) {
	// 		return it -> {
	// 			it.key(consumer);
	// 		};
	// 	}
	//
	// 	public Consumer<ContainerSpec> entry(Object key, Object value) {
	// 		return it -> {
	// 			it.entry(key, value);
	// 		};
	// 	}
	// }
}
