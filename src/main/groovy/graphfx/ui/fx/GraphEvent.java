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

package graphfx.ui.fx;

import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.event.EventType;

/**
 * Event that is fired, when a node is dragged and fixed to a certain
 * position.
 * 
 * @author martin
 *
 */
public class GraphEvent extends ActionEvent
{

    /**
     * 
     */
    private static final long serialVersionUID = 6361493107945474456L;
    
    /**
     * Node is fixed by the drag position
     */
    public static final EventType<GraphEvent>  GRAPH_NODE_FIXED = new EventType<>("GRAPH_NODE_FIXED");
    
    /**
     * Node is released and can float freely
     */
    public static final EventType<GraphEvent>  GRAPH_NODE_RELEASED = new EventType<>("GRAPH_NODE_RELEASED");

    public GraphEvent(EventType<? extends GraphEvent> eventType) {
        super();
        this.eventType = eventType;
    }

    
    public GraphEvent(Object source, EventTarget target, EventType<? extends GraphEvent> eventType) {
        super(source, target);
        this.eventType = eventType;
    }

    
    

}
