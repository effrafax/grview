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

package graphfx.ui.types;

import graphfx.model.Vertex;
import graphfx.ui.graph.FxGraph;
import graphfx.ui.graph.FxVertex;

import java.util.ArrayList;
import java.util.List;

public class FxPath extends FxGraph
{
    
    public FxPath(int n)
    {
        super();
        initialize(n);
    }

    private void initialize(int n)
    {
        List<Vertex<FxVertex>> vtc = new ArrayList<>();
        for (int i = 0; i < n; i++)
            vtc.add(i,insertVertex());
        for (int i = 1; i < n; i++)
            insertEdge(vtc.get(i-1), vtc.get(i));
    }

}
