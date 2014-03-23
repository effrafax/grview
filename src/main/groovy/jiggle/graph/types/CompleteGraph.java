package jiggle.graph.types;

import graphfx.model.Edge;
import graphfx.model.impl.BaseVertex;
import graphfx.model.Vertex;
import graphfx.model.impl.BaseGraph;
import graphfx.model.impl.BaseNodeFactory;


public class CompleteGraph extends BaseGraph<BaseVertex, Edge<BaseVertex>>
{

    public CompleteGraph(int n)
    {
        super(new BaseNodeFactory());
        initialize(n);
    }

    public CompleteGraph(int n, int d)
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
        for (int i = 0; i < n - 1; i++)
        {
            for (int j = i + 1; j < n; j++)
            {
                insertEdge(V[i], V[j]);
            }
        }
    }
}
