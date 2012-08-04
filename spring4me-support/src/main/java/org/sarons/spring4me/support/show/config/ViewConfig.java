package org.sarons.spring4me.support.show.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午9:52:01
 */
public class ViewConfig {

	private List<ViewItemConfig> itemConfigList = new ArrayList<ViewItemConfig>();
	
	public List<ViewItemConfig> getItemConfigList() {
		return Collections.unmodifiableList(itemConfigList);
	}
	
	public void addItemConfig(ViewItemConfig itemConfig) {
		this.itemConfigList.add(itemConfig);
	}
	
}
