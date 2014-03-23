package jiggle.graph.force.repulsion;

import graphfx.model.Edge;
import graphfx.model.Graph;
import graphfx.model.Node;
import graphfx.model.Vertex;
import graphfx.model.impl.BaseNode;

public class InverseSquareVertexVertexRepulsionLaw extends
        VertexVertexRepulsionLaw
{

    public InverseSquareVertexVertexRepulsionLaw(Graph<? extends Vertex<?>, ? extends Edge<? extends Vertex<?>>> g, double k)
    {
        super(g, k);
    }

    double pairwiseRepulsion(Node c1, Node c2)
    {
        double k = preferredEdgeLength + BaseNode.sumOfRadii(c1, c2);
        return cube(k / BaseNode.getDistance(c1, c2));
    }
}