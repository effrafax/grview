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

package graphfx.model;

import java.util.List;

public interface Vertex<V extends Vertex<V>> extends Node
{
    
    public String getName();

    public boolean isFixed();
    
    public int getUndirectedDegree();

    public int getInDegree();

    public int getOutDegree();
    
    public void insertNeighbor(Edge<V> e);

    public void deleteNeighbor(Edge<V> e);

    public List<Edge<V>> getUndirectedEdges();

    public void setUndirectedEdges(List<Edge<V>> undirectedEdges);

    public List<Edge<V>> getInEdges();

    public void setInEdges(List<Edge<V>> inEdges);

    public List<Edge<V>> getOutEdges();

    public void setOutEdges(List<Edge<V>> outEdges);

    public List<Vertex<V>> getUndirectedNeighbors();

    public void setUndirectedNeighbors(
        List<Vertex<V>> undirectedNeighbors);

    public List<Vertex<V>> getInNeighbors();

    public void setInNeighbors(List<Vertex<V>> inNeighbors);

    public List<Vertex<V>> getOutNeighbors();

    public void setOutNeighbors(List<Vertex<V>> outNeighbors);
    
    public V getExtension();

}
