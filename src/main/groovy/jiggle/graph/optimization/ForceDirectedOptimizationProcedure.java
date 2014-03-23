package jiggle.graph.optimization;

import jiggle.graph.JiggleObject;
import jiggle.graph.force.ForceModel;
import graphfx.model.Edge;
import graphfx.model.Graph;
import graphfx.model.Vertex;

public abstract class ForceDirectedOptimizationProcedure extends JiggleObject
{

    protected Graph<? extends Vertex<?>, ? extends Edge<? extends Vertex<?>>> graph;
    protected ForceModel forceModel;

    protected boolean constrained = false;

    public boolean getConstrained()
    {
        return constrained;
    }

    public void setConstrained(boolean c)
    {
        constrained = c;
    }

    ForceDirectedOptimizationProcedure(Graph<? extends Vertex<?>, ? extends Edge<? extends Vertex<?>>> g, ForceModel fm)
    {
        graph = g;
        forceModel = fm;
    }

    public abstract double improveGraph();
}
