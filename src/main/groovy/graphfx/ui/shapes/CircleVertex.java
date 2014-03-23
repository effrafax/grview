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

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class CircleVertex extends Circle implements VertexShape<Circle>
{

    public static String DEFAULT_STYLE_CLASS="graphVertexCircle";
    
    public CircleVertex()
    {
       super();
       init();
    }

    public CircleVertex(
        double centerX, double centerY, double radius, Paint fill)
    {
        super(centerX, centerY, radius, fill);
        init();
    }

    public CircleVertex(double centerX, double centerY, double radius)
    {
        super(centerX, centerY, radius);
        init();
    }

    public CircleVertex(double radius, Paint fill)
    {
        super(radius, fill);
        init();
    }

    public CircleVertex(double radius)
    {
        super(radius);
        init();
    }
    
    private void init() {
        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }

    @Override
    public Circle getNode()
    {
        return this;
    }

}
