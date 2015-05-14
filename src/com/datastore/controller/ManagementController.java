package com.datastore.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.datastore.model.Collection;
import com.datastore.model.Json;
import com.datastore.model.Json4DynamicGridPanel;
import com.datastore.model.JsonList;
import com.datastore.service.MongooseService;


@Controller
public class ManagementController {
	private static final Logger logger = Logger.getLogger(ManagementController.class);
	
	@Autowired
	MongooseService mongooseService;
	
	@RequestMapping(value="/getDatabases")
	@ResponseBody
	public List<HashMap> getDatabases() {
		List<HashMap> list= new ArrayList<HashMap>();
		List<String> dbs = mongooseService.getDatabases();
		for(String db : dbs){
			HashMap map = new HashMap();
			map.put("name", db);
			list.add(map);
		}
		return list;
	}
	
	@RequestMapping(value="/existsCustomDB")
	@ResponseBody
	public boolean existsDB(@RequestParam String db){
		return mongooseService.existsCustomDB(db);
	}
	
	@RequestMapping(value="/getCollections")
	@ResponseBody
	public JsonList getCollections(@RequestParam String db) {
		JsonList list=new JsonList();
		Set<String> colls = mongooseService.getCollectionsInDB(db);
		List<Collection> rows=new ArrayList<Collection>();
		for(String name: colls){
			Collection c=new Collection();
			c.setName(name);
			rows.add(c);
		}
		list.setRows(rows);
		return list;
	}
	
	@RequestMapping(value="/createCollection")
	@ResponseBody
	public void createCollection(@RequestParam String db,@RequestParam String collection,@RequestParam String json) {
		String[] arr=json.split(";");
		if(arr.length>1){
			mongooseService.createCollection(db, collection, Arrays.asList(arr));
		}else{
			mongooseService.createCollection(db, collection, json);
		}
	}
	
	@RequestMapping(value="/findall4gridpanel")
	@ResponseBody
	public Json4DynamicGridPanel findall4gridpanel(@RequestParam String db,@RequestParam String collection) {
		return mongooseService.findall(db, collection);
	}
}
