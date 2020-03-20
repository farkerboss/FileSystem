package com.qys.dao.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.qys.dao.FileDao;
import com.qys.domain.File;
import com.qys.util.JdbcUtil;

public class FileDaoImpl implements FileDao {
	private JdbcUtil jdbc=new JdbcUtil();
	@Override
	public void addFile(File file) {
		// TODO Auto-generated method stub
		String sql="insert into file (id, fsize, ftype, fname, createtime, filepath,envo) values (?,?,?,?,?,?,?)";
		List<Object> params=new ArrayList<Object>();
		params.add(file.getId());
		params.add(file.getFsize());
		params.add(file.getFtype());
		params.add(file.getFname());
		long time=file.getCreatetime().getTime();
		params.add(new Timestamp(time));
		params.add(file.getFilepath());
		params.add(file.getEnvo());
		jdbc.updatePreparedStatement(sql, params);
		jdbc.close();
	}
	@Override
	public File query(String id) {
		// TODO Auto-generated method stub
		String sql="select * from file where id=?";
		List<Object> params=new ArrayList<Object>();
		params.add(id);
		List<File> files= jdbc.queryPreparedStatement(sql, params, File.class);
		if(files!=null&&files.size()>0)
		{
			return files.get(0);
		}
		return null;
		
	}
	@Override
	public List<File> queryList() {
		// TODO Auto-generated method stub
		String sql="select * from file order by createtime desc offset 0 rows fetch next 10 rows only";
		List<File> files= jdbc.queryPreparedStatement(sql, null, File.class);
		return files;
	}
	

}
