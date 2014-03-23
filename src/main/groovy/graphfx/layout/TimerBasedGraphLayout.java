/*
 * Copyright 2008-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package graphfx.layout;

import graphfx.model.Vertex;
import graphfx.ui.fx.GraphEvent;
import graphfx.ui.graph.FxGraph;
import graphfx.ui.graph.FxVertex;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import jfxtras.labs.scene.control.MiniIconButton.AnimationType;
import jiggle.graph.optimization.FirstOrderOptimizationProcedure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimerBasedGraphLayout extends SimpleGraphLayout implements
    EventHandler<ActionEvent>
{

    private static final Logger log = LoggerFactory
        .getLogger(TimerBasedGraphLayout.class);

    public static final double OPTIMIZER_THRESHOLD = 0.01;
    public static final int KEY_FRAME_NUM = 5;

    // This is the minimum cycle period to allow, to avoid jitter

    private static final int STATUS_STOPPED = 0;
    private static final int STATUS_RUNNING = 1;
    private static final int STATUS_PAUSED = 2;

    private ExecutorService executorPool = Executors.newFixedThreadPool(2);
    private Future<Double> optimizerControl;
    private AtomicInteger optimizerStatus = new AtomicInteger(STATUS_STOPPED);
    private AtomicInteger animationStatus = new AtomicInteger(STATUS_STOPPED);
    private SynchronousQueue<FxGraph> animationQueue;
    private GraphAnimation graphAnimation;

    private class GraphAnimation extends AnimationTimer
    {

        @Override
        public void handle(long now)
        {
            FxGraph graph = animationQueue.poll();
            if (graph != null)
            {
                graph.syncModelToNodes();
            }
        }

        @Override
        public void start()
        {
            super.start();
            animationStatus.set(STATUS_RUNNING);
        }

        @Override
        public void stop()
        {
            super.stop();
            animationStatus.set(STATUS_STOPPED);
        }

    }

    public TimerBasedGraphLayout(FxGraph graph)
    {
        super(graph);
        animationQueue = new SynchronousQueue<FxGraph>();
        graphAnimation = new GraphAnimation();
    }

    @Override
    public void initialize()
    {
        super.initialize();
        for (Vertex<FxVertex> vert : getGraph().getVertices())
        {
            FxVertex vertex = (FxVertex) vert;
            vertex.setUpdateNode(false);
            vertex.syncModelToNode();
        }
        getGraph().addEventHandler(GraphEvent.GRAPH_NODE_FIXED, this);
        getGraph().addEventHandler(GraphEvent.GRAPH_NODE_RELEASED, this);
    }

    @Override
    public void start()
    {
        if (animationStatus.compareAndSet(STATUS_STOPPED, STATUS_RUNNING))
        {
            if (optimizerStatus.compareAndSet(STATUS_STOPPED, STATUS_RUNNING))
            {
                log.debug("String optimizer");
                startImproveGraph();
            }
        }
        log.debug("Starting animation");
        graphAnimation.start();
        updateStatus(Animation.Status.RUNNING);
    }

    @Override
    public void stop()
    {
        if (animationStatus.compareAndSet(STATUS_RUNNING, STATUS_STOPPED)
            || animationStatus.compareAndSet(STATUS_PAUSED, STATUS_STOPPED))
        {
            log.debug("Stopping animation");
            graphAnimation.stop();
        }
        if (optimizerStatus.compareAndSet(STATUS_RUNNING, STATUS_STOPPED))
        {
            log.debug("Stopping optimizer");
            optimizerControl.cancel(true);
        }
        updateStatus(Animation.Status.STOPPED);
    }

    @Override
    public void pause()
    {
        if (animationStatus.compareAndSet(STATUS_RUNNING, STATUS_STOPPED))
        {
            log.debug("Stopping animation");
            graphAnimation.stop();
            updateStatus(Animation.Status.PAUSED);
        }
    }

    @Override
    public void resume()
    {
        if (animationStatus.compareAndSet(STATUS_PAUSED, STATUS_RUNNING))
        {
            log.debug("Starting animation");
            graphAnimation.start();
            updateStatus(Animation.Status.RUNNING);
        }
    }

    @Override
    public void scramble()
    {
        super.scramble();
        getGraph().syncModelToNodes();
    }

    /*
     * Start the graph layout improvement calculation.
     */
    protected void startImproveGraph()
    {
        if (optimizerControl == null || optimizerControl.isDone())
        {

            final FirstOrderOptimizationProcedure optimizer = super.optim;
            Callable<Double> optimizerTask = new Callable<Double>() {
                @Override
                public Double call() throws Exception
                {
                    try
                    {
                        FirstOrderOptimizationProcedure myOptim = optimizer;
                        Double result = Double.MAX_VALUE;
                        while (!(optimizerStatus.get() == STATUS_STOPPED))
                        {
                            try
                            {
                                final Double myResult = myOptim.improveGraph();
                                result = myResult;
                                animationQueue.put(graph);
                                if (getOptimizer() != myOptim)
                                {
                                    myOptim = getOptimizer();
                                }
                                Platform.runLater(new Runnable() {

                                    @Override
                                    public void run()
                                    {
                                        updateOptimizerResult(myResult);

                                    }
                                });
                            } catch (Exception ex)
                            {
                                log.error("Exception in improve graph: "
                                    + ex.getMessage());
                                ex.printStackTrace();
                            }
                        }
                        return result;
                    } finally
                    {
                        optimizerStatus.set(STATUS_STOPPED);
                    }
                }
            };
            optimizerControl = executorPool.submit(optimizerTask);
        }
    }

    private FirstOrderOptimizationProcedure getOptimizer()
    {
        return super.optim;
    }

    @Override
    public void updateModel()
    {
        super.updateModel();
    }

    @Override
    public void handle(ActionEvent arg0)
    {
        // TODO Auto-generated method stub

    }

}
