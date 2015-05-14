package com.datastore.model;

import java.util.List;

import com.mongodb.DBObject;

public class Json4DynamicGridPanel {
	private boolean action=true;
	private String message="";
	private List<DBObject> data;
	private List<ColumnModel> columnModels;
	private List<FieldName> fieldNames;
	public boolean isAction() {
		return action;
	}
	public void setAction(boolean action) {
		this.action = action;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<DBObject> getData() {
		return data;
	}
	public void setData(List<DBObject> data) {
		this.data = data;
	}
	public List<ColumnModel> getColumnModels() {
		return columnModels;
	}
	public void setColumnModels(List<ColumnModel> columnModels) {
		this.columnModels = columnModels;
	}
	public List<FieldName> getFieldNames() {
		return fieldNames;
	}
	public void setFieldNames(List<FieldName> fieldNames) {
		this.fieldNames = fieldNames;
	}
	
}
