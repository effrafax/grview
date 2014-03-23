package jiggle.graph.types;

import graphfx.model.Edge;
import graphfx.model.impl.BaseVertex;
import graphfx.model.Vertex;
import graphfx.model.impl.BaseGraph;
import graphfx.model.impl.BaseNodeFactory;


public class TriangularMesh extends BaseGraph<BaseVertex, Edge<BaseVertex>>
{

    public TriangularMesh(int h)
    {
        super(new BaseNodeFactory());
        initialize(h);
    }

    public TriangularMesh(int h, int d)
    {
        super(new BaseNodeFactory(),d);
        initialize(h);
    }

    private void initialize(int h)
    {
        int n = (h * (h + 1)) / 2;
        @SuppressWarnings("unchecked")
        Vertex<BaseVertex> V[] = new Vertex[n];
        for (int i = 0; i < n; i++)
            V[i] = insertVertex();
        int cur = 0;
        for (int i = 0; i < h; i++)
        {
            for (int j = 0; j <= i; j++)
            {
                if (j < i)
                    insertEdge(V[cur], V[cur + 1]);
                if (i < h - 1)
                {
                    insertEdge(V[cur], V[cur + i + 1]);
                    insertEdge(V[cur], V[cur + i + 2]);
                }
                ++cur;
            }
        }
    }
}
