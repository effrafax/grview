package jiggle.graph.force.repulsion;

import graphfx.model.Edge;
import graphfx.model.Graph;
import graphfx.model.Node;
import graphfx.model.Vertex;
import graphfx.model.impl.BaseNode;

public class InverseSquareVertexEdgeRepulsionLaw extends VertexEdgeRepulsionLaw
{

    public InverseSquareVertexEdgeRepulsionLaw(Graph<? extends Vertex<?>, ? extends Edge<? extends Vertex<?>>> g, double k)
    {
        super(g, k, 1);
    }

    public InverseSquareVertexEdgeRepulsionLaw(Graph<? extends Vertex<?>, ? extends Edge<? extends Vertex<?>>> g, double k, double s)
    {
        super(g, k, s);
    }

    double pairwiseRepulsion(Node c1, Node c2)
    {
        double k = preferredEdgeLength + BaseNode.sumOfRadii(c1, c2);
        double d = BaseNode.getDistance(c1, c2);
        if (d >= k)
            return 0;
        else
            return cube(k / d) - k / d;
    }

    double pairwiseRepulsion(Node cell, double[] coords)
    {
        double k = preferredEdgeLength + BaseNode.radius(cell, coords);
        double d = BaseNode.getDistance(cell, coords);
        if (d >= k)
            return 0;
        else
            return cube(k / d) - k / d;
    }
}