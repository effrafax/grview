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

import graphfx.ui.graph.FxGraph;
import graphfx.ui.graph.FxVertex;

import java.util.ArrayList;
import java.util.List;

public class FxTree extends FxGraph
{

    public FxTree(int children, int level, boolean randomChilds)
    {
        super();

        initialize(children, level, randomChilds);
    }

    private void initialize(int children, int level, boolean randomChilds)
    {
        List<FxVertex> currentNodes = new ArrayList<>();
        List<FxVertex> newNodes = new ArrayList<>();
        FxVertex rootNode = insertVertex().getExtension();
        currentNodes.add(rootNode);
        int childs;
        for (int l = 0; l < level; l++)
        {
            newNodes.clear();
            for (FxVertex fxVertex : currentNodes)
            {
                childs = randomChilds ? ((int) (Math.random() * children))
                    : children;
                for (int i = 0; i < childs; i++)
                {
                    FxVertex to = insertVertex().getExtension();
                    insertEdge(fxVertex, to);
                    newNodes.add(to);
                }
                
            }
            currentNodes.clear();
            currentNodes.addAll(newNodes);
        }

    }

}
