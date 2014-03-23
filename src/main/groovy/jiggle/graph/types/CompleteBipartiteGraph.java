package jiggle.graph.types;

import graphfx.model.Edge;
import graphfx.model.impl.BaseVertex;
import graphfx.model.Vertex;
import graphfx.model.impl.BaseGraph;
import graphfx.model.impl.BaseNodeFactory;



public class CompleteBipartiteGraph extends BaseGraph<BaseVertex,Edge<BaseVertex>>
{

    public CompleteBipartiteGraph(int n)
    {
        super(new BaseNodeFactory());
        initialize(n);
    }

    public CompleteBipartiteGraph(int n, int d)
    {
        super(new BaseNodeFactory(),d);
        initialize(n);
    }

    private void initialize(int n)
    {
        @SuppressWarnings("unchecked")
        Vertex<BaseVertex> V[] = new Vertex[2 * n];
        for (int i = 0; i < 2 * n; i++)
            V[i] = insertVertex();
        for (int i = 0; i < n; i++)
            for (int j = n; j < 2 * n; j++)
                insertEdge(V[i], V[j]);
    }
}
