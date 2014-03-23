package jiggle.graph.types;

import graphfx.model.Edge;
import graphfx.model.impl.BaseVertex;
import graphfx.model.Vertex;
import graphfx.model.impl.BaseGraph;
import graphfx.model.impl.BaseNodeFactory;

public class CycleOfCliques extends BaseGraph<BaseVertex, Edge<BaseVertex>>
{

    public CycleOfCliques(int numberOfCliques, int sizeOfClique)
    {
        super(new BaseNodeFactory());
        initialize(numberOfCliques, sizeOfClique);
    }

    public CycleOfCliques(int numberOfCliques, int sizeOfClique, int d)
    {
        super(new BaseNodeFactory(),d);
        initialize(numberOfCliques, sizeOfClique);
    }

    private void initialize(int numberOfCliques, int sizeOfClique)
    {
        int n = numberOfCliques * sizeOfClique;
        @SuppressWarnings("unchecked")
        Vertex<BaseVertex> V[] = new Vertex[n];
        for (int i = 0; i < n; i++)
            V[i] = insertVertex();
        for (int i = 0; i < numberOfCliques; i++)
        {
            int cur = i * sizeOfClique;
            if (i < numberOfCliques - 1)
                insertEdge(V[cur], V[cur + sizeOfClique]);
            else
                insertEdge(V[cur], V[0]);
            for (int j = cur + 1; j < cur + sizeOfClique; j++)
            {
                for (int k = cur; k < j; k++)
                {
                    insertEdge(V[k], V[j]);
                }
            }
        }
    }
}
