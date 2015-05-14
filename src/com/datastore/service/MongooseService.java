package com.datastore.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datastore.model.ColumnModel;
import com.datastore.model.FieldName;
import com.datastore.model.Json4DynamicGridPanel;
import com.datastore.utils.MongooseUtil;
import com.mongodb.DBObject;

@Service("mongooseService")
public class MongooseService {
	@Autowired
	MongooseUtil mongoose;
	private final int columnwidth=100;
	
	public List<String> getDatabases(){
		List<String> dbs = mongoose.getDatabaseNames();
		dbs.remove("local");
		dbs.remove("admin");
		return dbs;
	}
	
	public boolean existsCustomDB(String dbname){
		return mongoose.getDatabaseNames().contains(dbname);
	}
	
	public Set<String> getCollectionsInDB(String db){
		return mongoose.getCollectionNames(db);
	}

	public void createCollection(String db,String name,String json){
		mongoose.createCollection(db, name, json);
	}
	
	public void createCollection(String db,String name,List<String> jsons){
		mongoose.createCollection(db, name, jsons);
	}
	
	public Json4DynamicGridPanel findall(String db,String collection){
		Json4DynamicGridPanel obj=new Json4DynamicGridPanel();
		List<DBObject> data=mongoose.findall(db, collection);
		obj.setData(data);
		List<FieldName> fields=new ArrayList<FieldName>();
		List<ColumnModel> columns=new ArrayList<ColumnModel>();
		if(data.size()!=0){
			for(String k : data.get(0).keySet()){
				FieldName f=new FieldName();
				ColumnModel c=new ColumnModel();
				f.setName(k);
				c.setText(k);
				c.setDataIndex(k);
				c.setWidth(this.columnwidth);
				if("_id".equals(k))
					c.setHidden(true);
				fields.add(f);
				columns.add(c);
			}
		}
		obj.setColumnModels(columns);
		obj.setFieldNames(fields);
		return obj;
	}
}
