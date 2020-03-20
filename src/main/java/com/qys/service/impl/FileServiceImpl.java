package com.qys.service.impl;

import java.util.List;

import com.qys.dao.FileDao;
import com.qys.dao.impl.FileDaoImpl;
import com.qys.domain.File;
import com.qys.service.FileService;

public class FileServiceImpl implements FileService {
	FileDao fileDao=new FileDaoImpl();
	@Override
	public void addFile(File file) {
		// TODO Auto-generated method stub
		fileDao.addFile(file);
	}
	@Override
	public File query(String id) {
		// TODO Auto-generated method stub
		return fileDao.query(id);
	}
	@Override
	public List<File> queryList() {
		// TODO Auto-generated method stub
		return fileDao.queryList();
	}

}
