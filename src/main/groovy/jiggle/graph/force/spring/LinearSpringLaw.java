package jiggle.graph.force.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphfx.model.Edge;
import graphfx.model.Graph;
import graphfx.model.Vertex;
import graphfx.model.impl.BaseNode;

public class LinearSpringLaw extends SpringLaw
{
    
    private static final Logger log = LoggerFactory
        .getLogger(LinearSpringLaw.class);


    public LinearSpringLaw(Graph<? extends Vertex<?>, ? extends Edge<? extends Vertex<?>>> g, double k)
    {
        super(g, k);
    }

    double springAttraction(Edge<? extends Vertex<?>> e)
    {
        double r = BaseNode.sumOfRadii(e.getFrom(), e.getTo());
        log.debug("Attraction r:"+r+" len:"+e.getLength());

        if (r == 0)
            return 1;
        else
            return 1 - r / e.getLength();
    }
}
