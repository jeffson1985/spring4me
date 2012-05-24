/*
 * Copyright 2011-2012 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.osforce.spring4me.web.page.config.xml;

import java.util.List;

import org.osforce.spring4me.web.page.config.AbstractGroupConfig;
import org.osforce.spring4me.web.page.config.PageConfig;
import org.osforce.spring4me.web.widget.config.WidgetConfig;
import org.osforce.spring4me.web.widget.config.xml.XmlWidgetConfig;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

/**
 * 
 * @author gavin
 * @since 0.4.0
 * @create Feb 12, 2012 - 11:27:25 AM
 */
public class XmlGroupConfig extends AbstractGroupConfig {

    public XmlGroupConfig(Element groupEle, PageConfig pageConfig) {
        initialize(groupEle, pageConfig);
    }
    
    private void initialize(Element groupEle,  PageConfig pageConfig) {
        setPageConfig(pageConfig);
        //
        String id = groupEle.getAttribute("id");
        String layout = groupEle.getAttribute("layout");
        String disabled = groupEle.getAttribute("disabled");
        //
        setId(id);
        setLayout(layout);
        setDisabled("true".equals(disabled));
        //
        List<Element> widgetEles = DomUtils.getChildElementsByTagName(groupEle, "widget");
        for(Element widgetEle : widgetEles) {
            WidgetConfig widgetConfig = new XmlWidgetConfig(widgetEle, this);
            addWidgetConfig(widgetConfig);
        }
    }

}
