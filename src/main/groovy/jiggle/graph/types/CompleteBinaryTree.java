package jiggle.graph.types;

import graphfx.model.Edge;
import graphfx.model.impl.BaseVertex;
import graphfx.model.Vertex;
import graphfx.model.impl.BaseGraph;
import graphfx.model.impl.BaseNodeFactory;



public class CompleteBinaryTree extends BaseGraph<BaseVertex,Edge<BaseVertex>>
{

    public CompleteBinaryTree(int h)
    {
        super(new BaseNodeFactory());
        initialize(h);
    }

    public CompleteBinaryTree(int h, int d)
    {
        super(new BaseNodeFactory(),d);
        initialize(h);
    }

    private void initialize(int h)
    {
        int twoToTheHMinusOne = (int) Math.pow(2, h) - 1;
        @SuppressWarnings("unchecked")
        Vertex<BaseVertex> V[] = new Vertex[twoToTheHMinusOne];
        for (int i = 0; i < twoToTheHMinusOne; i++)
            V[i] = insertVertex();
        for (int i = 1; i < twoToTheHMinusOne; i++)
            insertEdge(V[(i - 1) / 2], V[i]);
    }
}
