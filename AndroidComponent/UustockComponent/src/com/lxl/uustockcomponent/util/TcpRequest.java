package com.lxl.uustockcomponent.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import de.greenrobot.event.EventBus;

/**
 * 异步tcp数据请求类，依赖eventbus。
 * 
 * @author liuxiaolong
 * 
 */
public class TcpRequest {

	private Socket socket;
	private InetSocketAddress inetAddress;
	private EventBus eventBus;

	public TcpRequest(String host, int port) {
		socket = new Socket();
		inetAddress = new InetSocketAddress(host, port);
		eventBus = EventBus.getDefault();
	}

	/**
	 * 开始请求数据。
	 * 
	 * @param message
	 */
	public void request(String message) {
		ChackResponseThread chackResponseThread = new ChackResponseThread(
				message);
		chackResponseThread.start();
	}

	private class ChackResponseThread extends Thread {
		private String message;

		public ChackResponseThread(String message) {
			this.message = message;
		}

		@Override
		public void run() {
			// TODO 自动生成的方法存根
			super.run();
			TcpRequestMessage tcpRequestMessage = new TcpRequestMessage();
			try {
				socket.setSoTimeout(10000);
				socket.connect(inetAddress);
				InputStream inputStream = socket.getInputStream();
				OutputStream outputStream = socket.getOutputStream();
				outputStream.write(message.getBytes());
				outputStream.flush();
				// outputStream.close();
				tcpRequestMessage.setTcpRequestMessage(read(inputStream));
				tcpRequestMessage.setCode(1);
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
				tcpRequestMessage.setCode(-1);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
				tcpRequestMessage.setCode(-1);
			} finally {
				eventBus.post(tcpRequestMessage);
			}
		}

		/**
		 * 轮训抓取返回数据。
		 * 
		 * @param inputStream
		 * @return
		 * @throws IOException
		 * @throws InterruptedException
		 */
		private byte[] read(InputStream inputStream) throws IOException,
				InterruptedException {
			boolean off = true;
			byte[] b = new byte[1];
			byte[] b1 = new byte[1024 * 3 - 1];
			byte[] b2 = new byte[1024 * 3];
			while (off) {
				Thread.sleep(500);
				if (inputStream.read(b) != -1) {
					inputStream.read(b1);
					System.arraycopy(b, 0, b2, 0, b.length);
					System.arraycopy(b1, 0, b2, b.length, b1.length);
					off = false;
				}
			}
			inputStream.close();
			return b2;
		}
	}

	public class TcpRequestMessage {
		private byte[] tcpRequestMessage;
		private int code;

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public byte[] getTcpRequestMessage() {
			return tcpRequestMessage;
		}

		public void setTcpRequestMessage(byte[] tcpRequestMessage) {
			this.tcpRequestMessage = tcpRequestMessage;
		}

	}
}
