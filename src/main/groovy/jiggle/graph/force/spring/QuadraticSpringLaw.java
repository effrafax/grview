package jiggle.graph.force.spring;

import graphfx.model.Edge;
import graphfx.model.Graph;
import graphfx.model.Vertex;
import graphfx.model.impl.BaseNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuadraticSpringLaw extends SpringLaw
{
    private static final Logger log = LoggerFactory
        .getLogger(QuadraticSpringLaw.class);

    public QuadraticSpringLaw(Graph<? extends Vertex<?>, ? extends Edge<? extends Vertex<?>>> g, double k)
    {
        super(g, k);
    }

    double springAttraction(Edge<? extends Vertex<?>> e)
    {
        double r = BaseNode.sumOfRadii(e.getFrom(), e.getTo());
        double len = e.getLength();
        log.debug("Attraction len:"+len+" r:"+r+" pref:"+preferredEdgeLength);
        return (len - r) / preferredEdgeLength;
    }
}
