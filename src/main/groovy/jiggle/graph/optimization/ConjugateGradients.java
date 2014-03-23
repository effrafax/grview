package jiggle.graph.optimization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jiggle.graph.force.ForceModel;
import jiggle.graph.force.spring.SpringLaw;
import graphfx.model.Edge;
import graphfx.model.Graph;
import graphfx.model.Vertex;

/* Class for conjugate gradient method. */

public class ConjugateGradients extends FirstOrderOptimizationProcedure
{
    
    private static final Logger log = LoggerFactory
        .getLogger(ConjugateGradients.class);

    private double magnitudeOfPreviousGradientSquared;
    private double previousDescentDirection[][] = null;
    private double restartThreshold = 0;

    public ConjugateGradients(Graph<? extends Vertex<?>, ? extends Edge<? extends Vertex<?>>> g, ForceModel fm, double acc)
    {
        super(g, fm, acc);
        restartThreshold = 0;
    }

    public ConjugateGradients(Graph<? extends Vertex<?>, ? extends Edge<? extends Vertex<?>>>  g, ForceModel fm, double acc, double rt)
    {
        super(g, fm, acc);
        restartThreshold = rt;
    }

    public void reset()
    {
        negativeGradient = null;
        descentDirection = null;
    }

    protected void computeDescentDirection()
    {
        int n = graph.getVertexNumber(), d = graph.getDimensions();
        double magnitudeOfCurrentGradientSquared = 0;
        if ((descentDirection == null) || (descentDirection.length != n))
        {
            descentDirection = new double[n][d];
            previousDescentDirection = new double[n][d];
            for (int i = 0; i < n; i++)
            {
                for (int j = 0; j < d; j++)
                {
                    double temp = negativeGradient[i][j];
                    descentDirection[i][j] = temp;
                    magnitudeOfCurrentGradientSquared += square(temp);
                }
            }
        } else
        {
            for (int i = 0; i < n; i++)
            {
                for (int j = 0; j < d; j++)
                {
                    double temp = negativeGradient[i][j];
                    magnitudeOfCurrentGradientSquared += square(temp);
                }
            }
            log.debug("magGradSqu: "+magnitudeOfCurrentGradientSquared);
            if (magnitudeOfCurrentGradientSquared < 0.000001)
            {
                for (int i = 0; i < n; i++)
                {
                    for (int j = 0; j < d; j++)
                    {
                        previousDescentDirection[i][j] = 0;
                        descentDirection[i][j] = 0;
                    }
                }
                return;
            }
            double w = magnitudeOfCurrentGradientSquared
                    / magnitudeOfPreviousGradientSquared;
            double dotProduct = 0, magnitudeOfDescentDirectionSquared = 0, m;
            for (int i = 0; i < n; i++)
            {
                for (int j = 0; j < d; j++)
                {
                    descentDirection[i][j] = negativeGradient[i][j] + w
                            * previousDescentDirection[i][j];
                    dotProduct += descentDirection[i][j]
                            * negativeGradient[i][j];
                    magnitudeOfDescentDirectionSquared += square(descentDirection[i][j]);
                }
            }
            m = magnitudeOfCurrentGradientSquared
                    * magnitudeOfDescentDirectionSquared;
            log.debug("m: "+m+" magOfGradSqrt: "+magnitudeOfCurrentGradientSquared+" dotProd: "+dotProduct+ " restartThr:"+restartThreshold);
            if (dotProduct / Math.sqrt(m) < restartThreshold)
            {
                log.debug("restart");
                descentDirection = null;
                computeDescentDirection();
                return;
            }
        }
        magnitudeOfPreviousGradientSquared = magnitudeOfCurrentGradientSquared;
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < d; j++)
            {
                previousDescentDirection[i][j] = descentDirection[i][j];
            }
        }
    }
}