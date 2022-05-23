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

package com.navercorp.fixturemonkey.resolver;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import com.navercorp.fixturemonkey.api.property.MapEntryElementProperty;
import com.navercorp.fixturemonkey.arbitrary.ArbitraryExpression;

@API(since = "0.4.0", status = Status.EXPERIMENTAL)
public final class ExpressionMapNodeResolver implements NodeResolver {
	private final ArbitraryExpression arbitraryExpression;
	private final List<Object> keys;

	public ExpressionMapNodeResolver(ArbitraryExpression arbitraryExpression, List<Object> keys) {
		// IllegalArgumentException when number of "?" is different from number of keys provided
		// 이 부분을 ArbitraryExpression을 확장하고, 그 부분에 넣을 수 없을까?
		Long numOfKeys = arbitraryExpression.toCursors().stream().filter(cursor -> cursor.getName().equals("?")).count();
		if (!numOfKeys.equals((long)keys.size())) {
			throw new IllegalArgumentException("Number of placeholders does not match number of keys provided : " + arbitraryExpression);
		}
		this.arbitraryExpression = arbitraryExpression;
		this.keys = keys;
	}

	@Override
	public List<ArbitraryNode> resolve(ArbitraryTree arbitraryTree) {
		List<ArbitraryNode> selectedNodes = arbitraryTree.findAll(arbitraryExpression, keys);
		List<ArbitraryNode> foundNodes = new LinkedList<>();

		for(ArbitraryNode selectedNode : selectedNodes) {
			//Map Entry인 경우 Value 노드를 반환
			if (selectedNode.getProperty() instanceof MapEntryElementProperty) {
				foundNodes.add(selectedNode.getChildren().get(1));
			} else {
				foundNodes.add(selectedNode);
			}
		}
		return foundNodes;
	}
}
