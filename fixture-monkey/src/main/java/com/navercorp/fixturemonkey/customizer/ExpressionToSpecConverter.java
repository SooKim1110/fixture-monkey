package com.navercorp.fixturemonkey.customizer;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import com.navercorp.fixturemonkey.arbitrary.ArbitraryExpression;
import com.navercorp.fixturemonkey.resolver.ArbitraryTraverser;
import com.navercorp.fixturemonkey.resolver.ExpressionNodeResolver;

public final class ExpressionToSpecConverter {
	private final ArbitraryTraverser traverser;

	public ExpressionToSpecConverter(ArbitraryTraverser traverser) {
		this.traverser = traverser;
	}

	public CollectionSpec convertToCollectionSpec (String expression, Object key) {
		// 0. expression을 파싱하기(ArbitraryExpression 클래스처럼)
		List<String> expressions = parse(expression);
		// 1. 첫 K 앞까지 Expression 사용해서 CollectionSpec 만들기
		// 일단 0번째까지가 접근할 expression이라고 가정
		CollectionSpec collectionSpec = new CollectionSpec(
			traverser,
			new ExpressionNodeResolver(ArbitraryExpression.from(expressions.get(0)))
		);
		//마지막 표현식을 토대로 연산 만들기
		Consumer<CollectionSpec> consumer = key(key);
	    // 2. 표현식에 따라 consumer 만들기
		for (int i = expressions.size()-2; i>0; i--) {
			if (expressions.get(i).equals("[K]")) {
				consumer = key(consumer);
			} else if (expressions.get(i).equals("[V]")) {
				consumer = value(consumer);
			}
			//field
			else {

			}
		}
		consumer.accept(collectionSpec);
		return collectionSpec;
	}

	public List<String> parse(String expression) {
		List<String> expressions = Arrays.stream(expression.split("\\."))
			.flatMap(it-> splitExpression(it).stream())
			.collect(toList());
		return expressions;
	}

	public List<String> splitExpression(String expression) {
		List<String> expressions = new ArrayList<>();
		int li = expression.indexOf('[');
		int ri = expression.indexOf(']');

		if ((li != -1 && ri == -1) || (li == -1 && ri != -1)) {
			throw new IllegalArgumentException("expression is invalid. expression : " + expression);
		}

		if (li == -1) {
			expressions.add(expression);
		} else {
			expressions.add(expression.substring(0, li));
			while (li != -1 && ri != -1) {
				if (ri - li > 1) {
					expressions.add(expression.substring(li, ri+1));
				}
				expression = expression.substring(ri + 1);
				li = expression.indexOf('[');
				ri = expression.indexOf(']');
			}
		}
		return expressions;
	}

	public Consumer<CollectionSpec> key(Consumer<CollectionSpec> consumer) {
		return it -> {
			it.key(consumer);
		};
	}

	public Consumer<CollectionSpec> key(Object key) {
		return it -> {
			it.key(key);
		};
	}

	public Consumer<CollectionSpec> value(Consumer<CollectionSpec> consumer) {
		return it -> {
			it.value(consumer);
		};
	}

	public Consumer<CollectionSpec> value(Object value) {
		return it -> {
			it.value(value);
		};
	}

	public Consumer<CollectionSpec> field(String field, Consumer<CollectionSpec> consumer) {
		return it -> {
			it.field(field, consumer);
		};
	}

	public Consumer<CollectionSpec> field(String field, Object value) {
		return it -> {
			it.field(field, value);
		};
	}

	public Consumer<CollectionSpec> listElement(int index, Consumer<CollectionSpec> consumer) {
		return it -> {
			it.listElement(index, consumer);
		};
	}

	public Consumer<CollectionSpec> listElement(int index, Object value) {
		return it -> {
			it.listElement(index, value);
		};
	}

	private static final class Exp {
		private String name;
		private final List<ExpElement> element;

		private Exp(String name) {
			this.name = name;
			this.element = new ArrayList<>();
		}

		public List<String> splitExpression(String expression) {
			List<String> expressions = new ArrayList<>();
			int li = expression.indexOf('[');
			int ri = expression.indexOf(']');

			if ((li != -1 && ri == -1) || (li == -1 && ri != -1)) {
				throw new IllegalArgumentException("expression is invalid. expression : " + expression);
			}

			if (li == -1) {
				this.name = name + expression;
			} else {
				expressions.add(expression.substring(0, li));
				while (li != -1 && ri != -1) {
					if (ri - li > 1) {
						String substr = expression.substring(li+1, ri);
						if (substr == "K") {
							element.add(new ExpMap(true));
						} else if (substr == "V") {
							element.add(new ExpMap(false));
						} else {
							element.add(new ExpIndex(Integer.parseInt(substr)));
						}
					}
					expression = expression.substring(ri + 1);
					li = expression.indexOf('[');
					ri = expression.indexOf(']');
				}
			}
			return expressions;
		}
	}

	private interface ExpElement {
		// String toString();
		// Cursor toCursor(); // IndexCursor, NameCursor, MapCursor
	}

	private static final class ExpIndex implements ExpElement {
		private final int index;

		public ExpIndex(int index) {
			this.index = index;
		}
	}

	private static final class ExpMap implements ExpElement {
		private final Boolean isSetKey;

		public ExpMap(Boolean isSetKey) {
			this.isSetKey = isSetKey;
		}
	}

	private static final class ExpField implements ExpElement {
		private final String field;

		public ExpField(String field) {
			this.field = field;
		}
	}


}
