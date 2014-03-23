package jiggle.graph.types;

import graphfx.model.Edge;
import graphfx.model.NodeFactory;
import graphfx.model.Vertex;
import graphfx.model.impl.BaseGraph;

import java.util.ArrayList;
import java.util.List;

public class GenericPath<V extends Vertex<V>,E extends Edge<V>> extends BaseGraph<V,E>
{

    public GenericPath(NodeFactory<V,E> factory, int n)
    {
        super(factory);
        initialize(n);
    }
    
    private void initialize(int n)
    {
        List<Vertex<V>> vtc = new ArrayList<>();
        for (int i = 0; i < n; i++)
            vtc.add(i,insertVertex());
        for (int i = 1; i < n; i++)
            insertEdge(vtc.get(i-1), vtc.get(i));
    }

}
