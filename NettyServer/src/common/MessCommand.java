package common;

import java.util.ArrayList;
import java.util.Arrays;

import group.Group;
import server.ServerManager;
import user.User;

public class MessCommand {
	
	private ServerManager manager;
	private User sender;
	/***
	 * Struct of message 
	 * {%[type]<cmd>%}(%content%)
	 */
	public MessCommand(String mess,ServerManager manager, User sender){
		
		this.manager=manager;
		this.sender=sender;
		if(mess.contains("{%")&&mess.contains("%}")&&mess.contains("(%")&&mess.contains("%")){
			// right message
			String type=mess.substring(mess.indexOf("[")+1,mess.indexOf("]"));
			String cmd=mess.substring(mess.indexOf("<")+1,mess.indexOf(">"));
			String content=mess.substring(mess.indexOf("(%")+2,mess.indexOf("%)"));
			
			if(type.equals("cmd")){
				handlerCMD(cmd, content);
			}else if(type.equals("mess")){
				handlerMess(cmd, content);
			}else if(type.equals("game")){
				handlerGame(cmd, content);
			}
		}else{ // wrong message
			
		}
	}
	
	
	private void handlerCMD(String cmd,String content){
		/**  Login command **/
		if(cmd.equals("login")){
			String name=content.substring(0,content.indexOf(":"));
			String pass=content.substring(content.indexOf(":")+1);
			boolean login=manager.database.login(name, pass);
			if(login){
				sender.sendMessage(Common.newMess("cmd", "login", ""+true));
				sender.setName(name);
				manager.listName.put(name,sender);
				manager.nofifyToAll();
			}else{
				sender.sendMessage(Common.newMess("cmd", "login", ""+false));
			}
		}
		/**  Register command **/
		else if(cmd.equals("register")){
			String name=content.substring(0,content.indexOf(":"));
			String pass=content.substring(content.indexOf(":")+1);
			boolean register=manager.database.createUser(name, pass);
			if(register){
				sender.sendMessage(Common.newMess("cmd", "register", ""+true));
				//sender.setName(name);
				//manager.listName.put(name, sender);
				//manager.nofifyToAll();
			}else{
				sender.sendMessage(Common.newMess("cmd", "register", ""+false));
			}
		}
		
		/**  Request friend command **/
		else if(cmd.equals("friend")){
			
			
		}
		
		/**  Request friend command **/
		else if(cmd.equals("group")){			
			if(content.contains("[create]")){ //"[cmd]<name>name1,name2"
				String gr_name=content.substring(content.indexOf("<")+1,content.indexOf(">"));
				String str=content.substring(content.indexOf(">")+1);
				ArrayList<String> lstUser=new ArrayList<String>(Arrays.asList(str.split(",")));
				gr_name="_gr_"+sender.getName()+"_"+gr_name;
				Group group=new Group(gr_name);
				group.addMemb(sender);
				for (String user_name : lstUser) {
					User user=manager.listUser.get(user_name);
					if(user!=null){
						group.addMemb(user);
					}
				}
				manager.listGroup.put(gr_name, group);
				
			}else if(content.contains("[add]")){
				String gr_name=content.substring(content.indexOf("<")+1,content.indexOf(">"));
				String user_name=content.substring(content.indexOf(">")+1);
				Group group=manager.listGroup.get(gr_name);
				User user =manager.listUser.get(user_name);
				if(user!=null){
					group.addMemb(user);
				}				
			}else if(content.contains("[out]")){
				String gr_name=content.substring(content.indexOf("<")+1,content.indexOf(">"));
				Group group=manager.listGroup.get(gr_name);
				group.removeMemb(sender);
			}
			
		}
	}
	
	private void handlerMess(String recv,String mess){
		
		if(sender.getName()!=null){
			if(recv.equals("all")){
				for (User user : manager.listUser.values()) {
					if(user!=sender){
						user.sendMessage(Common.newMess("mess", sender.getName(), mess));
					}
				}
			}else if(recv.contains("_gr_")){
				Group group= manager.listGroup.get(recv);
				if(group!=null){
					
				}
			}			
			else{
				User receiver= manager.listName.get(recv);
				if(receiver!=null){
					receiver.sendMessage(Common.newMess("mess", sender.getName(), mess));
				}else{
					sender.sendMessage(Common.newMess("mess", "-server-", recv+" offline"));
				}
			}
			
		}
	}
	
	private void handlerGame(String player,String mess){
		if(mess.contains("[join]")){
			manager.listPlayer.put(player, sender);
		}else if(mess.contains("[out]")){
			manager.listPlayer.remove(player);
		}else if(mess.contains("[move]")){  // [move]<>()
			System.out.println(mess);
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					for (User user : manager.listPlayer.values()) {
						if(user!=sender){
							user.sendMessage(Common.newMess("game", player, mess));
						}
					}
					
				}
			}).start();
		}
	}
	
}
