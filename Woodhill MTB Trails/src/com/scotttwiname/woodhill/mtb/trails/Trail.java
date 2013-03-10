package com.scotttwiname.woodhill.mtb.trails;

public class Trail {
	private long id;
	private String name;
	private String file;
	private double dist;
	private int diff;
	private int jumpers;
	
	public long getId(){
		return id;
	}
	public void setID(long id){
		this.id = id;
	}
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	public String getFile(){
		return file;
	}
	public void setFile(String file){
		this.file = file;
	}
	public double getDist(){
		return dist;
	}
	public void setDist(double dist){
		this.dist = dist;
	}
	public String getDiff(){
		switch(diff){
		case 0: return "Beginner";
		case 1: return "Intermediate";
		case 2: return "Advanced";
		default: return "Null";
		}			
	}
	public void setDiff(int diff){
		this.diff = diff;
	}
	public int getJumpers(){
	 return jumpers;
	}
	public void setJumpers(int jumpers){
		this.jumpers = jumpers;
	}

}
