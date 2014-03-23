package jiggle.graph.types;

import graphfx.model.Edge;
import graphfx.model.impl.BaseVertex;
import graphfx.model.Vertex;
import graphfx.model.impl.BaseGraph;
import graphfx.model.impl.BaseNodeFactory;



public class Path extends BaseGraph<BaseVertex,Edge<BaseVertex>>
{

    public Path(int n)
    {
        super(new BaseNodeFactory());
        initialize(n);
    }

    public Path(int n, int d)
    {
        super(new BaseNodeFactory(),d);
        initialize(n);
    }

    private void initialize(int n)
    {
        @SuppressWarnings("unchecked")
        Vertex<BaseVertex> V[] = new Vertex[n];
        for (int i = 0; i < n; i++)
            V[i] = insertVertex();
        for (int i = 1; i < n; i++)
            insertEdge(V[i - 1], V[i]);
    }
}
