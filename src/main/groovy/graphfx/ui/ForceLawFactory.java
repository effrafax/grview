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

package graphfx.ui;


import jiggle.graph.force.ForceLaw;
import jiggle.graph.force.repulsion.HybridVertexVertexRepulsionLaw;
import jiggle.graph.force.repulsion.InverseSquareVertexEdgeRepulsionLaw;
import jiggle.graph.force.repulsion.InverseSquareVertexVertexRepulsionLaw;
import jiggle.graph.force.repulsion.InverseVertexEdgeRepulsionLaw;
import jiggle.graph.force.repulsion.InverseVertexVertexRepulsionLaw;
import jiggle.graph.force.spring.LinearSpringLaw;
import jiggle.graph.force.spring.QuadraticSpringLaw;
import graphfx.model.Edge;
import graphfx.model.Graph;
import graphfx.model.Vertex;


public class ForceLawFactory<V extends Vertex<V>>
{

    public static <V extends Vertex<V>> ForceLaw createForceLaw(Graph<? extends V, ? extends Edge<? extends V>> g, double k, StandardForceLaw force)
    {
        ForceLaw law = null;
        switch (force)
        {
        case INVERSE_VERTEX_VERTEX_REPULSION_LAW:
            law = new InverseVertexVertexRepulsionLaw(g, k);
            break;

        case INVERSE_VERTEX_EDGE_REPULSION_LAW:
            law = new InverseVertexEdgeRepulsionLaw(g, k);
            break;

        case INVERSE_SQUARE_VERTEX_VERTEX_REPULSION_LAW:
            law = new InverseSquareVertexVertexRepulsionLaw(g, k);
            break;

        case INVERSE_SQUARE_VERTEX_EDGE_REPULSION_LAW:
            law = new InverseSquareVertexEdgeRepulsionLaw(g, k);
            break;

        case HYBRID_VERTEX_VERTEX_REPULSION_LAW:
            law = new HybridVertexVertexRepulsionLaw(g, k);
            break;

        case LINEAR_SPRING_LAW:
            law = new LinearSpringLaw(g, k);
            break;

        case QUADRATIC_SPRING_LAW:
            law = new QuadraticSpringLaw(g, k);
            break;
        }
        return law;
    }

}
