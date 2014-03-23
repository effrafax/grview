package jiggle.graph;

import jiggle.graph.force.ForceLaw;
import graphfx.model.Edge;
import graphfx.model.Graph;
import graphfx.model.Vertex;

public abstract class BaseForceLaw extends JiggleObject implements ForceLaw
{

    @Override
    public abstract void apply(double[][] negativeGradient);

    protected Graph<? extends Vertex<?>, ? extends Edge<? extends Vertex<?>>> graph;

    protected BaseForceLaw(Graph<? extends Vertex<?>, ? extends Edge<? extends Vertex<?>>> g)
    {
        graph = g;
    }

    protected double cap = Double.MAX_VALUE / 1000;

    @Override
    public double getCap()
    {
        return cap;
    }

    @Override
    public void setCap(double c)
    {
        cap = c;
    }
}
