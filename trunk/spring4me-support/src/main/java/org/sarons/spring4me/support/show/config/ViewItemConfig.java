package org.sarons.spring4me.support.show.config;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午9:52:05
 */
public class ViewItemConfig {

	public enum Type {
		NUMBER, DATE, STRING
	}

	private String name;
	private Type type;
	private String format;
	//
	private String property;
	private String value;
	//
	private String catchValue;
	private String replaceValue;
	//
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	public void setType(String type) {
		if(type!=null) {
			this.type = Type.valueOf(type.toUpperCase());
		}
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getProperty() {
		return property;
	}
	
	public void setProperty(String property) {
		this.property = property;
	}
	
	public String getCatchValue() {
		return catchValue;
	}
	
	public void setCatchValue(String catchValue) {
		this.catchValue = catchValue;
	}
	
	public String getReplaceValue() {
		return replaceValue;
	}
	
	public void setReplaceValue(String replaceValue) {
		this.replaceValue = replaceValue;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
}
