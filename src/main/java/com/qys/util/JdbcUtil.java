package com.qys.util;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JdbcUtil {
	private Connection conn=null;
	private PreparedStatement pst=null;
	private ResultSet RS=null;
	private String uri="jdbc:derby://localhost:1527/COREJAVA";
	static{
		try {
			Class.forName("org.apache.derby.jdbc.ClientDriver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Connection getConn(){
		try {
			conn = DriverManager.getConnection(uri);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
	public void updatePreparedStatement(String sql,List<Object> params){
		getConn();
		try {
			pst=conn.prepareStatement(sql);
			if(params!=null)
			{
				for(int i=0;i<params.size();i++)
				{
					pst.setObject(i+1, params.get(i));
				}
			}
			pst.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void close()
	{
		try {
			if(RS!=null)
			{
				RS.close();
			}
			if(conn!=null)
			{
				conn.close();
			}
			if(pst!=null)
			{
				pst.close();
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public List queryPreparedStatement(String sql,List<Object> params,Class clazz){
		getConn();
		List<Object> result = new ArrayList<Object>();
		try {
			pst = conn.prepareStatement(sql);
			
			if(params!=null){
				for(int i=0;i<params.size();i++){
					pst.setObject(i+1, params.get(i));
				}
			}
		
			RS = pst.executeQuery();
			
			ResultSetMetaData rsmd = RS.getMetaData();
			//获取列的数量
			int count = rsmd.getColumnCount();
			//存储所有列的名字
			List<String> column = new ArrayList<String>();
			for(int i=0;i<count;i++){
				column.add(rsmd.getColumnName(i+1));
			}
			
			while(RS.next()){
				//创建对象
				Object obj = clazz.newInstance();
				
				for(int i=0;i<count;i++){
					String name = column.get(i).toLowerCase();
					//根据列明获取属性
					Field f = clazz.getDeclaredField(name);
					f.setAccessible(true);
					//获取属性的类型
					String type = f.getType().getName();
					if("int".equals(type) || "java.lang.Integer".equals(type)){
						int val = RS.getInt(name);
						f.set(obj, val);
					}else if("double".equals(type) || "java.lang.Double".equals(type)){
						double val = RS.getDouble(name);
						f.set(obj, val);
					}else if("float".equals(type) || "java.lang.Float".equals(type)){
						float val = RS.getFloat(name);
						f.set(obj, val);
					}else if("java.lang.String".equals(type)){
						String val = RS.getString(name);
						f.set(obj, val);
					}else if("java.util.Date".equals(type)){
						Date val = RS.getDate(name);
						f.set(obj, val);
					}else if("boolean".equals(type) || "java.lang.Boolean".equals(type)){
						boolean val = RS.getBoolean(name);
						f.set(obj, val);
					}
					
				}
				
				result.add(obj);
				
				
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return result;
	}
	
	
}
