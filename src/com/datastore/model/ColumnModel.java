package com.datastore.model;

public class ColumnModel {
	//private String xtype="gridcolumn";
	private String dataIndex="";
	private String text="";
	private boolean hidden=false;
	private int width;
	
//	public String getXtype() {
//		return xtype;
//	}
//	public void setXtype(String xtype) {
//		this.xtype = xtype;
//	}
	
	public String getDataIndex() {
		return dataIndex;
	}
	public void setDataIndex(String dataIndex) {
		this.dataIndex = dataIndex;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isHidden() {
		return hidden;
	}
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
}
