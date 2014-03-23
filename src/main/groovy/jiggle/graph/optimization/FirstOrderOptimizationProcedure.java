package jiggle.graph.optimization;

import jiggle.graph.force.ForceModel;
import graphfx.model.Edge;
import graphfx.model.Graph;
import graphfx.model.Vertex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* Abstract base class for first-order graph-drawing optimization procedures.
 Includes concrete method for performing adaptive line search. */

public abstract class FirstOrderOptimizationProcedure extends
    ForceDirectedOptimizationProcedure
{

    private static final Logger log = LoggerFactory
        .getLogger(ForceDirectedOptimizationProcedure.class);

    protected double maxCos = 1;

    FirstOrderOptimizationProcedure(
        Graph<? extends Vertex<?>, ? extends Edge<? extends Vertex<?>>> g,
        ForceModel fm, double accuracy)
    {
        super(g, fm);
        maxCos = accuracy;
        stepInt = new StepIntegration(graph.getVertexNumber(),
            graph.getDimensions());
    }

    protected double negativeGradient[][] = null;
    protected double descentDirection[][] = null;
    protected double penaltyVector[][] = null;
    protected double penaltyFactor = 0;
    private boolean resetOptimization = false;
    private StepIntegration stepInt;
    boolean constrStep = false;

    public double improveGraph()
    {
        if (log.isDebugEnabled())
        {
            log.debug("Starting improveGraph");
        }
        int n = graph.getVertexNumber(), d = graph.getDimensions();
        if ((negativeGradient == null) || (negativeGradient.length != n))
        {
            negativeGradient = new double[n][d];
            penaltyVector = new double[n][d];
            getNegativeGradient();
        }
        if (constrStep != graph.isConstrained()) {
            reset();
            negativeGradient = new double[n][d];
            penaltyVector = new double[n][d];
            if(!graph.isConstrained()) 
                getNegativeGradient();
            constrStep = graph.isConstrained();
        }
        if (graph.isConstrained())
        {
            log.debug("Constrained");
            getNegativeGradient();
            return stepInt.integrate(graph, negativeGradient, 1);
        } else
        {
            computeDescentDirection();

            if (log.isDebugEnabled())
            {
                log.debug("ng " + negativeGradient);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < d; i++)
                {
                    for (int k = 0; k < n; k++)
                    {
                        sb.append(String
                            .format("%12e,", negativeGradient[k][i]));
                    }
                }
                log.debug("\n" + sb.toString());
                sb = new StringBuilder();
                log.debug("pv " + penaltyVector);
                for (int i = 0; i < d; i++)
                {
                    for (int k = 0; k < n; k++)
                    {
                        sb.append(String.format("%12e,", penaltyVector[k][i]));
                    }
                }
                log.debug("\n" + sb.toString());
                log.debug("Finished improveGraph");
            }

            return lineSearch();
        }
    }

    public void reset()
    {
        negativeGradient = null;
        penaltyFactor = 0;
    }

    private void computePenaltyFactor()
    {
        double m1 = l2Norm(negativeGradient);
        double m2 = l2Norm(penaltyVector);
        if (m2 == 0)
            penaltyFactor = 0;
        else if (m1 == 0)
            penaltyFactor = 1;
        else
        {
            double cos = dotProduct(negativeGradient, penaltyVector)
                / (m1 * m2);
            penaltyFactor = Math.max(0.00000001, (0.00000001 - cos))
                * Math.max(1, (m1 / m2));
        }
    }

    private void getNegativeGradient()
    {
        forceModel.getNegativeGradient(negativeGradient);
        if (constrained)
        {
            getPenaltyVector();
            computePenaltyFactor();
            int n = graph.getVertexNumber(), d = graph.getDimensions();
            for (int i = 0; i < n; i++)
            {
                for (int j = 0; j < d; j++)
                {
                    negativeGradient[i][j] += penaltyFactor
                        * penaltyVector[i][j];
                }
            }
        }
    }

    private void getPenaltyVector()
    {
        forceModel.getPenaltyVector(penaltyVector);
    }

    protected abstract void computeDescentDirection();

    private double stepSize = 0.1, previousStepSize = 0;

    protected double lineSearch()
    {
        log.debug("Line search");
        previousStepSize = 0;
        // int n = graph.numberOfVertices;
        double magDescDir = l2Norm(descentDirection);
        log.debug("magDescDir: " + magDescDir);
        if (magDescDir < 0.0001)
        {
            log.debug("Finished lineSearch");
            return 0;
        }
        // double magLo = l2Norm (negativeGradient);
        step();
        getNegativeGradient();
        double magHi = l2Norm(negativeGradient);
        double m = magDescDir * magHi;
        double cos = dotProduct(negativeGradient, descentDirection) / m;
        double lo = 0, hi = Double.MAX_VALUE;
        // int i = 0;
        log.debug("cos " + cos);
        while (((cos < 0) || (cos > maxCos)) && (hi - lo > 0.00000001))
        {
            if (cos < 0)
            {
                hi = stepSize;
                stepSize = (lo + hi) / 2;
            } else
            {
                if (hi < Double.MAX_VALUE)
                {
                    lo = stepSize;
                    stepSize = (lo + hi) / 2;
                } else
                {
                    lo = stepSize;
                    stepSize *= 2;
                }
            }
            step();
            getNegativeGradient();
            m = magDescDir * l2Norm(negativeGradient);
            cos = dotProduct(negativeGradient, descentDirection) / m;
        }
        return l2Norm(negativeGradient);
    }

    private void step()
    {
        // int n = graph.getVertexNumber();
        double s = stepSize - previousStepSize;
        log.debug("StepSize: " + s + "/" + stepSize);
        int i = 0;
        for (Vertex<?> v : graph.getVertices())
            v.translate(s, descentDirection[i++]);
        previousStepSize = stepSize;
    }

    protected double dotProduct(double[][] u, double[][] v)
    {
        int n = graph.getVertexNumber(), d = graph.getDimensions();
        double sum = 0;
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < d; j++)
            {
                sum += u[i][j] * v[i][j];
            }
        }
        return sum;
    }

    protected double l2Norm(double[][] vect)
    {
        return Math.sqrt(dotProduct(vect, vect));
    }

    protected double lInfinityNorm(double[][] vect)
    {
        int n = graph.getVertexNumber(), d = graph.getDimensions();
        double max = 0;
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < d; j++)
            {
                max = Math.max(max, Math.abs(vect[i][j]));
            }
        }
        return max;
    }

    public boolean isResetOptimization()
    {
        return resetOptimization;
    }

    public void setResetOptimization(boolean resetOptimization)
    {
        this.resetOptimization = resetOptimization;
    }
}