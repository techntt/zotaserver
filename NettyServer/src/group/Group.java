package group;

import java.util.ArrayList;
import java.util.List;

import common.Common;
import user.User;

public class Group {
	private String name;
	private List<User> list;
	public Group(String name){
		this.name=name;
		this.list=new ArrayList<User>();
	}
	
	public String getName(){
		return name;
	}
	
	public void addMemb(User user){
		this.list.add(user);
	}
	
	public void removeMemb(User user){
		this.list.remove(user);
	}
	
	public void sendMess(User sender,String mess) {
		for (User user : list) {
			if(user!=sender){
				user.sendMessage(Common.newMess("mess", name, mess));
			}
		}
	}

}
