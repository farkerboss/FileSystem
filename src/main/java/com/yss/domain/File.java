package com.yss.domain;

import java.util.Date;

public class File {
	private String id;
	private String fsize;
	private String ftype;
	private String fname;
	private Date createtime;
	private String time;
	private String filepath;
	private String envo;

	public File() {
		super();
		// TODO Auto-generated constructor stub
	}

	public File(String id, String fsize, String ftype, String fname, Date createtime, String filepath, String envo) {
		super();
		this.id = id;
		this.fsize = fsize;
		this.ftype = ftype;
		this.fname = fname;
		this.createtime = createtime;
		this.filepath = filepath;
		this.envo = envo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFsize() {
		return fsize;
	}

	public void setFsize(String fsize) {
		this.fsize = fsize;
	}

	public String getFtype() {
		return ftype;
	}

	public void setFtype(String ftype) {
		this.ftype = ftype;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getEnvo() {
		return envo;
	}

	public void setEnvo(String envo) {
		this.envo = envo;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
}
