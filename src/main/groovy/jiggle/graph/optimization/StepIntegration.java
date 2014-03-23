package jiggle.graph.optimization;

import graphfx.model.Edge;
import graphfx.model.Graph;
import graphfx.model.Vertex;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StepIntegration
{
    
    private static final Logger log = LoggerFactory
        .getLogger(StepIntegration.class);

    
    private double speedLimit = 1000.0;
    private double[][] velocity;
    int size;
    int dim;

    public StepIntegration(int size, int dim)
    {
        initialize(size,dim);
        this.size = size;
        this.dim = dim;
    }
    
    public void initialize(int size, int dim) {
        velocity = new double[size][dim];
        for (int i=0; i<size; i++) {
            for (int k=0; k<dim; k++) {
                velocity[i][k] = 0;
            }
        }
    }
    
    public double integrate(Graph<? extends Vertex<?>,? extends Edge<? extends Vertex<?>>> graph, double[][] gradient, long timestep) {
        List<? extends Vertex<?>> iter = graph.getVertices();
        int i=0;
        double avSpeed = 0.0;
        for (Vertex<?> vertex : iter)
        {
            double[] co = new double[graph.getDimensions()];
            double[] orgCoords = vertex.getCoords();
            double speed=0.0;
            for (int v=0; v<dim; v++) {
                co[v] = orgCoords[v] + 0.01*velocity[i][v];
                velocity[i][v] -= gradient[i][v];
                speed += velocity[i][v]*velocity[i][v];
                log.debug("Speed: "+speed+" Velocity: "+velocity[i][v]);
            }
            speed = Math.sqrt(speed);
            if (speed > speedLimit) {
                for(int v=0; v<dim; v++) {
                    velocity[i][v] = speedLimit*velocity[i][v]/speed;
                }
            }
            vertex.setCoords(co);
            avSpeed+=speed/size;
        }
        log.debug("Speed: "+avSpeed);
        return avSpeed;
    }

}
