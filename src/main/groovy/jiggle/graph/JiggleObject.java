package jiggle.graph;

import graphfx.model.GraphElement;

/* Abstract base class for all JIGGLE objects. */

public abstract class JiggleObject
{

    private GraphElement context = null;

    public GraphElement getContext()
    {
        return context;
    }

    public void setContext(GraphElement c)
    {
        context = c;
    }

    /*
     * The context of a JiggleObject identifies the parent JiggleObject (if any)
     * that contains it. The context of a Vertex or Cell is either a Graph or a
     * Cell; the context of an Edge is a Graph; the context of an EdgeLabel is
     * an Edge. For now, we assume that the context of a Graph is null; if,
     * however, we extend the present implementation to include composite
     * graphs, then the context of a Graph could be a JiggleObject (e.g. a
     * Vertex) that contains the graph inside it.
     */

    private boolean booleanField = false;
    private int intField = 0;
    private Object objectField = null;

    static public double square(double d)
    {
        return d * d;
    }

    static public double cube(double d)
    {
        return d * d * d;
    }

    static public int intSquare(int n)
    {
        return n * n;
    }

    static public int power(int base, int d)
    {
        if (d == 0)
            return 1;
        else if (d == 1)
            return base;
        else if (d % 2 == 0)
            return intSquare(power(base, d / 2));
        else
            return base * intSquare(power(base, d / 2));
    }

    public boolean isBooleanField()
    {
        return booleanField;
    }

    public void setBooleanField(boolean booleanField)
    {
        this.booleanField = booleanField;
    }

    public int getIntField()
    {
        return intField;
    }

    public void setIntField(int intField)
    {
        this.intField = intField;
    }

    public Object getObjectField()
    {
        return objectField;
    }

    public void setObjectField(Object objectField)
    {
        this.objectField = objectField;
    }
}