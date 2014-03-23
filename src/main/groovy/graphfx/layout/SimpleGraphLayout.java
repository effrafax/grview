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

import static graphfx.ui.StandardForceLaw.HYBRID_VERTEX_VERTEX_REPULSION_LAW;
import static graphfx.ui.StandardForceLaw.LINEAR_SPRING_LAW;
import graphfx.model.Graph;
import graphfx.model.Vertex;
import graphfx.ui.ForceLawFactory;
import graphfx.ui.ForceLawId;
import graphfx.ui.StandardForceLaw;
import graphfx.ui.graph.FxEdge;
import graphfx.ui.graph.FxGraph;
import graphfx.ui.graph.FxVertex;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import jiggle.graph.force.ForceLaw;
import jiggle.graph.force.ForceModel;
import jiggle.graph.optimization.ConjugateGradients;
import jiggle.graph.optimization.FirstOrderOptimizationProcedure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleGraphLayout implements ListChangeListener<ForceLawId>,
    ChangeListener<Number>, GraphLayout
{

    public static final long INITIAL_PERIOD = 100;
    public static final int INITIAL_EDGE_LENGTH = 20;
    public static final double INITIAL_ACCURACY = 0.0001;

    private static final Logger log = LoggerFactory
        .getLogger(SimpleGraphLayout.class);

    private final AtomicReference<Task<Integer>> optimizer = new AtomicReference<>();

    protected FirstOrderOptimizationProcedure optim;
    protected FxGraph graph;
    private final ObservableList<ForceLawId> appliedForces = FXCollections
        .observableArrayList();

    private final DoubleProperty cyclePeriod = new SimpleDoubleProperty(
        INITIAL_PERIOD);
    protected final AtomicLong cyclePeriodValue = new AtomicLong(INITIAL_PERIOD);

    protected final DoubleProperty edgeLength = new SimpleDoubleProperty(
        INITIAL_EDGE_LENGTH);
    private final AtomicInteger edgeLengthValue = new AtomicInteger(
        INITIAL_EDGE_LENGTH);

    protected final DoubleProperty accuracy = new SimpleDoubleProperty(
        INITIAL_ACCURACY);

    private final ObservableList<ForceLawId> availableForces = FXCollections
        .observableArrayList(getAvailabeForceLaws());

    private final DoubleProperty regionX = new SimpleDoubleProperty(0);
    private final DoubleProperty regionY = new SimpleDoubleProperty(0);
    private final DoubleProperty regionWidth = new SimpleDoubleProperty(10);
    private final DoubleProperty regionHeight = new SimpleDoubleProperty(10);

    protected final ReadOnlyDoubleWrapper optimizerResult = new ReadOnlyDoubleWrapper();

    protected final ReadOnlyObjectWrapper<Animation.Status> status = new ReadOnlyObjectWrapper<>(
        Animation.Status.STOPPED);

    public SimpleGraphLayout(FxGraph graph)
    {
        super();
        this.graph = graph;
    }

    /*
     * (non-Javadoc)
     * 
     * @see graphfx.layout.GraphLayout#edgeLengthProperty()
     */
    @Override
    public final DoubleProperty edgeLengthProperty()
    {
        return edgeLength;
    }

    public double getEdgeLength()
    {
        return edgeLength.doubleValue();
    }

    public void setEdgeLength(final double value)
    {
        edgeLength.set(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see graphfx.layout.GraphLayout#cyclePeriodProperty()
     */
    @Override
    public final DoubleProperty cyclePeriodProperty()
    {
        return cyclePeriod;
    }

    public void setCyclePeriod(double time)
    {
        cyclePeriod.set(time);
    }

    public double getCyclePeriod()
    {
        return cyclePeriod.get();
    }

    protected ForceLawId[] getAvailabeForceLaws()
    {
        return StandardForceLaw.values();
    }

    protected ForceLaw createForceLaw(Graph<FxVertex,FxEdge> g, double k, String forceId)
    {
        return ForceLawFactory.createForceLaw(g, k,
            StandardForceLaw.valueOf(forceId));
    }

    public void initialize()
    {
        log.debug("Base Initialization");
        appliedForces.addListener(this);
        appliedForces.add(LINEAR_SPRING_LAW);
        appliedForces.add(HYBRID_VERTEX_VERTEX_REPULSION_LAW);
        log.debug("Applied Forces: " + appliedForces);
        updateModel();
        cyclePeriod.addListener(this);
        edgeLength.addListener(this);
    }

    public void updateModel()
    {
        log.debug("Updating Model");
        ForceModel fm = initializeForces(graph);
        log.debug("Force Model: " + fm.toString() + " Accuracy: "
            + accuracy.get());
        optim = new ConjugateGradients(graph, fm, accuracy.get(),1.0);
    }

    private ForceModel initializeForces(Graph<FxVertex,FxEdge> g)
    {
        log.debug("Initializing Forces");
        double k = getEdgeLength();
        ForceModel fm = new ForceModel(g);
        addForceLaws(fm, g, k);
        return fm;
    }

    private void addForceLaws(ForceModel fm, Graph<FxVertex,FxEdge> g, double k)
    {
        for (ForceLawId force : appliedForces)
        {
            fm.addForceLaw(createForceLaw(g, k, force.getName()));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see graphfx.layout.GraphLayout#getAppliedForces()
     */
    @Override
    public ObservableList<ForceLawId> getAppliedForces()
    {
        return appliedForces;
    }

    /*
     * (non-Javadoc)
     * 
     * @see graphfx.layout.GraphLayout#getAvailableForces()
     */
    @Override
    public ObservableList<ForceLawId> getAvailableForces()
    {
        return availableForces;
    }

    /*
     * (non-Javadoc)
     * 
     * @see graphfx.layout.GraphLayout#getGraph()
     */
    @Override
    public FxGraph getGraph()
    {
        return graph;
    }

    /*
     * (non-Javadoc)
     * 
     * @see graphfx.layout.GraphLayout#start()
     */
    @Override
    public void start()
    {
        if (optimizer.get() == null || !optimizer.get().isRunning())
        {
            Task<Integer> runnable = new Task<Integer>() {

                public Integer call()
                {
                    int i = 0;
                    while (i++ < 1000)
                    {
                        long startTime = System.currentTimeMillis();

                        log.debug("round " + i);
                        if (isCancelled())
                        {
                            return i;
                        }
                        log.debug("improve start: " + startTime);
                        final double result = optim.improveGraph();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run()
                            {
                                updateOptimizerResult(result);
                            }
                        });
                        log.debug("improve end: "
                            + (System.currentTimeMillis() - startTime));
                        long period = cyclePeriodValue.get();
                        while ((System.currentTimeMillis() - startTime) < period)
                        {
                            period = cyclePeriodValue.get();
                            log.debug("Period: " + cyclePeriodValue.get());
                            try
                            {
                                if (isCancelled())
                                {
                                    return i;
                                }
                                Thread.sleep(period
                                    - (System.currentTimeMillis() - startTime));
                                log.debug("sleep end "
                                    + (System.currentTimeMillis() - startTime));
                            } catch (InterruptedException e)
                            {
                                if (isCancelled())
                                {
                                    return i;
                                }
                            }
                        }
                    }
                    return i;
                }
            };
            Thread co = new Thread(runnable);
            optimizer.set(runnable);
            co.start();
            updateStatus(Status.RUNNING);
            log.debug("Thread started: " + co + " " + co.getState());
        }
    }

    protected void updateStatus(Status status)
    {
        this.status.set(status);
    }

    @Override
    public void stop()
    {
        log.debug("Cancelling");
        try
        {
            if (optimizer.get() != null && optimizer.get().isRunning())
                optimizer.get().cancel();
        } finally
        {
            updateStatus(Status.STOPPED);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see graphfx.layout.GraphLayout#pause()
     */
    @Override
    public void pause()
    {
        log.debug("Pausing");
        if (optimizer.get() != null && optimizer.get().isRunning())
            optimizer.get().cancel();
        updateStatus(Status.PAUSED);
    }

    /*
     * (non-Javadoc)
     * 
     * @see graphfx.layout.GraphLayout#resume()
     */
    @Override
    public void resume()
    {
        start();
        updateStatus(Status.RUNNING);
    }

    @Override
    public ReadOnlyObjectProperty<Status> statusProperty()
    {
        return status.getReadOnlyProperty();
    }

    /*
     * (non-Javadoc)
     * 
     * @see graphfx.layout.GraphLayout#scramble()
     */
    @Override
    public void scramble()
    {
        stop();
        while (optimizer.get() != null && optimizer.get().isRunning())
        {
            log.debug("Running");
        }
        updateModel();
        double[] region = getInitialRegion();
        for (Vertex<FxVertex> vertex : graph.getVertices())
        {
            vertex.setCoords(new double[] {
                region[0] + Math.random() * region[2],
                region[1] + Math.random() * region[3] });
        }
    }

    public double[] getInitialRegion()
    {
        return new double[] { getRegionX(), getRegionY(), getRegionWidth(),
            getRegionHeight() };
    }

    /**
     * Change listener for the appliedForces list to update the availableForces
     * list accordingly
     */
    @Override
    public
        void
        onChanged(
            javafx.collections.ListChangeListener.Change<? extends ForceLawId> change)
    {
        log.debug("Force Change: " + change.getList());
        while (change.next())
        {
            if (change.wasAdded())
            {
                availableForces.removeAll(change.getAddedSubList());
            }
            if (change.wasRemoved())
            {
                availableForces.addAll(change.getRemoved());
            }
        }
        updateModel();

    }

    /**
     * 
     * Updates the atomic variables of cyclePeriod and edgeLength for concurrent
     * access to these values.
     * 
     * @see javafx.beans.value.ChangeListener#changed(javafx.beans.value.ObservableValue,
     *      java.lang.Object, java.lang.Object)
     */
    @Override
    public void changed(
        ObservableValue<? extends Number> value, Number oldVal, Number newVal)
    {
        log.debug("Changed: " + value + " cyclePeriod: " + cyclePeriod);
        log.debug("Old: " + oldVal + " New: " + newVal);
        if (value == cyclePeriod)
        {
            long newCycleVal = newVal.longValue();
            cyclePeriodValue.set(newCycleVal <= 0 ? 1 : newCycleVal);
        } else if (value == edgeLength)
        {
            int newLengthVal = newVal.intValue();
            edgeLengthValue.set(newLengthVal <= 0 ? 1 : newLengthVal);
            updateModel();
        } 

    }

    // Sets the result value of the optimizer
    // It assumes that it is running on the UI thread
    protected void updateOptimizerResult(double result)
    {
        optimizerResult.set(result);
    }

    /*
     * (non-Javadoc)
     * 
     * @see graphfx.layout.GraphLayout#regionXProperty()
     */
    @Override
    public DoubleProperty regionXProperty()
    {
        return regionX;
    }

    /*
     * (non-Javadoc)
     * 
     * @see graphfx.layout.GraphLayout#regionYProperty()
     */
    @Override
    public DoubleProperty regionYProperty()
    {
        return regionY;
    }

    /*
     * (non-Javadoc)
     * 
     * @see graphfx.layout.GraphLayout#regionWidthProperty()
     */
    @Override
    public DoubleProperty regionWidthProperty()
    {
        return regionWidth;
    }

    /*
     * (non-Javadoc)
     * 
     * @see graphfx.layout.GraphLayout#regionHeightProperty()
     */
    @Override
    public DoubleProperty regionHeightProperty()
    {
        return regionHeight;
    }

    public void setRegionX(double value)
    {
        regionX.set(value);
    }

    public double getRegionX()
    {
        return regionX.getValue();
    }

    public void setRegionY(double value)
    {
        regionY.set(value);
    }

    public double getRegionY()
    {
        return regionY.getValue();
    }

    public double getRegionWidth()
    {
        return regionWidth.getValue();
    }

    public void setRegionWidth(double value)
    {
        regionWidth.set(value);
    }

    public double getRegionHeight()
    {
        return regionHeight.getValue();
    }

    public void setRegionHeight(double value)
    {
        regionHeight.set(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see graphfx.layout.GraphLayout#optimizerResultProperty()
     */
    @Override
    public ReadOnlyDoubleProperty optimizerResultProperty()
    {
        return optimizerResult.getReadOnlyProperty();
    }

    public double getOptimizerResult()
    {
        return optimizerResult.get();
    }

}
