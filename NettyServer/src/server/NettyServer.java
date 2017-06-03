package server;

import java.io.IOException;
import java.util.LinkedHashMap;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import user.User;

public class NettyServer {
	
	private static NettyServer instance;
	private Channel channel;
	private NioEventLoopGroup boosGroup;
	private NioEventLoopGroup workerGroup;
	private ServerBootstrap bootstrap;
	
	public ServerManager manager;	
	
	
	public NettyServer(int port){	
		
		manager=new ServerManager(instance);
		try {
			initServer(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)  {		
		getInstance();
	}
	
	public static NettyServer getInstance () {
		if(instance==null)
			instance=new NettyServer(13192);
		return instance;
	}

	private void initServer(int port) throws IOException,InterruptedException{
		boosGroup=new NioEventLoopGroup();
		workerGroup=new NioEventLoopGroup();
		bootstrap =new ServerBootstrap();
		bootstrap.group(boosGroup,workerGroup);
		bootstrap.channel(NioServerSocketChannel.class);
		
		/**
		 * Define a separate thread pool to execute handlers with slow business logic
		 * e.g: database operation
		 */
		final EventExecutorGroup group=new DefaultEventExecutorGroup(1500);
		
		bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pipeline =ch.pipeline();
				pipeline.addLast("idleStateHandler",new IdleStateHandler(0, 0, 5));
				pipeline.addLast(new StringEncoder());
				pipeline.addLast(new StringDecoder());
				
				/** Run handler with slow business  logic in separate thread from I/O thread **/
				pipeline.addLast(group,"serverHandler",new ServerHandler(manager));
			}
		});
		
		bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
		channel=bootstrap.bind(port).sync().channel();
	}
	
	
	public void destroy() {
        if (channel != null) {
            channel.close();
        }
        workerGroup.shutdownGracefully();
    }
}
