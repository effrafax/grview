package jiggle.graph.force;

import graphfx.model.Edge;
import graphfx.model.Graph;
import graphfx.model.Vertex;

import java.util.Enumeration;
import java.util.Vector;

import jiggle.graph.constraints.Constraint;

public class ForceModel
{

    protected Graph<? extends Vertex<?>, ? extends Edge<? extends Vertex<?>>> graph = null;
    protected double preferredEdgeLength;

    private Vector<ForceLaw> baseForceLaws = new Vector<ForceLaw>();
    private Vector<Constraint> constraints = new Vector<Constraint>();

    public ForceModel(Graph<? extends Vertex<?>, ? extends Edge<? extends Vertex<?>>> g)
    {
        graph = g;
    }

    double getPreferredEdgeLength()
    {
        return preferredEdgeLength;
    }

    void setPreferredEdgeLength(double k)
    {
        preferredEdgeLength = k;
    }

    public void addForceLaw(ForceLaw fl)
    {
        baseForceLaws.addElement(fl);
    }

    public void removeForceLaw(ForceLaw fl)
    {
        baseForceLaws.removeElement(fl);
    }

    public void addConstraint(Constraint c)
    {
        constraints.addElement(c);
    }

    public void removeConstraint(Constraint c)
    {
        constraints.removeElement(c);
    }

    public void getNegativeGradient(double[][] negativeGradient)
    {
        int n = graph.getVertexNumber(), d = graph.getDimensions();
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < d; j++)
            {
                negativeGradient[i][j] = 0;
            }
            graph.getVertices().get(i).setIntField(i);
        }
        for (Enumeration<ForceLaw> en = baseForceLaws.elements(); en
            .hasMoreElements();)
            ((ForceLaw) (en.nextElement())).apply(negativeGradient);
    }

    public void getPenaltyVector(double[][] penaltyVector)
    {
        int n = graph.getVertexNumber(), d = graph.getDimensions();
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < d; j++)
            {
                penaltyVector[i][j] = 0;
            }
            graph.getVertices().get(i).setIntField(i);
        }
        for (Enumeration<Constraint> en = constraints.elements(); en
            .hasMoreElements();)
            ((Constraint) (en.nextElement())).apply(penaltyVector);
    }
}