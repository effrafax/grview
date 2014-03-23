package jiggle.graph.force.repulsion;

import graphfx.model.Edge;
import graphfx.model.Graph;
import graphfx.model.Node;
import graphfx.model.Vertex;
import graphfx.model.impl.BaseNode;

public class InverseVertexEdgeRepulsionLaw extends VertexEdgeRepulsionLaw
{

    public InverseVertexEdgeRepulsionLaw(Graph<? extends Vertex<?>, ? extends Edge<? extends Vertex<?>>> g, double k)
    {
        super(g, k, 1);
    }

    public InverseVertexEdgeRepulsionLaw(Graph<? extends Vertex<?>, ? extends Edge<? extends Vertex<?>>> g, double k, double s)
    {
        super(g, k, s);
    }

    double pairwiseRepulsion(Node c1, Node c2)
    {
        double k = preferredEdgeLength + BaseNode.sumOfRadii(c1, c2);
        double dSquared = BaseNode.getDistanceSquared(c1, c2);
        if (dSquared >= square(k))
            return 0;
        else
            return k * k / dSquared - k / Math.sqrt(dSquared);
    }

    double pairwiseRepulsion(Node cell, double[] coords)
    {
        double k = preferredEdgeLength + BaseNode.radius(cell, coords);
        double dSquared = BaseNode.getDistanceSquared(cell, coords);
        if (dSquared >= square(k))
            return 0;
        else
            return k * k / dSquared - k / Math.sqrt(dSquared);
    }
}