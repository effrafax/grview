package jiggle.graph.constraints;

import jiggle.graph.JiggleObject;
import graphfx.model.Edge;
import graphfx.model.Graph;
import graphfx.model.Vertex;

public abstract class Constraint extends JiggleObject
{

    protected Graph<? extends Vertex<?>, ? extends Edge<? extends Vertex<?>>> graph;

    protected Constraint(Graph<? extends Vertex<?>, ? extends Edge<? extends Vertex<?>>> g)
    {
        graph = g;
    }

    public abstract void apply(double[][] penalty);
}
