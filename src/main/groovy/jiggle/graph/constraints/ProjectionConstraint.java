package jiggle.graph.constraints;

import graphfx.model.Edge;
import graphfx.model.Graph;
import graphfx.model.Vertex;

public class ProjectionConstraint extends Constraint
{

    private int dimensions = 0;

    public ProjectionConstraint(Graph<? extends Vertex<?>, ? extends Edge<? extends Vertex<?>>> g, int d)
    {
        super(g);
        dimensions = d;
    }

    public void apply(double[][] penalty)
    {
        int d = graph.getDimensions();
        int n = graph.getVertexNumber();
        for (int i = 0; i < n; i++)
        {
            double coords[] = graph.getVertices().get(i).getCoords();
            for (int j = dimensions; j < d; j++)
            {
                penalty[i][j] += (-coords[j]);
            }
        }
    }
}
