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
import graphfx.model.Vertex;

import java.util.ArrayList;
import java.util.List;

public class BaseGraph<V extends Vertex<V>, E extends Edge<V>> extends BaseNode implements Graph<V, E> 
{

    // public int numberOfVertices = 0, numberOfEdges = 0;
    protected List<Vertex<V>> vertices;
    protected List<E> edges;
    private int constrained;
    
    NodeFactory<V, E> factory;

    public BaseGraph(NodeFactory<V,E> factory)
    {
        super();
        this.factory = factory;
        initialize(10, 20);
    }

    public BaseGraph(NodeFactory<V,E> factory, int d)
    {
        this.factory = factory;
        initialize(10, 20);
        setDimensions(d);
    }

    public BaseGraph(NodeFactory<V,E> factory, int d, int vtxSize, int edgSize)
    {
        this.factory = factory;
        initialize(vtxSize, edgSize);
        setDimensions(d);
    }
    
    public void setNodeFactory(NodeFactory<V,E> factory) {
        this.factory = factory;
    }

    public NodeFactory<V,E> getNodeFactory() {
        return this.factory;
    }

    private void initialize(int vtxSize, int edgSize)
    {
        vertices = new ArrayList<>(vtxSize);
        edges = new ArrayList<>(edgSize);
    }

    /*
     * (non-Javadoc)
     * 
     * @see jiggle.Graph#insertVertex()
     */
    @Override
    public Vertex<V> insertVertex()
    {
        Vertex<V> v = factory.createVertex(this);
        vertices.add(v);
        return v;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jiggle.Graph#insertEdge(jiggle.Vertex, jiggle.Vertex)
     */
    @Override
    public E insertEdge(Vertex<V> from, Vertex<V> to)
    {
        return insertEdge(from, to, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see jiggle.Graph#insertEdge(jiggle.Vertex, jiggle.Vertex, boolean)
     */
    @Override
    public E insertEdge(Vertex<V> from, Vertex<V> to, boolean dir)
    {
        E e = factory.createEdge(this, from, to, dir);
        from.insertNeighbor(e);
        to.insertNeighbor(e);
        edges.add(e);
        return e;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jiggle.Graph#deleteVertex(jiggle.Vertex)
     */
    @Override
    public void deleteVertex(Vertex<V> v)
    {
        for (int i = 0; i < v.getInDegree(); i++)
        {
            Edge<V> e = v.getUndirectedEdges().get(i);
            v.getUndirectedNeighbors().get(i).deleteNeighbor(e);
            edges.remove(e);
        }
        for (int i = 0; i < v.getInDegree(); i++)
        {
            Edge<V> e = v.getInEdges().get(i);
            v.getInNeighbors().get(i).deleteNeighbor(e);
            edges.remove(e);
        }
        for (int i = 0; i < v.getOutDegree(); i++)
        {
            Edge<V> e = v.getOutEdges().get(i);
            v.getOutNeighbors().get(i).deleteNeighbor(e);
            edges.remove(e);
        }
        vertices.remove(v);
    }

    /*
     * (non-Javadoc)
     * 
     * @see jiggle.Graph#deleteEdge(jiggle.Edge)
     */
    @Override
    public void deleteEdge(E e)
    {
        e.getFrom().deleteNeighbor(e);
        e.getTo().deleteNeighbor(e);
        edges.remove(e);
    }

    public void recomputeBoundaries()
    {
        int d = getDimensions();
        double lo[] = getMin(), hi[] = getMax();
        for (int i = 0; i < d; i++)
        {
            lo[i] = Double.MAX_VALUE;
            hi[i] = -Double.MAX_VALUE;
        }
        for (Vertex<V> v : vertices)
        {
            double c[] = v.getCoords();
            for (int j = 0; j < d; j++)
            {
                lo[j] = Math.min(lo[j], c[j]);
                hi[j] = Math.max(hi[j], c[j]);
            }
        }
        recomputeSize();
    }

    // The isConnected method tests whether a graph is connected.
    // An empty graph is considered to be not connected.

    boolean isConnected()
    {
        if (vertices.size() == 0)
            return false;
        for (Vertex<V> v : vertices)
            v.setBooleanField(false);
        numberOfMarkedVertices = 0;
        dft(vertices.iterator().next());
        return (numberOfMarkedVertices == vertices.size());
    }

    private int numberOfMarkedVertices = 0;

    private void dft(Vertex<V> v)
    {
        v.setBooleanField(true);
        ++numberOfMarkedVertices;
        for (int i = 0; i < v.getUndirectedDegree(); i++)
        {
            Vertex<V> neighbor = v.getUndirectedNeighbors().get(i);
            if (!neighbor.isBooleanField())
                dft(neighbor);
        }
        for (int i = 0; i < v.getUndirectedDegree(); i++)
        {
            Vertex<V> neighbor = v.getInNeighbors().get(i);
            if (!neighbor.isBooleanField())
                dft(neighbor);
        }
        for (int i = 0; i < v.getUndirectedDegree(); i++)
        {
            Vertex<V> neighbor = v.getOutNeighbors().get(i);
            if (!neighbor.isBooleanField())
                dft(neighbor);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see jiggle.Graph#getVertexNumber()
     */
    @Override
    public int getVertexNumber()
    {
        return vertices.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see jiggle.Graph#getEdgetNumber()
     */
    @Override
    public int getEdgeNumber()
    {
        return edges.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see jiggle.Graph#getVertices()
     */
    @Override
    public List<Vertex<V>> getVertices()
    {
        return vertices;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jiggle.Graph#getEdges()
     */
    @Override
    public List<E> getEdges()
    {
        return edges;
    }

    public void translate(double scalar, double[] vector)
    {
    }

    public void translate(double[] vector)
    {
    }

    public void setWeight(double w)
    {
    }

    public double getWeight()
    {
        return 0;
    }

    @Override
    public Vertex<V> addVertex(Vertex<V> vtx)
    {
        vertices.add(vtx);
        return vtx;
    }
    
    @Override
    public E addEdge(E edge)
    {
        edges.add(edge);
        return edge;
    }

    @Override
    public boolean isConstrained()
    {
        return constrained>0;
    }

    @Override
    public void setConstrained(boolean constr)
    {
        if (constr) {
            constrained++;
        } else {
            constrained--;
        }
    }

}
