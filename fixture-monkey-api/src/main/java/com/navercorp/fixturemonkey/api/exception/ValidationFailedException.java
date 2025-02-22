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

package com.navercorp.fixturemonkey.api.exception;

import java.util.Set;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import com.navercorp.fixturemonkey.api.validator.ArbitraryValidator;

/**
 * It is thrown when validated by {@link ArbitraryValidator}.
 * A new populated object would be generated when this exception is thrown.
 */
@API(since = "0.6.0", status = Status.EXPERIMENTAL)
public final class ValidationFailedException extends RuntimeException {
	private final Set<String> constraintViolationPropertyNames;

	public ValidationFailedException(String message, Set<String> constraintViolationPropertyNames) {
		super(message);
		this.constraintViolationPropertyNames = constraintViolationPropertyNames;
	}

	public Set<String> getConstraintViolationPropertyNames() {
		return constraintViolationPropertyNames;
	}
}
