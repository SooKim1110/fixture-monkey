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

package com.navercorp.fixturemonkey.api.arbitrary;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import com.navercorp.fixturemonkey.api.property.PropertyPath;
import com.navercorp.fixturemonkey.api.property.Traceable;

/**
 * It is a delegate implementation of CombinableArbitrary for logging.
 */
@API(since = "0.6.0", status = Status.EXPERIMENTAL)
public final class TraceableCombinableArbitrary<T> implements CombinableArbitrary<T>, Traceable {
	private final CombinableArbitrary<T> combinableArbitrary;
	private final PropertyPath propertyPath;

	public TraceableCombinableArbitrary(CombinableArbitrary<T> combinableArbitrary, PropertyPath propertyPath) {
		this.combinableArbitrary = combinableArbitrary;
		this.propertyPath = propertyPath;
	}

	@Override
	public T combined() {
		return combinableArbitrary.combined();
	}

	@Override
	public Object rawValue() {
		return combinableArbitrary.rawValue();
	}

	@Override
	public void clear() {
		combinableArbitrary.clear();
	}

	@Override
	public boolean fixed() {
		return combinableArbitrary.fixed();
	}

	@Override
	public PropertyPath getPropertyPath() {
		return propertyPath;
	}
}
