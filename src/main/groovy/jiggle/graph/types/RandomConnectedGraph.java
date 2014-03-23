package jiggle.graph.types;

import graphfx.model.Edge;
import graphfx.model.impl.BaseVertex;
import graphfx.model.Vertex;
import graphfx.model.impl.BaseGraph;
import graphfx.model.impl.BaseNodeFactory;



public class RandomConnectedGraph extends BaseGraph<BaseVertex, Edge<BaseVertex>>
{

    public RandomConnectedGraph(int n, int m)
    {
        super(new BaseNodeFactory());
        initialize(n, m);
    }

    public RandomConnectedGraph(int n, int m, int d)
    {
        super(new BaseNodeFactory(),d);
        initialize(n, m);
    }

    private int randomInt(int n)
    {
        return (int) (Math.random() * n);
    }

    private void initialize(int n, int m)
    {
        @SuppressWarnings("unchecked")
        Vertex<BaseVertex> V[] = new Vertex[n];
        for (int i = 0; i < n; i++)
            V[i] = insertVertex();
        for (int i = 1; i < n; i++)
            insertEdge(V[randomInt(i - 1)], V[i]);
        for (int i = 0; i < m - (n - 1); i++)
        {
            outerLoop: while (true)
            {
                int from = randomInt(n - 1);
                int to = randomInt(n - 1);
                if (from >= to)
                    continue;
                for (int j = 0; j < n - 1 + i; j++)
                {
                    Edge<BaseVertex> e = getEdges().get(j);
                    if ((V[from] == e.getFrom()) && (V[to] == e.getTo()))
                        continue outerLoop;
                }
                insertEdge(V[from], V[to]);
                break;
            }
        }
    }
}
