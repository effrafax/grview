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

import graphfx.ui.ForceLawId
import graphfx.ui.fx.ForceLawListCell
import javafx.beans.binding.Bindings
import javafx.collections.ListChangeListener
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.ContextMenu
import javafx.scene.control.Menu
import javafx.scene.control.MenuItem
import javafx.scene.shape.Rectangle
import javafx.util.StringConverter

import griffon.core.artifact.GriffonController

@griffon.metadata.ArtifactProviderFor(GriffonController)
class GrviewController {
    def builder 
    GrviewModel model
    Menu addMenu
    ContextMenu forceCtxMenu

    def forceMenuMap = [:]

    void mvcGroupInit(Map args) {
        //    // this method is called after model and view are injected
        runInsideUIAsync() {
            log.info "CONTROLLER"
            log.info builder.toString()
            log.info builder.application?.toString()
            log.info builder.sc?.toString()
             builder.graph.children << model.gLayout.graph
             // items attribute in the view does only a addAll()
             // it does not check for observable list
             builder.appliedForces.setItems(model.gLayout.appliedForces)

             // Context menu on forces list view
             // Remove item
             MenuItem deleteItem = new MenuItem(application.messageSource.getMessage('item.remove',"Remove"))
             deleteItem.onAction = { ActionEvent ev ->
                 def item = builder.appliedForces.selectionModel.selectedItem
                 model.gLayout.appliedForces.remove(item)
             } as EventHandler<ActionEvent>
             // Add item
             addMenu = new Menu(application.messageSource.getMessage('item.add',"Add"))
             model.gLayout.availableForces.each {
                 log.debug "Available Force: ${it}"
                 addForceMenuItem(it)
             }
             model.gLayout.availableForces.addListener(changeForces)
             // Context menu
             forceCtxMenu = new ContextMenu(deleteItem, addMenu);
             builder.appliedForces.setCellFactory(ForceLawListCell.forListView(forceCtxMenu, application.messageSource, application.resourceHandler))



             // view.periodSlider.valueProperty().bindBidirectional(model.gLayout.minCyclePeriodProperty())
             // view.period.textProperty().bind(model.gLayout.cyclePeriodProperty().asString("%04.0f"))
             // def sltooltip = new Tooltip();
             // sltooltip.textProperty().bind(Bindings.format("%3.1f", model.gLayout.minCyclePeriodProperty()));
             // view.periodSlider.setTooltip(sltooltip)
             builder.edgeLengthSlider.valueProperty().bindBidirectional(model.gLayout.edgeLengthProperty())

             builder.optimizerResult.textProperty().bind(model.gLayout.optimizerResultProperty().asString("%012.8f"))

             builder.statusLabel.textProperty().bind(Bindings.format("%s",model.getgLayout().statusProperty()))

             def clip = new Rectangle(10,10,builder.canvas.width,builder.canvas.height)
             clip.widthProperty().bind(Bindings.max(0,builder.canvas.widthProperty().subtract(20)))
             clip.heightProperty().bind(Bindings.max(0,builder.canvas.heightProperty().subtract(20)))
             clip.arcWidth=20
             clip.arcHeight=20
             //view.canvasbg.widthProperty().bind(Bindings.max(0,view.canvas.widthProperty().subtract(20)))
             // view.canvasbg.heightProperty().bind(Bindings.max(0, view.canvas.heightProperty().subtract(20)))
             //view.graph1.clip=clip
             builder.canvas.clip=clip

             model.gLayout.regionXProperty().bind(builder.canvas.widthProperty().multiply(0.1))
             model.gLayout.regionYProperty().bind(builder.canvas.heightProperty().multiply(0.1))

             model.gLayout.regionWidthProperty().bind(builder.canvas.widthProperty().multiply(0.8))
             model.gLayout.regionHeightProperty().bind(builder.canvas.heightProperty().multiply(0.8))


             model.gLayout.initialize();

        }

    }

    private MenuItem createMenuItem(ForceLawId law) {
        return new MenuItem(application.messageSource.getMessage(law.name, law.name))
    }

    private void addForceMenuItem(ForceLawId law) {
        def newItem = createMenuItem(law)
        addMenu.items.add(newItem)
        forceMenuMap.put(newItem,law)
        log.debug "Adding item: ${newItem} // ${law}"
        newItem.onAction = selectForce
    }

    private void removeForceMenuItem(ForceLawId law) {
        def item = forceMenuMap.find{ it.value == law }?.key
        log.debug("Menu Force Remove: "+law+" // "+forceMenuMap[item])
        log.debug("Menu item: "+item)
        forceMenuMap.remove(item)
        addMenu.items.remove(item)
        log.debug("Menu items: "+addMenu.items)
    }


    void onWindowShown(window)  { 
        log.debug("OnReady: Canvas: "+builder.canvas.width+" "+builder.canvas.height)
        model.gLayout.scramble()
    }

    // void mvcGroupDestroy() {
    //    // this method is called when the group is destroyed
    // }

    // Applied, wenn menu item for available force was selected
    def selectForce = { ActionEvent ev ->
        log.debug "Event: "+ev
        def addedItem = forceMenuMap[ev.source]
        model.gLayout.appliedForces << addedItem
        forceCtxMenu.hide()
    } 

    // Applied, when the list of available forces changed.
    def changeForces = { ListChangeListener<ForceLawId>.Change change ->
        while(change.next()) {
            if (change.wasAdded()) {
                change.addedSubList.each { ForceLawId law ->
                    addForceMenuItem(law)
                }
            }
            if (change.wasRemoved()) {
                change.removed.each { ForceLawId law ->
                    removeForceMenuItem(law)
                }
            }
        }
    } as ListChangeListener<ForceLawId>


    def initializeGraph = { evt ->
        model.gLayout.updateModel()
    }

    def startLayout = { evt ->
        runInsideUIAsync {
            model.gLayout.start();
        }
    }

    def stopLayout = { evt ->
        runInsideUIAsync {
            model.gLayout.stop();
        }
    }

    def pauseLayout = { evt ->
        runInsideUIAsync {
            model.gLayout.pause();
        }
    }

    def resumeLayout = { evt -> 
        runInsideUIAsync {
            model.gLayout.resume();
        }
    }

    def scramble =  { evt ->
        runInsideUIAsync {
            model.gLayout.scramble()
        }

    }


    def reloadStylesheet = { evt ->
        def graphStyle = application.resourceHandler.getResourceAsURL("graph.css").toString();
        if (log.isDebugEnabled()) {
            builder.sc.stylesheets.eachWithIndex { st, i ->
                log.debug("Stylesheet [${i}]: "+st)
            }
        }
        int index = builder.sc.stylesheets.indexOf(graphStyle)
        builder.sc.stylesheets.add(index,graphStyle)
        builder.sc.stylesheets.remove(index+1)

    }

    def lawConverter = { from ->
        application.messageSource.getMessage(from.name,from.name[0..-5])
    }


}
