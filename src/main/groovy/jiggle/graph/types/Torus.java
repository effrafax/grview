package jiggle.graph.types;

import graphfx.model.Edge;
import graphfx.model.impl.BaseVertex;
import graphfx.model.Vertex;
import graphfx.model.impl.BaseGraph;
import graphfx.model.impl.BaseNodeFactory;



public class Torus extends BaseGraph<BaseVertex,Edge<BaseVertex>>
{

    public Torus(int n)
    {
        super(new BaseNodeFactory());
        initialize(n);
    }

    public Torus(int n, int d)
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
            insertEdge(grid[0][i], grid[n - 1][i]);
            insertEdge(grid[i][0], grid[i][n - 1]);
        }
    }
}
