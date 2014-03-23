package jiggle.graph.optimization;

import graphfx.model.Edge;
import graphfx.model.Graph;
import graphfx.model.Vertex;
import jiggle.graph.force.ForceModel;

/* Class for method of steepest descent. */

public class SteepestDescent extends FirstOrderOptimizationProcedure
{

    public SteepestDescent(Graph<? extends Vertex<?>, ? extends Edge<? extends Vertex<?>>> g, ForceModel fm, double accuracy)
    {
        super(g, fm, accuracy);
    }

    protected void computeDescentDirection()
    {
        int n = graph.getVertexNumber(), d = graph.getDimensions();
        if ((descentDirection == null) || (descentDirection.length != n))
            descentDirection = new double[n][d];
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < d; j++)
            {
                descentDirection[i][j] = negativeGradient[i][j];
            }
        }
    }
}