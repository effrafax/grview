package jiggle.graph.force.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jiggle.graph.BaseForceLaw;
import jiggle.graph.optimization.ForceDirectedOptimizationProcedure;
import graphfx.model.Edge;
import graphfx.model.Graph;
import graphfx.model.Vertex;

public abstract class SpringLaw extends BaseForceLaw
{
    private static final Logger log = LoggerFactory
        .getLogger(SpringLaw.class);

    protected double preferredEdgeLength;

    protected SpringLaw(Graph<? extends Vertex<?>, ? extends Edge<? extends Vertex<?>>> g, double k)
    {
        super(g);
        preferredEdgeLength = k;
    }

    public void apply(double[][] negativeGradient)
    {
        int m = graph.getEdgeNumber(), d = graph.getDimensions();
        for (int i = 0; i < m; i++)
        {
            Edge<? extends Vertex<?>> e = graph.getEdges().get(i);
            Vertex<?> from = e.getFrom(), to = e.getTo();
            double fromWeight = from.getWeight(), toWeight = to.getWeight();
            int f = from.getIntField(), t = to.getIntField();
            log.debug("Forces SAttr: "+springAttraction(e)+" cap:"+cap+" len:"+e.getLength());
            double w = Math.min(springAttraction(e), cap / e.getLength());
            double fromCoords[] = from.getCoords();
            double toCoords[] = to.getCoords();
            for (int j = 0; j < d; j++)
            {
                double force = (toCoords[j] - fromCoords[j]) * w;
                negativeGradient[f][j] += force * toWeight;
                negativeGradient[t][j] -= force * fromWeight;
            }
        }
    }

    abstract double springAttraction(Edge<? extends Vertex<?>> e);
}
