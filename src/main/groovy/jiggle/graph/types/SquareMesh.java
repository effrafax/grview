package jiggle.graph.types;

import graphfx.model.Edge;
import graphfx.model.impl.BaseVertex;
import graphfx.model.Vertex;
import graphfx.model.impl.BaseGraph;
import graphfx.model.impl.BaseNodeFactory;



public class SquareMesh extends BaseGraph<BaseVertex, Edge<BaseVertex>>
{

    public SquareMesh(int n)
    {
        super(new BaseNodeFactory());
        initialize(n);
    }

    public SquareMesh(int n, int d)
    {
        super(new BaseNodeFactory(),d);
        initialize(n);
    }

    private void initialize(int n)
    {
        @SuppressWarnings("unchecked")
        Vertex<BaseVertex> grid[][] = new Vertex[n][n];
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                grid[i][j] = insertVertex();
            }
        }
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n - 1; j++)
            {
                insertEdge(grid[i][j], grid[i][j + 1]);
                insertEdge(grid[j][i], grid[j + 1][i]);
            }
        }
    }
}
