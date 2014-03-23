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

public enum StandardForceLaw implements ForceLawId
{
    INVERSE_VERTEX_VERTEX_REPULSION_LAW,
    INVERSE_VERTEX_EDGE_REPULSION_LAW,
    INVERSE_SQUARE_VERTEX_VERTEX_REPULSION_LAW,
    INVERSE_SQUARE_VERTEX_EDGE_REPULSION_LAW,
    HYBRID_VERTEX_VERTEX_REPULSION_LAW,
    QUADRATIC_SPRING_LAW,
    LINEAR_SPRING_LAW;
    
    ForceLawId lawFor(String id) {
        return StandardForceLaw.valueOf(id);
    }
    
    @Override
    public String getName()
    {
        return this.name();
    }
    
    

}
