package com.datastore.utils;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import com.google.gson.Gson;

public class JsonpUtil {
	private static String callbackkey="datastore_callback";
	
	public static String getJsonp(HttpServletRequest request,Object obj) {
		Gson gson=new Gson();
		String cbfunction=request.getParameter(callbackkey)!=null?request.getParameter(callbackkey):"Ext.data.JsonP.callback1";
		String jsonp=!obj.getClass().isArray()?gson.toJson(obj):Arrays.toString(JSONArray.fromObject(obj).toArray());
		return cbfunction+"("+jsonp+")";
	}
}
