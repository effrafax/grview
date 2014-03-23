package jiggle.graph;

import graphfx.model.Vertex;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class VertexSet extends JiggleObject implements Set<Vertex<?>>
{

    private Set<Vertex<?>> vertices;

    public int size()
    {
        return vertices.size();
    }

    public boolean isEmpty()
    {
        return vertices.isEmpty();
    }

    public boolean contains(Object o)
    {
        return vertices.contains(o);
    }

    public Object[] toArray()
    {
        return vertices.toArray();
    }

    public <T> T[] toArray(T[] a)
    {
        return vertices.toArray(a);
    }

    public boolean add(Vertex<?> e)
    {
        return vertices.add(e);
    }

    public boolean remove(Object o)
    {
        return vertices.remove(o);
    }

    public boolean containsAll(Collection<?> c)
    {
        return vertices.containsAll(c);
    }

    public boolean addAll(Collection<? extends Vertex<?>> c)
    {
        return vertices.addAll(c);
    }

    public boolean retainAll(Collection<?> c)
    {
        return vertices.retainAll(c);
    }

    public boolean removeAll(Collection<?> c)
    {
        return vertices.removeAll(c);
    }

    public void clear()
    {
        vertices.clear();
    }

    public boolean equals(Object o)
    {
        return vertices.equals(o);
    }

    public int hashCode()
    {
        return vertices.hashCode();
    }

    public VertexSet()
    {
        vertices = new HashSet<Vertex<?>>();
    }
    
    public VertexSet(Vertex<?> v)
    {
        vertices = new HashSet<Vertex<?>>();
        vertices.add(v);
    }

    public Iterator<Vertex<?>> iterator()
    {
        return vertices.iterator();
    }
}