package com.qys.util;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

public class Conversion {
	public static void req_obj(Object obj, HttpServletRequest request)
	{
		try {
			Class clazz=obj.getClass();
			Field[] fields=clazz.getDeclaredFields();
			for(Field f:fields)
			{
				f.setAccessible(true);
				String name=f.getName();
				Class cls=f.getType();
				System.out.println(cls);
				if(!cls.isArray())
				{
					String type=cls.getName();
					String value=request.getParameter(name);
					
					if(type.equals("java.lang.String"))
					{
						f.set(obj,value);
					}
					else if(type.equals("int")||type.equals("java.lang.Integer"))
					{
						f.set(obj,Integer.parseInt(value) );
					}
					else if(type.equals("double")||type.equals("java.lang.Double"))
					{
						f.set(obj, Double.parseDouble(value));
					}
					else if(type.equals("java.util.Date"))
					{
						SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
						f.set(obj, sdf.parse(value));
					}
				}
				else{
					String [] arr=request.getParameterValues(name);
					String type=cls.getName();
					
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
