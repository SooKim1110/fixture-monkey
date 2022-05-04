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

import java.util.Objects;
import java.util.function.Supplier;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

public final class ArbitrarySet<T> extends AbstractArbitrarySet<T> {
	private T value;
	private Supplier<T> supplier;
	private long limit;

	public ArbitrarySet(ArbitraryExpression arbitraryExpression, T value, long limit) {
		super(arbitraryExpression);
		this.value = value;
		this.limit = limit;
	}

	public ArbitrarySet(ArbitraryExpression arbitraryExpression, T value) {
		this(arbitraryExpression, value, Long.MAX_VALUE);
	}

	public ArbitrarySet(ArbitraryExpression arbitraryExpression, Supplier<T> supplier) {
		super(arbitraryExpression);
		this.supplier = supplier;
		this.limit = Long.MAX_VALUE;
	}

	@Override
	public T getValue() {
		if (value == null && supplier != null) {
			return supplier.get();
		}
		return value;
	}

	@Override
	public Arbitrary<T> apply(Arbitrary<T> from) {
		if (this.limit > 0) {
			limit--;
			return Arbitraries.just(value);
		} else {
			return from;
		}
	}

	@Override
	public ArbitrarySet<T> copy() {
		return new ArbitrarySet<>(this.getArbitraryExpression(), this.getValue(), this.limit);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		if (!super.equals(obj)) {
			return false;
		}
		ArbitrarySet<?> that = (ArbitrarySet<?>)obj;
		return value.equals(that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), value);
	}
}
