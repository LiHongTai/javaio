package com.roger.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class NioClient {
	InetSocketAddress serverAddress = new InetSocketAddress("127.0.0.1", 8088);
	private SocketChannel client = null;
	// 创建两个缓冲池
	private ByteBuffer sendBuffer = ByteBuffer.allocate(1024);
	private ByteBuffer receiveBuffer = ByteBuffer.allocate(1024);
	private Selector selector = null;

	public NioClient() throws IOException {
		client = SocketChannel.open();
		client.configureBlocking(false);
		client.connect(serverAddress);

		selector = Selector.open();
		// 注册OP_CONNECT事件
		client.register(selector, SelectionKey.OP_CONNECT);
	}

	@SuppressWarnings("resource")
	private void session() throws IOException {
		if (client.isConnectionPending()) {
			client.finishConnect();
			client.register(selector, SelectionKey.OP_WRITE);
			System.out.println("NIO客户端已经连接到服务器，可以在控制台登记了！");
		}
		Scanner scan = new Scanner(System.in);
		while (scan.hasNextLine()) {
			String msg = scan.nextLine();
			if ("".equals(msg)) {
				continue;
			}
			if ("EXIT".equals(msg.toUpperCase())) {
				System.exit(0);
			}
			process(msg);
		}
	}

	private void process(String msg) throws IOException {
		boolean waitHelp = true;
		Iterator<SelectionKey> keyIterator = null;
		Set<SelectionKey> keys = null;
		while (waitHelp) {
			int readys = selector.select(2000);
			if (readys == 0) {
				continue;
			}
			keys = selector.keys();
			keyIterator = keys.iterator();
			while (keyIterator.hasNext()) {
				SelectionKey key = (SelectionKey) keyIterator.next();
				if (!key.isValid()) {
					keyIterator.remove();
					continue;
				}

				if (key.isWritable()) {
					// 首先判断客户端是否可写，就是代表客户端要对服务器发送消息
					sendBuffer.clear();
					sendBuffer.put(msg.getBytes());
					sendBuffer.flip();

					client.write(sendBuffer);
					client.register(selector, SelectionKey.OP_READ);
				} else if (key.isReadable()) {
					// 服务器发送消息，回来给客户端读
					receiveBuffer.clear();
					int len = client.read(receiveBuffer);
					if (len > 0) {
						receiveBuffer.flip();
						String returnMsg = new String(receiveBuffer.array(), 0, len);
						System.out.println("服务器端反馈的消息：" + returnMsg);
						client.register(selector, SelectionKey.OP_WRITE);
						waitHelp = false;
					}
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		NioClient nioClient = new NioClient();
		nioClient.session();
	}

}
