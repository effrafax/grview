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

import graphfx.ui.ForceLawId;
import griffon.core.resources.ResourceHandler;
import griffon.core.i18n.MessageSource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class ForceLawListCell extends ListCell<ForceLawId>
{
    
    private MessageSource msgSource;
    private ResourceHandler res;
    private Map<String, Group> imageNodes;

    public ForceLawListCell(MessageSource msgSource, ResourceHandler res)
    {
        this.msgSource = msgSource;
        this.res = res;
        imageNodes = new HashMap<>();
    }

    @Override
    protected void updateItem(ForceLawId item, boolean empty)
    {
        super.updateItem(item, empty);
        if (empty)
        {
            setText(null);
            setGraphic(null);
        } else
        {
            String msg = msgSource == null ? item.getName() : msgSource
                .getMessage(item.getName(), item.getName());
            setText(item == null ? "" : msg);
            Group g;
            if (!imageNodes.containsKey(item.getName()))
            {
                try
                {
                    g = loadImage(item.getName());
                } catch (IOException | NullPointerException e)
                {
                    g = new Group();
                }
                imageNodes.put(item.getName(), g);
            } else
            {
                g = imageNodes.get(item.getName());
            }
            setGraphic(g);
        }
    }

    private Group loadImage(String name) throws IOException
    {
        return FXMLLoader.load(res.getResourceAsURL(name + ".fxml"));
    }

    public MessageSource getMsgSource()
    {
        return msgSource;
    }

    public void setMsgSource(MessageSource msg)
    {
        this.msgSource = msg;
    }

    public static
        Callback<ListView<ForceLawId>, ListCell<ForceLawId>>
        forListView(
            final ContextMenu contextMenu,
            final MessageSource msgSource, 
            final ResourceHandler res)
    {
        return new Callback<ListView<ForceLawId>, ListCell<ForceLawId>>() {
            @Override
            public ListCell<ForceLawId> call(ListView<ForceLawId> listView)
            {
                ListCell<ForceLawId> cell = new ForceLawListCell(msgSource, res);
                cell.setContextMenu(contextMenu);
                return cell;
            }
        };
    }
}
