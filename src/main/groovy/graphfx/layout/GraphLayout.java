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

import graphfx.model.Graph;
import graphfx.ui.ForceLawId;
import graphfx.ui.graph.FxEdge;
import graphfx.ui.graph.FxVertex;
import javafx.animation.Animation;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;

public interface GraphLayout
{

    public DoubleProperty edgeLengthProperty();

    public DoubleProperty cyclePeriodProperty();

    public ObservableList<ForceLawId> getAppliedForces();

    public ObservableList<ForceLawId> getAvailableForces();

    /**
     * Returns the underlying graph model.
     * @return
     */
    public Graph<FxVertex, FxEdge> getGraph();

    /**
     * Starts the layout animation
     */
    public void start();

    /**
     * Stops the layout animation. 
     * @TODO: What about vertex coordinate synchronisation?
     */
    public void stop();

    /**
     * Pauses the current layout animation. The optimizer keeps the current
     * status and can be resumed by the resume() method.
     */
    public void pause();

    /**
     * Resumes the animation if it is in PAUSED state.
     */
    public void resume();
    
    /**
     * Returns the current status
     *
     * @return
     */
    public ReadOnlyObjectProperty<Animation.Status> statusProperty();

    /**
     * Sets the coordinates of the vertices to random values
     */
    public void scramble();

    /**
     * The x coordinate of the upper left corner of the drawing region for
     * vertices.
     * 
     * @return
     */
    public DoubleProperty regionXProperty();

    /**
     * The y coordinate of the upper left corner of the drawing region for
     * vertices
     * @return
     */
    public DoubleProperty regionYProperty();

    /**
     * The width of the drawing region for vertices.
     * @return
     */
    public DoubleProperty regionWidthProperty();

    /**
     * The height of the drawing region for vertices.
     * @return
     */
    public DoubleProperty regionHeightProperty();

    /**
     * The current result of the optimizer.
     * @return
     */
    public ReadOnlyDoubleProperty optimizerResultProperty();

}