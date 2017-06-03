package server;

import java.net.SocketAddress;
import java.util.LinkedHashMap;

import common.Common;
import database.Database;
import group.Group;
import user.User;

public class ServerManager {
	
	public NettyServer server;
	public Database database;
	public LinkedHashMap<SocketAddress, User> listUser;
	public LinkedHashMap<String, User> listName;
	public LinkedHashMap<String, Group> listGroup;
	
	public ServerManager(NettyServer server){
		this.server=server;
		this.database=Database.getInstance();
		listUser=new LinkedHashMap<SocketAddress,User>();
		listName=new LinkedHashMap<String,User>();
		listGroup=new LinkedHashMap<String,Group>();
	}

	public void nofifyToAll() {
		for (User user : listName.values()) {
			user.sendMessage(Common.newMess("cmd", "online", listName.keySet().toString()));
		}
	}
}
