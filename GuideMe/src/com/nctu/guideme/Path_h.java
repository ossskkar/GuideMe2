package com.nctu.guideme;

public class Path_h{
	private long id;
	private String fileName;
	
	public long getId(){
		return id;
	}
	
	public void setId(long id){
		this.id=id;
	}
	
	public String getFileName(){
		return fileName;
	}
	
	public void setFileName(String fileName){
		this.fileName=fileName;
	}
	
	@Override
	public String toString(){
		return fileName;
	}
}
