package jiggle.graph.force;

import jiggle.graph.constraints.ProjectionConstraint;
import jiggle.graph.force.repulsion.HybridVertexVertexRepulsionLaw;
import jiggle.graph.force.repulsion.VertexVertexRepulsionLaw;
import jiggle.graph.force.spring.QuadraticSpringLaw;
import jiggle.graph.force.spring.SpringLaw;
import graphfx.model.Edge;
import graphfx.model.Graph;
import graphfx.model.Vertex;

/* Class for standard force model of graph-drawing aesthetics. */

public class StandardForceModel extends ForceModel
{

    public StandardForceModel(
        Graph<? extends Vertex<?>, ? extends Edge<? extends Vertex<?>>> g,
        double k, double theta)
    {
        super(g);
        preferredEdgeLength = k;
        SpringLaw springLaw = new QuadraticSpringLaw(g, k);
        VertexVertexRepulsionLaw vvRepulsionLaw = new HybridVertexVertexRepulsionLaw(
            g, k);
        addForceLaw(springLaw);
        addForceLaw(vvRepulsionLaw);
        addConstraint(new ProjectionConstraint(g, 2));
    }
}