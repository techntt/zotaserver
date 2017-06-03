package user;

import io.netty.channel.Channel;

public class User {
	
	private Channel channel;
	private String name;
	
	public User(Channel channel){
		this.channel=channel;
	}
	public Channel getChannel() {
		return this.channel;
	}
	public void sendMessage(String mess) {
		channel.writeAndFlush(mess+"\n");
	}
	public void setName(String name){
		this.name=name;
	}
	
	public String getName(){
		return this.name;
	}

}
