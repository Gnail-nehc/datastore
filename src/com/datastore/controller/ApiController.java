package com.datastore.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.datastore.model.DataPlace;
import com.datastore.model.Ret;
import com.datastore.utils.HttpServletRequestUtil;
import com.datastore.utils.JsonpUtil;
import com.datastore.utils.MongooseUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Controller
public class ApiController {
private static final Logger logger = Logger.getLogger(ApiController.class);
	
	@Autowired
	MongooseUtil mongoose;
	
	@RequestMapping(value="/*/*/findall" )
	@ResponseBody
	public void findall(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DataPlace dp=getDataPlaceFromUrl(request);
		String db=dp.getDb();
		String collection=dp.getTable();
		List<DBObject> obj = mongoose.findall(db, collection);
		String jsonp=JsonpUtil.getJsonp(request,obj);
		this.returnJSONP(response, jsonp);
	}
	
	/*param: 
    1.
    	condition: name=="liangchen";gender==true
    			OR name=="liangchen"
    */
	@RequestMapping(value="/*/*/find" )
	@ResponseBody
	public void find(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<DBObject> objs;
		String function=request.getParameter("condition")!=null?request.getParameter("condition"):
			HttpServletRequestUtil.getValueFromRequestInput(HttpServletRequestUtil.getHttpServletRequestBody(request),"condition");
		if(null==function || function.isEmpty()){
			objs=new ArrayList<DBObject>();
		}else{
			DataPlace dp=getDataPlaceFromUrl(request);
			String db=dp.getDb();
			String collection=dp.getTable();
			objs = mongoose.findby(db, collection, function);
			if(objs.size()==0){
				DBObject obj=new BasicDBObject();
				obj.put("code", 1);
				obj.put("msg", "not found record");
				objs.add(obj);
			}
		}
		String jsonp=JsonpUtil.getJsonp(request,objs);
		this.returnJSONP(response, jsonp);
	}
	
	/*param: 
    1.
    	data: {"name":"gnail","gender":false}
    	   OR {"name":"gnail","gender":false};{"name":"nehc","gender":true}
    */
	@RequestMapping(value="/*/*/save" )
	@ResponseBody
	public void save(HttpServletRequest request, HttpServletResponse response) {
		Ret ret=new Ret();
		try{
			String data=request.getParameter("data")!=null?request.getParameter("data"):
				HttpServletRequestUtil.getValueFromRequestInput(HttpServletRequestUtil.getHttpServletRequestBody(request),"data");
			if(null==data || data.isEmpty()){
				ret.setCode(1);
				ret.setMsg("not found param 'data'");
			}else{
				DataPlace dp=getDataPlaceFromUrl(request);
				String db=dp.getDb();
				String collection=dp.getTable();
				if(data.endsWith(";")){
					data=StringUtils.substringBeforeLast(data, ";");
				}
				mongoose.save(db, collection, Arrays.asList(data.split(";")));
				ret.setMsg("success");
			}
		}catch(Exception e){
			ret.setCode(1);
			ret.setMsg(e.getMessage());
		}
		String jsonp=JsonpUtil.getJsonp(request,ret);
		this.returnJSONP(response, jsonp);
	}
	
	/*param: 
    1.
    	condition: name=="liangchen";gender==true
    			OR name="liangchen"
    2.
    	set: key1=value1
    	  OR key1=value1;key2=value2
    */
	@RequestMapping(value="/*/*/update" )
	@ResponseBody
	public void update(HttpServletRequest request, HttpServletResponse response) {
		Ret ret=new Ret();
		try{
			String function=request.getParameter("condition")!=null?request.getParameter("condition"):
				HttpServletRequestUtil.getValueFromRequestInput(HttpServletRequestUtil.getHttpServletRequestBody(request),"condition");
			String set=request.getParameter("set")!=null?request.getParameter("set"):
				HttpServletRequestUtil.getValueFromRequestInput(HttpServletRequestUtil.getHttpServletRequestBody(request),"data");
			if(null==function || function.isEmpty()){
				ret.setCode(1);
				ret.setMsg("not found param 'condition'");
			}else if(null==set || set.isEmpty()){
				ret.setCode(1);
				ret.setMsg("not found param 'set'");
			}else{
				DataPlace dp=getDataPlaceFromUrl(request);
				String db=dp.getDb();
				String collection=dp.getTable();
				if(set.endsWith(";")){
					set=StringUtils.substringBeforeLast(set, ";");
				}
				boolean res=mongoose.update(db, collection, function, Arrays.asList(set.split(";")));
				ret.setCode(res?0:1);
				ret.setMsg(res?"success":"failure");
			}
		}catch(Exception e){
			ret.setCode(1);
			ret.setMsg(e.getMessage());
		}
		String jsonp=JsonpUtil.getJsonp(request,ret);
		this.returnJSONP(response, jsonp);
	}
	
	/*param: 
    1.
    	condition: name=="liangchen";gender==true
    			OR name="liangchen"
    */
	@RequestMapping(value="/*/*/delete" )
	@ResponseBody
	public void delete(HttpServletRequest request, HttpServletResponse response) {
		Ret ret=new Ret();
		try{
			String function=request.getParameter("condition")!=null?request.getParameter("condition"):
				HttpServletRequestUtil.getValueFromRequestInput(HttpServletRequestUtil.getHttpServletRequestBody(request),"condition");
			if(null==function || function.isEmpty()){
				ret.setCode(1);
				ret.setMsg("not found param 'condition'");
			}else{
				DataPlace dp=getDataPlaceFromUrl(request);
				String db=dp.getDb();
				String collection=dp.getTable();
				boolean res=mongoose.delete(db, collection, function);
				ret.setCode(res?0:1);
				ret.setMsg(res?"success":"failure");
			}
		}catch(Exception e){
			ret.setCode(1);
			ret.setMsg(e.getMessage());
		}
		String jsonp=JsonpUtil.getJsonp(request,ret);
		this.returnJSONP(response, jsonp);
	}
	
	@RequestMapping(value="/*/*/drop" )
	@ResponseBody
	public void dropCollection(HttpServletRequest request, HttpServletResponse response) {
		Ret ret=new Ret();
		try{
			DataPlace dp=getDataPlaceFromUrl(request);
			String db=dp.getDb();
			String collection=dp.getTable();
			boolean res=mongoose.dropCollection(db, collection);
			ret.setCode(res?0:1);
			ret.setMsg(res?"success":"failure");
		}catch(Exception e){
			ret.setCode(1);
			ret.setMsg(e.getMessage());
		}
		String jsonp=JsonpUtil.getJsonp(request,ret);
		this.returnJSONP(response, jsonp);
	}
	
	private DataPlace getDataPlaceFromUrl(HttpServletRequest request){
		String pathinfo=request.getPathInfo();
		String db = StringUtils.substringBetween(pathinfo, "/");
		String table = StringUtils.substringBetween(pathinfo, "/"+db+"/", "/");
		DataPlace dp=new DataPlace();
		dp.setDb(db);
		dp.setTable(table);
		return dp;
	}
	
	private void returnJSONP(HttpServletResponse response,String jsonp){
		try {
			response.setContentType("application/x-javascript");
			response.getWriter().print(jsonp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
