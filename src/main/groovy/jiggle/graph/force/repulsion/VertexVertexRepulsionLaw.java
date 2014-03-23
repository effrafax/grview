package jiggle.graph.force.repulsion;

import jiggle.graph.BaseForceLaw;
import jiggle.graph.QuadTree;
import graphfx.model.Edge;
import graphfx.model.Graph;
import graphfx.model.Node;
import graphfx.model.Vertex;
import graphfx.model.impl.BaseNode;

public abstract class VertexVertexRepulsionLaw extends BaseForceLaw
{

    protected double preferredEdgeLength;

    abstract double pairwiseRepulsion(Node c1, Node c2);

    private double barnesHutTheta = 0;

    protected VertexVertexRepulsionLaw(Graph<? extends Vertex<?>, ? extends Edge<? extends Vertex<?>>> g, double k)
    {
        super(g);
        preferredEdgeLength = k;
    }

    public double getBarnesHutTheta()
    {
        return barnesHutTheta;
    }

    public void setBarnesHutTheta(double t)
    {
        barnesHutTheta = t;
    }

    public void apply(double[][] negativeGradient)
    {
        if (barnesHutTheta > 0)
            applyUsingBarnesHut(negativeGradient);
        int n = graph.getVertexNumber(), d = graph.getDimensions();
        for (int i = 0; i < n - 1; i++)
        {
            Vertex<?> v1 = graph.getVertices().get(i);
            double v1Coords[] = v1.getCoords(), weight1 = v1.getWeight();
            for (int j = i + 1; j < n; j++)
            {
                Vertex<?> v2 = graph.getVertices().get(j);
                double w = Math.min(pairwiseRepulsion(v1, v2),
                    cap / BaseNode.getDistance(v1, v2));
                double v2Coords[] = v2.getCoords(), weight2 = v2.getWeight();
                for (int k = 0; k < d; k++)
                {
                    double force = (v1Coords[k] - v2Coords[k]) * w;
                    negativeGradient[i][k] += force * weight2;
                    negativeGradient[j][k] -= force * weight1;
                }
            }
        }
    }

    private void applyUsingBarnesHut(double[][] negativeGradient)
    {
        int n = graph.getVertexNumber(), d = graph.getDimensions();
        if (n <= 1)
            return;
        graph.recomputeBoundaries();
        QuadTree root = new QuadTree(graph);
        for (int i = 0; i < n; i++)
        {
            Vertex<?> v = graph.getVertices().get(i);
            QuadTree cur = (QuadTree) v.getContext();
            while (cur.getContext() != graph)
            {
                QuadTree p = (QuadTree) cur.getContext();
                int numberOfSubtrees = power(2, d);
                for (int j = 0; j < numberOfSubtrees; j++)
                {
                    QuadTree st = p.subtrees[j];
                    if (cur != st)
                    {
                        computeQTRepulsion(cur, st, negativeGradient);
                    }
                }
                cur = p;
            }
        }
        pushForcesDownTree(root);
        for (int i = 0; i < n; i++)
        {
            Vertex<?> v = graph.getVertices().get(i);
            QuadTree qt = (QuadTree) v.getContext();
            for (int j = 0; j < d; j++)
                negativeGradient[i][j] += qt.force[j];
            v.setContext(graph);
        }
    }

    private void computeQTRepulsion(
        QuadTree leaf, QuadTree cell, double[][] negativeGradient)
    {
        if (cell == null)
            return;
        int d = leaf.getDimensions();
        if ((cell.getObjectField() == null) && (!wellSeparated(leaf, cell)))
        {
            int numberOfSubtrees = power(2, d);
            for (int i = 0; i < numberOfSubtrees; i++)
            {
                computeQTRepulsion(leaf, cell.subtrees[i], negativeGradient);
            }
        } else
        {
            double w = Math.min(pairwiseRepulsion(leaf, cell),
                cap / BaseNode.getDistance(leaf, cell));
            double leafWeight = leaf.getWeight(), cellWeight = cell.getWeight();
            double leafCoords[] = leaf.getCoords(), cellCoords[] = cell
                .getCoords();
            int i = leaf.getIntField();
            for (int j = 0; j < d; j++)
            {
                double force = 0.5 * w * (leafCoords[j] - cellCoords[j]);
                negativeGradient[i][j] += force * cellWeight;
                cell.force[j] -= force * leafWeight;
            }
        }
    }

    private boolean wellSeparated(QuadTree leaf, QuadTree cell)
    {
        if (cell == null)
            throw new Error("cell == null");
        if (cell.getObjectField() != null)
            return true;
        else
        {
            int d = cell.getDimensions();
            double len = Double.MAX_VALUE;
            double lo[] = cell.getMin(), hi[] = cell.getMax();
            for (int i = 0; i < d; i++)
                len = Math.min(len, hi[i] - lo[i]);
            double dist = BaseNode.getDistance(leaf, cell);
            return ((len / dist) < barnesHutTheta);
        }
    }

    private void pushForcesDownTree(QuadTree qt)
    {
        if ((qt != null) && (qt.getObjectField() == null)
                && (qt.getWeight() > 0))
        {
            int d = qt.getDimensions(), numberOfSubtrees = power(2, d);
            for (int i = 0; i < numberOfSubtrees; i++)
            {
                for (int j = 0; j < d; j++)
                {
                    qt.subtrees[i].force[j] += qt.force[j];
                }
            }
            for (int i = 0; i < numberOfSubtrees; i++)
            {
                pushForcesDownTree(qt.subtrees[i]);
            }
        }
    }
}
