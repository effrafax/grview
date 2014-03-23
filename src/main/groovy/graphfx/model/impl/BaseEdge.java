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
import jiggle.graph.EdgeLabel;
import jiggle.graph.JiggleObject;

public class BaseEdge<V extends Vertex<V>> extends JiggleObject implements Edge<V>
{
    private Vertex<V> from, to; /* endpoints of the edge */
    private EdgeLabel label = null; /* label of edge */
    private boolean directed = false; /* is the edge directed? */
    private double preferredLength = 0; /* preferred length of edge */

    public BaseEdge(Graph<V, ? extends Edge<V>> g, Vertex<V> f, Vertex<V> t)
    {
        from = f;
        to = t;
        setContext(g);
    }

    public BaseEdge(Graph<V, ? extends Edge<V>> g, Vertex<V> f, Vertex<V> t, boolean dir)
    {
        from = f;
        to = t;
        setContext(g);
        directed = dir;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jiggle.Edge#getFrom()
     */
    @Override
    public Vertex<V> getFrom()
    {
        return from;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jiggle.Edge#getTo()
     */
    @Override
    public Vertex<V> getTo()
    {
        return to;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jiggle.Edge#getLabel()
     */
    @Override
    public EdgeLabel getLabel()
    {
        return label;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jiggle.Edge#setLabel(jiggle.EdgeLabel)
     */
    @Override
    public void setLabel(EdgeLabel lbl)
    {
        label = lbl;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jiggle.Edge#getDirected()
     */
    @Override
    public boolean getDirected()
    {
        return directed;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jiggle.Edge#setDirected(boolean)
     */
    @Override
    public void setDirected(boolean d)
    {
        directed = d;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jiggle.Edge#getPreferredLength()
     */
    @Override
    public double getPreferredLength()
    {
        return preferredLength;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jiggle.Edge#setPreferredLength(double)
     */
    @Override
    public void setPreferredLength(double len)
    {
        preferredLength = len;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jiggle.Edge#getLengthSquared()
     */
    @Override
    public double getLengthSquared()
    {
        return BaseNode.getDistanceSquared(from, to);
    }

    /*
     * (non-Javadoc)
     * 
     * @see jiggle.Edge#getLength()
     */
    @Override
    public double getLength()
    {
        return BaseNode.getDistance(from, to);
    }

    /*
     * (non-Javadoc)
     * 
     * @see jiggle.Edge#toString()
     */
    @Override
    public String toString()
    {
        return "(Edge: " + from + ", " + to + ", "
                + (directed ? "directed" : "undirected") + ")";
    }

}
