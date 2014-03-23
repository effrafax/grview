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

package graphfx.ui.graph;

import graphfx.model.Graph;
import graphfx.model.GraphElement;
import graphfx.model.NodeFactory;
import graphfx.model.Vertex;
import graphfx.model.impl.BaseGraph;
import graphfx.ui.FxNodeFactory;
import graphfx.ui.fx.GraphEvent;

import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;

public class FxGraph extends Group implements Graph<FxVertex, FxEdge>, EventHandler<GraphEvent>
{

    private static final Logger log = LoggerFactory.getLogger(FxGraph.class);

    
    Graph<FxVertex, FxEdge> delegate;

    public FxGraph()
    {
        super();
        init(null);
    }

    public FxGraph(NodeFactory<FxVertex, FxEdge> factory)
    {
        super();
        init(factory);
    }

    private void init(NodeFactory<FxVertex, FxEdge> factory)
    {
        if (factory == null)
        {
            delegate =new BaseGraph<>(new FxNodeFactory());
        } else
        {
            delegate = new BaseGraph<>(factory);
        }
        addEventHandler(GraphEvent.GRAPH_NODE_FIXED, this);
        addEventHandler(GraphEvent.GRAPH_NODE_RELEASED, this);
    }

    public boolean isBooleanField()
    {
        return delegate.isBooleanField();
    }

    public void setBooleanField(boolean b)
    {
        delegate.setBooleanField(b);
    }

    public void setDimensions(int d)
    {
        delegate.setDimensions(d);
    }

    public int getIntField()
    {
        return delegate.getIntField();
    }

    public double[] getCoords()
    {
        return delegate.getCoords();
    }

    public void setIntField(int intField)
    {
        delegate.setIntField(intField);
    }

    public void setCoords(double[] c)
    {
        delegate.setCoords(c);
    }

    public Vertex<FxVertex> insertVertex()
    {
        Vertex<FxVertex> vtx = delegate.insertVertex();
        getChildren().add(vtx.getExtension());
        return vtx;
    }

    public Object getObjectField()
    {
        return delegate.getObjectField();
    }

    public boolean isFixed()
    {
        return delegate.isFixed();
    }

    public Vertex<FxVertex> addVertex(Vertex<FxVertex> vtx)
    {
        getChildren().add(vtx.getExtension());
        return delegate.addVertex(vtx);
    }

    public void setObjectField(Object objectField)
    {
        delegate.setObjectField(objectField);
    }

    public void setFixed(boolean fixed)
    {
        delegate.setFixed(fixed);
    }

    public FxEdge insertEdge(Vertex<FxVertex> from, Vertex<FxVertex> to)
    {
        FxEdge edge = delegate.insertEdge(from, to);
        ObservableList<Node> children = getChildren();
        if (!children.contains(from.getExtension()))
        {
            children.add(from.getExtension());
        }
        if (!children.contains(to.getExtension()))
        {
            children.add(to.getExtension());
        }
        children.add(edge);
        return edge;
    }

    public double[] getSize()
    {
        return delegate.getSize();
    }

    public GraphElement getContext()
    {
        return delegate.getContext();
    }

    public FxEdge insertEdge(
        Vertex<FxVertex> from, Vertex<FxVertex> to, boolean dir)
    {
        FxEdge edge = delegate.insertEdge(from, to, dir);
        ObservableList<Node> children = getChildren();
        if (!children.contains(from.getExtension()))
        {
            children.add(from.getExtension());
        }
        if (!children.contains(to.getExtension()))
        {
            children.add(to.getExtension());
        }
        children.add(edge);
        return edge;
    }

    public double[] getMin()
    {
        return delegate.getMin();
    }

    public void setContext(GraphElement context)
    {
        delegate.setContext(context);
    }

    public double[] getMax()
    {
        return delegate.getMax();
    }

    public FxEdge addEdge(FxEdge edge)
    {
        getChildren().add(edge);
        return delegate.addEdge(edge);
    }

    public void translate(double scalar, double[] vector)
    {
        delegate.translate(scalar, vector);
    }

    public void deleteVertex(Vertex<FxVertex> v)
    {
        getChildren().remove(v);
        delegate.deleteVertex(v);
    }

    public void deleteEdge(FxEdge e) throws NoSuchElementException
    {
        getChildren().remove(e);
        delegate.deleteEdge(e);
    }

    public void translate(double[] vector)
    {
        delegate.translate(vector);
    }

    public int getVertexNumber()
    {
        return delegate.getVertexNumber();
    }

    public void setWeight(double w)
    {
        delegate.setWeight(w);
    }

    public int getEdgeNumber()
    {
        return delegate.getEdgeNumber();
    }

    public double getWeight()
    {
        return delegate.getWeight();
    }

    public List<Vertex<FxVertex>> getVertices()
    {
        return delegate.getVertices();
    }

    public List<FxEdge> getEdges()
    {
        return delegate.getEdges();
    }

    public int getDimensions()
    {
        return delegate.getDimensions();
    }

    public void recomputeBoundaries()
    {
        delegate.recomputeBoundaries();
    }

    public void syncModelToNodes()
    {
        for (Vertex<FxVertex> vertex : getVertices())
        {
            ((FxVertex) vertex).syncModelToNode();
        }
    }

    public void syncNodesToModel()
    {
        for (Vertex<FxVertex> vertex : getVertices())
        {
            ((FxVertex) vertex).syncNodeToModel();
        }
    }

    @Override
    public void handle(GraphEvent event)
    {
       log.debug("Graph event"+event.getEventType());
    }

    @Override
    public boolean isConstrained()
    {
        return delegate.isConstrained();
    }

    @Override
    public void setConstrained(boolean constr)
    {
        delegate.setConstrained(constr);
    }
    
    

}
