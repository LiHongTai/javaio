package com.roger.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class NioServer {

	// 默认端口号
	private int port = 8080;
	// 创建两个缓冲池
	private ByteBuffer sendBuffer = ByteBuffer.allocate(1024);
	private ByteBuffer receiveBuffer = ByteBuffer.allocate(1024);
	// 创建服务通道
	private ServerSocketChannel server = null;
	// 多路复用器
	private Selector selector = null;
	// 创建消息缓存队列
	private Map<SelectionKey, String> sessionMsgs = new HashMap<>();

	private int CLEINT_NO = 631396529;

	public NioServer(int port) throws IOException {
		this.port = port;
		server = ServerSocketChannel.open();
		// 绑定端口地址
		server.socket().bind(new InetSocketAddress(this.port));
		server.configureBlocking(false);

		selector = Selector.open();
		// 注册OP_ACCEPT事件
		server.register(selector, SelectionKey.OP_ACCEPT);
		System.out.println("NIO消息器初始化完毕，可以随时接受客户端，监听端口为：" + this.port);
	}

	private void listener() throws IOException {
		while (true) {
			// 最大阻塞时间为2s，就接着继续往下执行代码
			int eventCount = selector.select(2000);
			if (eventCount == 0) {
				continue;
			}
			Set<SelectionKey> keys = selector.selectedKeys();
			final Iterator<SelectionKey> keyIterator = keys.iterator();
			while (keyIterator.hasNext()) {
				handleKey(keyIterator.next());
				keyIterator.remove();
			}
		}
	}

	private void handleKey(SelectionKey key) throws IOException {
		SocketChannel clientChannel = null;
		if (!key.isValid()) {
			return;
		}
		if (key.isAcceptable()) {
			// 服务端接受客户端的高速通道
			clientChannel = server.accept();
			++CLEINT_NO;
			clientChannel.configureBlocking(false);
			clientChannel.register(selector, SelectionKey.OP_READ);
		} else if (key.isReadable()) {
			// 将客户端通道里面的信息读取到接收缓冲池里面去
			// 然后放到消息队列中去
			receiveBuffer.clear();
			clientChannel = (SocketChannel) key.channel();
			int len = clientChannel.read(receiveBuffer);
			if (len > 0) {
				String msg = new String(receiveBuffer.array(), 0, len);
				sessionMsgs.put(key, msg);
				System.out
						.println("当前处理线程ID：" + Thread.currentThread().getId() + "读到客户端编号为：" + CLEINT_NO + "信息为：" + msg);
				// 响应客户端，读取完成后，就可以开始写
				clientChannel.register(selector, SelectionKey.OP_WRITE);
			}
		} else if (key.isWritable()) {
			if (!sessionMsgs.containsKey(key)) {
				return;
			}
			clientChannel = (SocketChannel) key.channel();
			sendBuffer.clear();
			String msg = sessionMsgs.get(key) + ",您好，服务器已经处理您的请求！";
			sendBuffer.put((msg).getBytes());
			// 切换读写
			sendBuffer.flip();
			clientChannel.write(sendBuffer);
			System.out.println("当前处理线程ID：" + Thread.currentThread().getId() + "写入客户端编号为：" + CLEINT_NO + "信息为：" + msg);
			clientChannel.register(selector, SelectionKey.OP_READ);
		}

	}

	public static void main(String[] args) throws IOException {
		NioServer nioServer = new NioServer(8088);
		nioServer.listener();
	}

}
