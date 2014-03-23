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
package grview

import graphfx.layout.TimerBasedGraphLayout
import graphfx.model.Graph
import graphfx.ui.graph.FxEdge
import graphfx.ui.graph.FxVertex
import graphfx.ui.types.FxTree
import groovyx.javafx.beans.FXBindable
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue

import griffon.core.artifact.GriffonModel
import griffon.metadata.ArtifactProviderFor
import griffon.transform.Observable

@ArtifactProviderFor(GriffonModel)
class GrviewModel {

    def view

    TimerBasedGraphLayout gLayout;
    Graph<FxVertex,FxEdge> graph;

    @FXBindable String edgeLength = Double.toString(gLayout.edgeLength)

    Double scaleFactor = 100;
    Double boundX = 400
    Double boundY = 250

    @FXBindable Double vxPos;
    @FXBindable Double vyPos;
    @FXBindable Double vx;
    @FXBindable Double vy;

    @FXBindable String vxPosStr;
    @FXBindable String vyPosStr;

    @FXBindable String vxStr;
    @FXBindable String vyStr;


    void mvcGroupInit(Map args) {
        vxStr = "0.0"
        vyStr = "0.0"
        vxPos = 0.0
        vyPos = 0.0
        graph = new  FxTree(2,2,false);
        // graph.setMin([20, 20] as double[]);
        // graph.setMax([boundX, boundY] as double[])
        gLayout = new TimerBasedGraphLayout(graph);
        edgeLength = Double.toString(gLayout.edgeLength)

        edgeLengthProperty().addListener({ observable, oldVal, newVal ->
            log.debug("Changing edge length: ${newVal}")
            gLayout.setEdgeLength(Double.parseDouble(newVal))
            gLayout.updateModel()
        } as ChangeListener<String>)
        // log.debug "canvas: ${view.canvas} ${view.canvas?.class.name}"
    }





    @Override
    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        log.debug "Value changed: ${observable} ${observable.class} ${oldValue} ${newValue}"

    }
}
