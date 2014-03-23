package jiggle.graph;

import graphfx.model.Edge;
import graphfx.model.Graph;
import graphfx.model.GraphElement;
import graphfx.model.Vertex;
import graphfx.model.impl.BaseNode;

public class QuadTree extends BaseNode implements GraphElement
{

    public QuadTree subtrees[];

    public double force[];

    public QuadTree(Graph<? extends Vertex<?>, ? extends Edge<? extends Vertex<?>>> g)
    {
        setContext(g);
        setObjectField(null);
        int d = g.getDimensions();
        setDimensions(d);
        subtrees = new QuadTree[power(2, d)];
        int n = g.getVertexNumber();
        setMin(g.getMin());
        setMax(g.getMax());
        for (int i = 0; i < n; i++)
            g.getVertices().get(i).setObjectField(null);
        for (int i = 0; i < n; i++)
            insert(g.getVertices().get(i));
        force = new double[d];
        for (int i = 0; i < d; i++)
            force[i] = 0;
    }

    public QuadTree(double[] min, double[] max, QuadTree p)
    {
        setContext(p);
        setObjectField(null);
        int d = p.getDimensions();
        setDimensions(d);
        subtrees = new QuadTree[power(2, d)];
        setMin(min);
        setMax(max);
        force = new double[d];
        for (int i = 0; i < d; i++)
            force[i] = 0;
    }

    public QuadTree lookUp(Vertex<?> v)
    {
        if (getObjectField() == v)
            return this;
        else if (getObjectField() != null)
            return null;
        else
            return subtrees[getIndex(v)].lookUp(v);
    }

    private int getIndex(Vertex<?> v)
    {
        double c[] = v.getCoords(), center[] = getCenter();
        int d = getDimensions(), index = 0, column = 1;
        for (int i = 0; i < d; i++)
        {
            if (c[i] > center[i])
                index += column;
            column *= 2;
        }
        return index;
    }

    private double[] getCenter()
    {
        int d = getDimensions();
        double mp[] = new double[d];
        double lo[] = getMin(), hi[] = getMax();
        for (int i = 0; i < d; i++)
            mp[i] = (lo[i] + hi[i]) / 2;
        return mp;
    }

    public void recomputeSize()
    {
    }

    public void recomputeBoundaries()
    {
    }

    /*
     * NOTE: Size for quadtrees has nothing to do with min and max! It stores
     * the average size of the vertices that have been inserted into the tree.
     */

    void insert(Vertex<?> v)
    {
        double w = getWeight(), vw = v.getWeight();
        int d = getDimensions();
        double vCoords[] = v.getCoords(), vSize[] = v.getSize();
        if (w == 0)
        {
            v.setContext(this);
            setWeight(v.getWeight());
            setCoords(vCoords);
            setSize(v.getSize());
            setObjectField(v);
            return;
        }
        if (getObjectField() != null)
            splitCell();
        double c[] = getCoords(), s[] = getSize();
        for (int i = 0; i < d; i++)
        {
            c[i] = (c[i] * w + vCoords[i] * vw) / (w + vw);
            s[i] = (s[i] * w + vSize[i] * vw) / (w + vw);
        }
        setWeight(w + vw);
        subtrees[getIndex(v)].insert(v);
    }

    private void splitCell()
    {
        Vertex<?> v = (Vertex<?>) getObjectField();
        setObjectField(null);
        double cellMin[] = getMin(), cellMax[] = getMax();
        double center[] = getCenter();
        int d = getDimensions(), n = power(2, d);
        double lo[] = new double[d], hi[] = new double[d];
        for (int index = 0; index < n; index++)
        {
            int column = 1;
            for (int i = 0; i < d; i++)
            {
                if ((index & column) > 0)
                {
                    lo[i] = center[i];
                    hi[i] = cellMax[i];
                } else
                {
                    lo[i] = cellMin[i];
                    hi[i] = center[i];
                }
                column *= 2;
            }
            subtrees[index] = new QuadTree(lo, hi, this);
        }
        subtrees[getIndex(v)].insert(v);
    }
}
