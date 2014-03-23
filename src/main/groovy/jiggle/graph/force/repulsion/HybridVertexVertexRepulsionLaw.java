package jiggle.graph.force.repulsion;

import graphfx.model.Edge;
import graphfx.model.Graph;
import graphfx.model.Node;
import graphfx.model.Vertex;
import graphfx.model.impl.BaseNode;

public class HybridVertexVertexRepulsionLaw extends VertexVertexRepulsionLaw
{

    public HybridVertexVertexRepulsionLaw(Graph<? extends Vertex<?>, ? extends Edge<? extends Vertex<?>>> g, double k)
    {
        super(g, k);
    }

    double pairwiseRepulsion(Node c1, Node c2)
    {
        double r = BaseNode.sumOfRadii(c1, c2);
        double k = preferredEdgeLength + r;
        double dSquared = BaseNode.getDistanceSquared(c1, c2);
        if (dSquared < k * k)
            return k * k / dSquared;
        else
            return cube(k / BaseNode.getDistance(c1, c2));
    }
}