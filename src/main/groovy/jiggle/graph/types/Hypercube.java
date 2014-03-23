package jiggle.graph.types;

import graphfx.model.Edge;
import graphfx.model.impl.BaseVertex;
import graphfx.model.Vertex;
import graphfx.model.impl.BaseGraph;
import graphfx.model.impl.BaseNodeFactory;


public class Hypercube extends BaseGraph<BaseVertex, Edge<BaseVertex>>
{

    public Hypercube(int n)
    {
        super(new BaseNodeFactory());
        initialize(n);
    }

    public Hypercube(int n, int d)
    {
        super(new BaseNodeFactory(),d);
        initialize(n);
    }

    private void initialize(int n)
    {
        int twoToTheN = (int) Math.pow(2, n);
        @SuppressWarnings("unchecked")
        Vertex<BaseVertex> V[] = new Vertex[twoToTheN];
        for (int i = 0; i < twoToTheN; i++)
        {
            V[i] = insertVertex();
        }
        for (int i = 0; i < twoToTheN; i++)
        {
            for (int j = 1; j < twoToTheN; j *= 2)
            {
                if ((i & j) == 0)
                    insertEdge(V[i], V[i + j]);
            }
        }
    }
}
