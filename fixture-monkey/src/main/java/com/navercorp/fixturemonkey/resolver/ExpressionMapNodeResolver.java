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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import com.navercorp.fixturemonkey.api.type.Types;
import com.navercorp.fixturemonkey.arbitrary.ArbitraryExpression;

@API(since = "0.4.0", status = Status.EXPERIMENTAL)
public final class ExpressionMapNodeResolver implements NodeResolver {
	private final ArbitraryExpression arbitraryExpression;
	private final List<Object> keys;

	public ExpressionMapNodeResolver(ArbitraryExpression arbitraryExpression, List<Object> keys) {
		//Todo: ? 개수랑 key 개수랑 맞지 않으면 Error
		this.arbitraryExpression = arbitraryExpression;
		this.keys = keys;
	}

	@Override
	public List<ArbitraryNode> resolve(ArbitraryTree arbitraryTree) {
		// List<ArbitraryNode> selectedNodes = arbitraryTree.findAllorInsert(arbitraryExpression, keys);
		List<ArbitraryNode> foundNodes = new LinkedList<>();

		// for(ArbitraryNode selectedNode : selectedNodes) {
		// 	Class<?> selectedNodeType = Types.getActualType(selectedNode.getProperty().getType());
		// 	//Map Entry인 경우 Value 노드를 반환
		// 	if (Map.Entry.class.isAssignableFrom(selectedNodeType)) {
		// 		foundNodes.add(selectedNode.getChildren().get(1));
		// 	} else {
		// 		foundNodes.add(selectedNode);
		// 	}
		// }
		return foundNodes;
	}
}
