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

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle
import javafx.util.StringConverter;
import jfxtras.labs.scene.control.BigDecimalField;

import griffon.core.artifact.GriffonView
import griffon.metadata.ArtifactProviderFor

@ArtifactProviderFor(GriffonView)
class GrviewView {

    def builder
    def model                                                                

    void initUI() {
      log.info "Builder: ${builder.toString()}"
    builder.application(title: 'Grview', sizeToScene: true, centerOnScreen: true) {
    scene(id:"sc",width: 800, height: 500, fill: WHITE, stylesheets:application.resourceHandler.getResourceAsURL('graph.css').toString()) {
        borderPane(id:"root") {
            top {
                vbox(styleClass:"controlBox") {
                    hbox(spacing: 10) {
                        vbox() {
                            text("Edge Length")
                            slider(id:"edgeLengthSlider",min:0,max:50,showTickMarks:true,
                                    showTickLabels:true,majorTickUnit:20)
                        }
                        vbox() {
                            text("Min. Optimizer Period (ms)")
                            slider(id:"periodSlider",min:0,max:200,
                                    showTickMarks:true, showTickLabels:true, majorTickUnit:100, minorTickCount:4)
                        }
                        vbox() {
                            label(text:application.messageSource.getMessage("periodLabel","Period (ms)"))
                            label(id:'period')

                        }
                        vbox() {
                            label("Optimizer Result")
                            label(id:"optimizerResult")
                        }
                        vbox() {
                            label(text:application.messageSource.getMessage("statusLabel","Status"))
                            label(id:'statusLabel')

                        }
                    }
                    hbox(spacing:10) {
                        button(application.messageSource.getMessage("start","Start"),onAction:controller.startLayout)
                        button(application.messageSource.getMessage("stop","Stop"),onAction:controller.stopLayout)
                        button(application.messageSource.getMessage("pause","Pause"),onAction:controller.pauseLayout)
                        button(application.messageSource.getMessage("resume","Resume"),onAction:controller.resumeLayout)
                        button(application.messageSource.getMessage("scramble","Scramble"),onAction:controller.scramble)
                        button(application.messageSource.getMessage("reloadCss","Reload Css"),onAction:controller.reloadStylesheet)
                    }
                }
            }
            right {
                listView(id:"appliedForces",prefHeight:80)
            }
            stackPane(id:"canvas",styleClass:"graphCanvas") {
                // rectangle(id:"canvasbg",styleClass:"graphCanvasBg")
                region(id:"graph")
            }
        }
    }
}
    log.info "SC: ${builder.sc.toString()}"
    log.info "GRAPH: ${builder.graph.toString()}"
}
}
