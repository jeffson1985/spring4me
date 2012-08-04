package org.sarons.spring4me.support.show;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.sarons.spring4me.support.show.ViewConfigHelper;
import org.sarons.spring4me.support.show.config.ViewConfig;
import org.sarons.spring4me.support.show.config.ViewItemConfig;
import org.sarons.spring4me.support.show.config.ViewItemConfig.Type;


public class ViewConfigHelperTest {

	private ViewConfig viewConfig;
	
	@Before
	public void setup() {
		viewConfig = new ViewConfig();
		ViewItemConfig itemConfig = new ViewItemConfig();
		itemConfig.setProperty("name");
		itemConfig.setType(Type.STRING);
		itemConfig.setCatchValue("消费单");
		itemConfig.setReplaceValue("Gavin 的对账单");
		itemConfig.setFormat("我的 %s 哈哈！");
		viewConfig.addItemConfig(itemConfig);
		//
		itemConfig = new ViewItemConfig();
		itemConfig.setProperty("money");
		itemConfig.setType(Type.NUMBER);
		itemConfig.setFormat("#,###.00");
		viewConfig.addItemConfig(itemConfig);
		//
		itemConfig = new ViewItemConfig();
		itemConfig.setProperty("create");
		itemConfig.setType(Type.DATE);
		itemConfig.setFormat("yyyy-MM-dd");
		viewConfig.addItemConfig(itemConfig);
	}
	
	@Test
	public void testApply() {
		Bill bill = new Bill();
		bill.setName("消费单");
		bill.setMoney(10000);
		bill.setCreate(new Date());
		//
		ViewConfigHelper.apply(viewConfig, bill);
		//
		for(ViewItemConfig itemConfig : viewConfig.getItemConfigList()) {
			System.out.println(itemConfig.getValue());
		}
	}
	
}
