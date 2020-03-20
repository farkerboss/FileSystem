package com.qys.dao;

import java.util.List;

import com.qys.domain.File;

public interface FileDao {
   public void addFile(File file);
   public File query(String id);
   public List<File> queryList();
}
