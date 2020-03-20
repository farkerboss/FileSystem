package com.qys.service;

import java.util.List;

import com.qys.domain.File;

public interface FileService {
	public void addFile(File file);
	public File query(String id);
	public List<File> queryList();
}
