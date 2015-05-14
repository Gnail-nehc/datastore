package com.datastore.utils;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.QueryOperators;
import com.mongodb.util.JSON;

public class MongooseUtil {
	private static MongoClient mongo = null;
	
	public MongooseUtil(String host,int port) {
		if(null==mongo){
			try {
				mongo=new MongoClient(host,port);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}  
    
    public List<String> getDatabaseNames(){
    	return mongo.getDatabaseNames();
    }
    
    public Set<String> getCollectionNames(String dbname){
    	DB db = mongo.getDB(dbname);
    	Set<String> collections = db.getCollectionNames();
    	collections.remove("system.indexes");
    	return collections;
    }
    
    public DBCollection getCollection(String dbname,String collectionname){
    	DB db = mongo.getDB(dbname);
    	return db.getCollection(collectionname);
    }
    
    public boolean isCollectionExist(String dbname,String collectionname){
    	DB db = mongo.getDB(dbname);
    	return db.collectionExists(collectionname);
    }
    
    public DBCollection createCollection(String dbname,String collectionname){
    	if(!isCollectionExist(dbname,collectionname)){
    		DB db = mongo.getDB(dbname);
    		DBObject options = BasicDBObjectBuilder.start().add("capped", false).get();
        	return db.createCollection(collectionname, options);
    	}else{
    		return this.getCollection(dbname, collectionname);
    	}
    }
    
    public DBCollection createCollection(String dbname,String collectionname,String json){
    	DBCollection collection=this.createCollection(dbname, collectionname);
    	DBObject obj =  (BasicDBObject)JSON.parse(json);
    	collection.insert(obj);
    	return collection;
    }
    
    public DBCollection createCollection(String dbname,String collectionname,List<String> jsons){
    	List<DBObject> list = new ArrayList<DBObject>();
    	DBCollection collection=this.createCollection(dbname, collectionname);
    	for(String json : jsons){
    		DBObject obj =  (BasicDBObject)JSON.parse(json);
    		list.add(obj);
    	}
    	collection.insert(list);
    	return collection;
    }
    
    public boolean dropDatabase(String dbname){
    	boolean res=true;
    	try{
    		DB db = mongo.getDB(dbname);
        	db.dropDatabase();
    	}catch(Exception ex){
    		res=false;
    	}
    	return res;
    }
    
    public boolean dropCollection(String dbname,String collectionname){
    	boolean res=true;
    	try{
    		DB db = mongo.getDB(dbname);
        	db.getCollection(collectionname).drop();
    	}catch(Exception ex){
    		res=false;
    	}
    	return res;
    }
    
    public List<DBObject> findall(String dbname,String collectionname){
    	List<DBObject> list = new ArrayList<DBObject>();
    	DBCollection collection=this.getCollection(dbname, collectionname);
    	DBCursor cursor = collection.find();
    	while (cursor.hasNext()) {
    		list.add(cursor.next());
        }
    	return list;
    }
    
    /*condition: 
    name=="liangchen";gender==true
    name in ("liangchen")
    */
    public List<DBObject> findby(String dbname,String collectionname,String conditionstring){
    	List<DBObject> list = new ArrayList<DBObject>();
    	try{
    		DBCollection collection=this.getCollection(dbname, collectionname);
        	if(null!=conditionstring){
        		if(!conditionstring.endsWith(";")){
        			conditionstring=StringUtils.substringBeforeLast(conditionstring, ";");
        		}
        		BasicDBList values = new BasicDBList();
        		for(String str : conditionstring.split(";")){
        			if(str.contains("==")){
        				String k=StringUtils.substringBefore(str, "==").trim();
        				String v=StringUtils.substringAfter(str, "==").trim().replace("\"", "");
        				values.add(new BasicDBObject(k, v));
        			}else if(str.contains(">")){
        				String k=StringUtils.substringBefore(str, ">").trim();
        				String v=StringUtils.substringAfter(str, ">").trim().replace("\"", "");
        				values.add(new BasicDBObject(k, new BasicDBObject("$gt", v)));
        			}else if(str.contains("<")){
        				String k=StringUtils.substringBefore(str, "<").trim();
        				String v=StringUtils.substringAfter(str, "<").trim().replace("\"", "");
        				values.add(new BasicDBObject(k, new BasicDBObject("$lt", v)));
        			}else if(str.contains(">=")){
        				String k=StringUtils.substringBefore(str, ">=").trim();
        				String v=StringUtils.substringAfter(str, ">=").trim().replace("\"", "");
        				values.add(new BasicDBObject(k, new BasicDBObject("$gte", v)));
        			}else if(str.contains("<=")){
        				String k=StringUtils.substringBefore(str, "<=").trim();
        				String v=StringUtils.substringAfter(str, "<=").trim().replace("\"", "");
        				values.add(new BasicDBObject(k, new BasicDBObject("$lte", v)));
        			}else if(str.contains("!=")){
        				String k=StringUtils.substringBefore(str, "!=").trim();
        				String v=StringUtils.substringAfter(str, "!=").trim().replace("\"", "");
        				values.add(new BasicDBObject(k, new BasicDBObject("$ne", v)));
        			}else if(str.contains(" in ")){
        				String k=StringUtils.substringBefore(str, " in ").trim();
        				String[] arr=StringUtils.substringBetween(str, "(", ")").trim().split(",");
        				List<Object> objs=new ArrayList<Object>();
        				for(String s : arr){
        					s=s.replace("\"", "");
        					if(StringUtils.isNumeric(s)){
        						objs.add(Integer.parseInt(s));
        					}else{
        						objs.add(s.toString());
        					}
        				}
        				values.add(new BasicDBObject(k, new BasicDBObject(QueryOperators.IN,objs)));
        			}else if(str.contains(" nin ")){
        				String k=StringUtils.substringBefore(str, " not in ").trim();
        				String[] arr=StringUtils.substringBetween(str, "(", ")").trim().split(",");
        				List<Object> objs=new ArrayList<Object>();
        				for(String s : arr){
        					s=s.replace("\"", "");
        					if(StringUtils.isNumeric(s)){
        						objs.add(Integer.parseInt(s));
        					}else{
        						objs.add(s.toString());
        					}
        				}
        				values.add(new BasicDBObject(k, new BasicDBObject(QueryOperators.NIN,objs)));
        			}
        		}
        		DBObject condition = new BasicDBObject();
        		condition.put("$and", values); 
            	DBCursor cursor = collection.find(condition);
                while (cursor.hasNext()) {
                	list.add(cursor.next());
                }
        	}
    	}catch(Exception e){
    		
    	}
        return list;
    }
    
    public boolean update(String dbname,String collectionname,String condition,List<String> updatedKV){
    	boolean res=true;
    	try{
    		DBCollection collection=this.getCollection(dbname, collectionname);
        	List<DBObject> updating=this.findby(dbname, collectionname, condition);
        	if(updating.size()==0)
        		res=false;
        	else{
        		for(DBObject record : updating){
            		DBObject setValue=new BasicDBObject();  
                    for(String kv : updatedKV){
                    	String k=StringUtils.substringBefore(kv, "=");
                    	String v=StringUtils.substringAfterLast(kv, "=").replaceAll("\"", "");
                    	Object val=null;
                    	if(StringUtils.isNumeric(v))
                    		val=Integer.parseInt(v);
                    	else if(v.toLowerCase()=="false" || v.toLowerCase()=="true")
                    		val=(v.toLowerCase()=="true")==true;
                    	else
                    		val=v;
                    	setValue.put(k, val);
                    }
            		collection.update(record, new BasicDBObject("$set",setValue));//, true, true);
            	}
        	}
    	}catch(Exception e){
    		res=false;
    	}
    	return res;
    }
    
    public void save(String dbname,String collectionname,String json){
    	DBCollection collection=this.getCollection(dbname, collectionname);
    	collection.insert((BasicDBObject)JSON.parse(json));
    }
    
    public void save(String dbname,String collectionname,List<String> jsons){
    	List<DBObject> list = new ArrayList<DBObject>();
    	DBCollection collection=this.getCollection(dbname, collectionname);
    	for(String json : jsons){
    		DBObject obj =  (BasicDBObject)JSON.parse(json);
    		list.add(obj);
    	}
    	collection.insert(list);
    }
 
    public boolean delete(String dbname,String collectionname,String condition){
    	boolean res=true;
    	try{
    		DBCollection collection=this.getCollection(dbname, collectionname);
        	List<DBObject> deleted=this.findby(dbname, collectionname, condition);
        	if(deleted.size()==0)
        		res=false;
        	else{
	        	for(DBObject o : deleted){
	        		collection.remove(o);
	        	}
        	}
    	}catch(Exception e){
    		res=false;
    	}
    	return res;
    }
}
