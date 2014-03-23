package jiggle.graph.force;

public interface ForceLaw
{

    public abstract void setCap(double c);

    public abstract double getCap();

    public abstract void apply(double[][] negativeGradient);

}