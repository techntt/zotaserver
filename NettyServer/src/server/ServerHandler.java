package server;
import common.Common;
import common.MessCommand;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import user.User;

public class ServerHandler extends ChannelInboundHandlerAdapter{
	
	private ServerManager manager;
	private User user;
	public ServerHandler(ServerManager manager) {
		this.manager=manager;
	}
	
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		super.handlerAdded(ctx);	
		user=new User(ctx.channel());
		manager.listUser.put(ctx.channel().remoteAddress(), user);
		System.out.println("[Server]- "+ ctx.channel().remoteAddress()+" has joined");
	}
	
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		super.handlerRemoved(ctx);
		System.out.println("[Server]- "+ ctx.channel().remoteAddress()+" has left");
		manager.listUser.remove(user);		
		if(user.getName()!=null){
			manager.listName.remove(user.getName());
				manager.nofifyToAll();
		}
	}
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		new MessCommand(msg.toString().trim(),manager,user);
	}
	
	
	// Send out heart beat for idle to long
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if(evt instanceof IdleStateEvent){
			IdleStateEvent event= (IdleStateEvent) evt;
			if(event.state()== IdleState.ALL_IDLE){ // Idle for no read and write
				//ctx.writeAndFlush("ping\n");
			}
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// Close the connection when an exception is raised
		cause.printStackTrace();
		ctx.close();
	}
}
