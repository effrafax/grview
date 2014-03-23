package jiggle.graph;

import graphfx.model.Edge;
import graphfx.model.Vertex;
import graphfx.model.impl.BaseNode;

/* Class for edge labels. */

public class EdgeLabel extends BaseNode
{

    String name;

    EdgeLabel(Edge<? extends Vertex<?>> e, String str)
    {
        setContext(e);
        name = str;
    }

    String getName()
    {
        return name;
    }

    void setName(String str)
    {
        name = str;
    }

    public String toString()
    {
        return "(EdgeLabel: " + name + ")";
    }
}