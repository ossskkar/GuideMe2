package com.nctu.guideme;

public class Path_d {
	private long id;
	private long path_h;
	private long steps;
	private float directionX;
	private float directionY;
	private float directionZ;
	
	public Path_d(){
	}
	
	public Path_d(long path_h, long steps, float directionX, float directionY, float directionZ){
		this.path_h=path_h;
		this.steps=steps;
		this.directionX=directionX;
		this.directionY=directionY;
		this.directionZ=directionZ;
	}
	
	public long getId(){
		return id;
	}

	public void setId(long id){
		this.id=id;
	}
	
	public long getPath_h(){
		return path_h;
	}
	
	public void setPath_h(long path_h){
		this.path_h=path_h;
	}
	
	public long getSteps(){
		return steps;
	}
	
	public void setSteps(long steps){
		this.steps=steps;
	}
	
	public float getDirectionX(){
		return directionX;
	}
	
	public void setDirectionX(float directionX){
		this.directionX=directionX;
	}

	public float getDirectionY(){
		return directionY;
	}
	
	public void setDirectionY(float directionY){
		this.directionY=directionY;
	}
	
	public float getDirectionZ(){
		return directionZ;
	}
	
	public void setDirectionZ(float directionZ){
		this.directionZ=directionZ;
	}
}
