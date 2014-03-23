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
import java.util.NoSuchElementException;



public interface Graph<V extends Vertex<V>,E extends Edge<V>> extends Node
{

    public Vertex<V> insertVertex();
    
    public Vertex<V> addVertex(Vertex<V> vtx);

    public E insertEdge(Vertex<V> from, Vertex<V> to);

    public E insertEdge(Vertex<V> from, Vertex<V> to, boolean dir);
    
    public E addEdge(E edge);

    public void deleteVertex(Vertex<V> v);

    public void deleteEdge(E e) throws NoSuchElementException;

    public int getVertexNumber();

    public int getEdgeNumber();

    public List<Vertex<V>> getVertices();

    public List<E> getEdges();

    public int getDimensions();

    public void recomputeBoundaries();
    
    public boolean isConstrained();
    
    public void setConstrained(boolean constr);

}