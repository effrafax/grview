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
import graphfx.model.Vertex;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractVertex<V extends Vertex<V>> extends BaseNode implements Vertex<V> {
    
    private static final Logger log = LoggerFactory.getLogger(AbstractVertex.class);
    
    public AbstractVertex(Graph<V, ? extends Edge<V>> graph)
    {
        super();
        setContext(graph);
        setWeight(1);
        setDimensions(graph.getDimensions());
    }

    private List<Edge<V>> undirectedEdges = new ArrayList<Edge<V>>();
    private List<Edge<V>> inEdges = new ArrayList<Edge<V>>();
    private List<Edge<V>> outEdges = new ArrayList<Edge<V>>();
    private List<Vertex<V>> undirectedNeighbors = new ArrayList<Vertex<V>>();
    private List<Vertex<V>> outNeighbors = new ArrayList<Vertex<V>>();
    private List<Vertex<V>> inNeighbors = new ArrayList<Vertex<V>>();

    /*
     * NOTE: the above are made package-accessible for reasons of efficiency.
     * They should NOT, however, be modified except by insertNeighbor and
     * deleteNeighbor methods below.
     */

    private String name = ""; /* name of vertex */


    /*
     * (non-Javadoc)
     * 
     * @see jiggle.Vertex#getName()
     */
    @Override
    public String getName()
    {
        return name;
    }

    void setName(String str)
    {
        name = str;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jiggle.Vertex#insertNeighbor(jiggle.Edge)
     */
    @Override
    public void insertNeighbor(Edge<V> e)
    {
        log.debug("Inserting neighbor "+e.getClass().getName());
        log.debug("this: "+this.getClass().getName());
        Vertex<V> from = e.getFrom(), to = e.getTo();
        Vertex<V> v = null;
        if (from.equals(this))
            v = to;
        else if (to.equals(this))
            v = from;
        else
            throw new Error(e + " not incident to " + this);
        if (!e.getDirected())
        {
            undirectedEdges.add(e);
            undirectedNeighbors.add(v);
        } else if (this == from)
        {
            outEdges.add(e);
            outNeighbors.add(to);
        } else
        {
            inEdges.add(e);
            inNeighbors.add(from);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see jiggle.Vertex#deleteNeighbor(jiggle.Edge)
     */
    @Override
    public void deleteNeighbor(Edge<V> e)
    {
        Vertex<V> from = e.getFrom(), to = e.getTo();
        Vertex<V> v = null;
        if (this == from)
            v = to;
        else if (this == to)
            v = from;
        else
            throw new Error(e + " not incident to " + this);
        boolean found = true;
        if (!e.getDirected())
        {
            found = undirectedEdges.remove(e) && undirectedEdges.remove(v);
        } else if (this == from)
        {
            found = outEdges.remove(e) && outNeighbors.remove(to);
        } else
        {
            found = inEdges.remove(e) && inNeighbors.remove(from);
        }
        if (!found)
            throw new Error(e + " not incident to " + this);
    }

    public String toString()
    {
        return "(Vertex: " + name + ")";
    }

    /*
     * (non-Javadoc)
     * 
     * @see jiggle.Vertex#getUndirectedEdges()
     */
    @Override
    public List<Edge<V>> getUndirectedEdges()
    {
        return undirectedEdges;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jiggle.Vertex#setUndirectedEdges(jiggle.Edge[])
     */
    @Override
    public void setUndirectedEdges(
        List<Edge<V>> undirectedEdges)
    {
        this.undirectedEdges.clear();
        this.undirectedEdges.addAll(undirectedEdges);
    }

    /*
     * (non-Javadoc)
     * 
     * @see jiggle.Vertex#getInEdges()
     */
    @Override
    public List<Edge<V>> getInEdges()
    {
        return inEdges;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jiggle.Vertex#setInEdges(jiggle.Edge[])
     */
    @Override
    public void setInEdges(List<Edge<V>> inEdges)
    {
        this.inEdges.clear();
        this.inEdges.addAll(inEdges);
    }

    /*
     * (non-Javadoc)
     * 
     * @see jiggle.Vertex#getOutEdges()
     */
    @Override
    public List<Edge<V>> getOutEdges()
    {
        return outEdges;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jiggle.Vertex#setOutEdges(jiggle.Edge[])
     */
    @Override
    public void setOutEdges(List<Edge<V>> outEdges)
    {
        this.outEdges.clear();
        this.outEdges.addAll(outEdges);
    }

    /*
     * (non-Javadoc)
     * 
     * @see jiggle.Vertex#getUndirectedNeighbors()
     */
    @Override
    public List<Vertex<V>> getUndirectedNeighbors()
    {
        return undirectedNeighbors;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jiggle.Vertex#setUndirectedNeighbors(jiggle.BaseVertex[])
     */
    @Override
    public void setUndirectedNeighbors(
        List<Vertex<V>> undirectedNeighbors)
    {
        this.undirectedNeighbors.clear();
        this.undirectedNeighbors.addAll(undirectedNeighbors);
    }

    /*
     * (non-Javadoc)
     * 
     * @see jiggle.Vertex#getInNeighbors()
     */
    @Override
    public List<Vertex<V>> getInNeighbors()
    {
        return inNeighbors;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jiggle.Vertex#setInNeighbors(jiggle.BaseVertex[])
     */
    @Override
    public void setInNeighbors(List<Vertex<V>> inNeighbors)
    {
        this.inNeighbors.clear();
        this.inNeighbors.addAll(inNeighbors);
    }

    /*
     * (non-Javadoc)
     * 
     * @see jiggle.Vertex#getOutNeighbors()
     */
    @Override
    public List<Vertex<V>> getOutNeighbors()
    {
        return outNeighbors;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jiggle.Vertex#setOutNeighbors(jiggle.BaseVertex[])
     */
    @Override
    public void setOutNeighbors(List<Vertex<V>> outNeighbors)
    {
        this.outNeighbors.clear();
        this.outNeighbors.addAll(outNeighbors);
    }

    @Override
    public int getUndirectedDegree()
    {
        return undirectedNeighbors.size();
    }


    public int getInDegree()
    {
        return inNeighbors.size();
    }

    public int getOutDegree()
    {
        return outNeighbors.size();
    }

    @SuppressWarnings("unchecked")
    @Override
    public V getExtension()
    {
        return (V)this;
    }



    

   

}
