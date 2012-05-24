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

import org.osforce.spring4me.web.page.config.AbstractPageConfig;
import org.osforce.spring4me.web.page.config.GroupConfig;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

/**
 * 
 * @author gavin
 * @since 0.4.0
 * @create Feb 12, 2012 - 11:27:31 AM
 */
public class XmlPageConfig extends AbstractPageConfig {

    public XmlPageConfig(Element pageEle, String pagePath) {
        initialize(pageEle, pagePath);
    }
    
    private void initialize(Element pageEle, String pagePath) {
    	String path = pagePath;
    	String id = pageEle.getAttribute("id");
    	String parent = pageEle.getAttribute("parent");
    	String template = pageEle.getAttribute("template");
        //
    	setId(id);
        setPath(path);
        setParent(parent);
        setTemplate(template!=null?template:path);
        //
        List<Element> groupEles = DomUtils.getChildElementsByTagName(pageEle, "group");
        for(Element groupEle : groupEles) {
            GroupConfig groupConfig = new XmlGroupConfig(groupEle, this);
            addGroupConfig(groupConfig);
        }
    }

}
