/*
 * Copyright 2008-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package graphfx.model.impl;

import graphfx.model.Edge;
import graphfx.model.Graph;
import graphfx.model.NodeFactory;
import graphfx.model.impl.BaseVertex;
import graphfx.model.Vertex;

public class BaseNodeFactory implements NodeFactory<BaseVertex, Edge<BaseVertex>>
{

    public BaseNodeFactory()
    {
        // TODO Auto-generated constructor stub
    }


    @Override
    public Edge<BaseVertex> createEdge(
        Graph<BaseVertex, Edge<BaseVertex>> graph, Vertex<BaseVertex> from, Vertex<BaseVertex> to)
    {
        return new BaseEdge<BaseVertex>(graph, from, to);
    }

    @Override
    public Edge<BaseVertex> createEdge(
        Graph<BaseVertex, Edge<BaseVertex>> graph, Vertex<BaseVertex> from, Vertex<BaseVertex> to,
        boolean directed)
    {
        return new BaseEdge<BaseVertex>(graph, from, to, directed);
    }


    @Override
    public BaseVertex createVertex(Graph<BaseVertex, Edge<BaseVertex>> graph)
    {
        return new BaseVertex(graph);
    }

}
