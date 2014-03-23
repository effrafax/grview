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

package graphfx.ui.shapes;

import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;

public interface EdgeShape<N extends Node> extends GraphShape<N>
{
    
    public static final String DEFAULT_STYLE_CLASS="graphEdge";

    DoubleProperty startXProperty();

    DoubleProperty startYProperty();

    DoubleProperty endXProperty();

    DoubleProperty endYProperty();

    void setStartX(double xcoord);

    void setStartY(double ycoord);

    void setEndX(double xcoord);

    void setEndY(double ycoord);

    double getStartX();

    double getStartY();

    double getEndX();

    double getEndY();

}
