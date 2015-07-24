/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidpn.client;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.provider.ProviderManager;

import android.R.bool;
import android.R.integer;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.util.Log;

/**
 * This class is to manage the XMPP connection between client and server.
 * 
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class XmppManager {



	private static final String LOGTAG = LogUtil.makeLogTag(XmppManager.class);

	private static final String XMPP_RESOURCE_NAME = "AndroidpnClient";

	private Context context;

	private NotificationService.TaskSubmitter taskSubmitter;

	private NotificationService.TaskTracker taskTracker;

	private SharedPreferences sharedPrefs;

	private String xmppHost;

	private int xmppPort;

	private XMPPConnection connection;

	private String username;

	private String password;

	private String nickname;

	private String receive;

	private String name;
	

	

	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getReceive() {
		return receive;
	}

	public void setReceive(String receive) {
		this.receive = receive;
	}

	private ConnectionListener connectionListener;

	private PacketListener notificationPacketListener;

	private Handler handler;

	private List<Runnable> taskList;

	private boolean running = false;

	private Future<?> futureTask;

	private Thread reconnection;

	

	public XmppManager(NotificationService notificationService) {
		// 初始化变量的
		context = notificationService;
		taskSubmitter = notificationService.getTaskSubmitter();
		taskTracker = notificationService.getTaskTracker();
		sharedPrefs = notificationService.getSharedPreferences();

		// 从share文件中读取的已存下的值
		xmppHost = sharedPrefs.getString(Constants.XMPP_HOST, "localhost");
		xmppPort = sharedPrefs.getInt(Constants.XMPP_PORT, 5222);
		username = sharedPrefs.getString(Constants.XMPP_USERNAME, "");
		password = sharedPrefs.getString(Constants.XMPP_PASSWORD, "");
		nickname = sharedPrefs.getString(Constants.XMPP_NICKNAME, "");
		
		receive = sharedPrefs.getString(Constants.XMPP_RECEIVE, "");
		name = sharedPrefs.getString(Constants.XMPP_NAME, "");
		connectionListener = new PersistentConnectionListener(this);
		notificationPacketListener = new NotificationPacketListener(this);

		handler = new Handler();
		taskList = new ArrayList<Runnable>();
		reconnection = new ReconnectionThread(this);
	}

	public Context getContext() {
		return context;
	}

	// 连接服务器
	public void connect() {
		Log.d(LOGTAG, "connect()...");
		submitLoginTask();
	}

	public void disconnect() {
		Log.d(LOGTAG, "disconnect()...");
		terminatePersistentConnection();
	}

	public void terminatePersistentConnection() {
		Log.d(LOGTAG, "terminatePersistentConnection()...");
		Runnable runnable = new Runnable() {

			final XmppManager xmppManager = XmppManager.this;

			public void run() {
				if (xmppManager.isConnected()) {
					Log.d(LOGTAG, "terminatePersistentConnection()... run()");
					xmppManager.getConnection().removePacketListener(
							xmppManager.getNotificationPacketListener());
					xmppManager.getConnection().disconnect();
				}
				xmppManager.runTask();
			}

		};
		addTask(runnable);
	}

	public XMPPConnection getConnection() {
		return connection;
	}

	public void setConnection(XMPPConnection connection) {
		this.connection = connection;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ConnectionListener getConnectionListener() {
		return connectionListener;
	}

	public PacketListener getNotificationPacketListener() {
		return notificationPacketListener;
	}

	// 启动断线重连的线程
	public void startReconnectionThread() {
		synchronized (reconnection) {
			if (reconnection == null && !reconnection.isAlive()) {
				reconnection = new ReconnectionThread(this);
				reconnection.setName("Xmpp Reconnection Thread");
				reconnection.start();
			}
		}
	}

	public Handler getHandler() {
		return handler;
	}

	public void reregisterAccount() {
		removeAccount();
		submitLoginTask();
		runTask();
	}

	public List<Runnable> getTaskList() {
		return taskList;
	}

	public Future<?> getFutureTask() {
		return futureTask;
	}

	public void runTask() {
		Log.d(LOGTAG, "runTask()...");
		synchronized (taskList) {
			running = false;
			futureTask = null;
			if (!taskList.isEmpty()) {
				Runnable runnable = (Runnable) taskList.get(0);
				taskList.remove(0);
				running = true;
				futureTask = taskSubmitter.submit(runnable);// 执行runnable
				if (futureTask == null) {
					taskTracker.decrease();
				}
			}
		}
		taskTracker.decrease();
		Log.d(LOGTAG, "runTask()...done");
	}

	private String newRandomUUID() {
		String uuidRaw = UUID.randomUUID().toString();
		return uuidRaw.replaceAll("-", "");
	}

	private boolean isConnected() {
		return connection != null && connection.isConnected();
	}

	public boolean isAuthenticated() {
		return connection != null && connection.isConnected()
				&& connection.isAuthenticated();
	}

	private boolean isRegistered() {
		return sharedPrefs.contains(Constants.XMPP_USERNAME)
				&& sharedPrefs.contains(Constants.XMPP_PASSWORD);
	}

	private void submitConnectTask() {
		Log.d(LOGTAG, "submitConnectTask()...");
		addTask(new ConnectTask());
	}

	private void submitRegisterTask() {
		Log.d(LOGTAG, "submitRegisterTask()...");
		submitConnectTask();
		addTask(new RegisterTask());
	}

	private void submitLoginTask() {
		Log.d(LOGTAG, "submitLoginTask()...");
		submitRegisterTask();
		addTask(new LoginTask());
	}

	private void addTask(Runnable runnable) {
		Log.d(LOGTAG, "addTask(runnable)...");
		taskTracker.increase();// 任务计数器，计数作用
		synchronized (taskList) {
			if (taskList.isEmpty() && !running) {
				running = true;
				futureTask = taskSubmitter.submit(runnable);
				if (futureTask == null) {
					taskTracker.decrease();
				}
			} else {
				taskList.add(runnable);
			}
		}
		Log.d(LOGTAG, "addTask(runnable)... done");
	}

	private void removeAccount() {
		Editor editor = sharedPrefs.edit();
		editor.remove(Constants.XMPP_USERNAME);
		editor.remove(Constants.XMPP_PASSWORD);
		editor.commit();
	}

	private void dropTask(int dropCount) {
		synchronized (taskList) {
			if (taskList.size() >= dropCount) {
				for (int i = 0; i < dropCount; i++) {
					taskList.remove(0);
					taskTracker.decrease();
				}
			}
		}

	}

	/**
	 * A runnable task to connect the server.
	 */
	private class ConnectTask implements Runnable {

		final XmppManager xmppManager;

		// 连接任务
		private ConnectTask() {
			this.xmppManager = XmppManager.this;
		}

		public void run() {
			Log.i(LOGTAG, "ConnectTask.run()...");

			if (!xmppManager.isConnected()) {
				// Create the configuration for this new connection
				ConnectionConfiguration connConfig = new ConnectionConfiguration(
						xmppHost, xmppPort);

				// 配置一些链接参数
				// connConfig.setSecurityMode(SecurityMode.disabled);
				connConfig.setSecurityMode(SecurityMode.required);
				connConfig.setSASLAuthenticationEnabled(false);
				connConfig.setCompressionEnabled(false);

				XMPPConnection connection = new XMPPConnection(connConfig);
				xmppManager.setConnection(connection);

				try {
					// 执行客户端与服务器的连接
					connection.connect();// 通过asmack处理连接任务
					Log.i(LOGTAG, "XMPP connected successfully");

					// packet provider
					ProviderManager.getInstance().addIQProvider("notification",
							"androidpn:iq:notification",
							new NotificationIQProvider());
					// 连接成功开始下一个任务
					xmppManager.runTask();
				} catch (XMPPException e) {
					Log.e(LOGTAG, "XMPP connection failed", e);
					xmppManager.dropTask(2);
					xmppManager.runTask();
					// 启动断线重连
					xmppManager.startReconnectionThread();
				}

			} else {
				Log.i(LOGTAG, "XMPP connected already");
				xmppManager.runTask();
			}
		}
	}

	/**
	 * A runnable task to register a new user onto the server. 注册用户
	 */
	private class RegisterTask implements Runnable {

		final XmppManager xmppManager;
		boolean isRegisterSucceed;
		boolean hasDropTask;

		private RegisterTask() {
			xmppManager = XmppManager.this;
		}

		public void run() {
			Log.i(LOGTAG, "RegisterTask.run()...");

			if (!xmppManager.isRegistered()) {
				isRegisterSucceed = false;
				hasDropTask = false;
				// 随机的ID和PassWord。。。
				final String newUsername = newRandomUUID();
				final String newPassword = newRandomUUID();
				final String newNickname = newRandomUUID();
				final String newReceive = "1";
				final String newName = "abcd";

				Registration registration = new Registration();
				// PacketFilter对服务器获取的数据进行过滤
				PacketFilter packetFilter = new AndFilter(new PacketIDFilter(
						registration.getPacketID()), new PacketTypeFilter(
						IQ.class));
				// PacketListener监听服务器返回数据的回调
				PacketListener packetListener = new PacketListener() {
					// 执行该方法表示服务器已经响应
					public void processPacket(Packet packet) {
						synchronized (xmppManager) {
							Log.d("RegisterTask.PacketListener",
									"processPacket().....");
							Log.d("RegisterTask.PacketListener", "packet="
									+ packet.toXML());

							if (packet instanceof IQ) {
								IQ response = (IQ) packet;
								if (response.getType() == IQ.Type.ERROR) {
									if (!response.getError().toString()
											.contains("409")) {
										Log.e(LOGTAG,
												"Unknown error while registering XMPP account! "
														+ response.getError()
																.getCondition());
									}
								} else if (response.getType() == IQ.Type.RESULT) {
									xmppManager.setUsername(newUsername);
									xmppManager.setPassword(newPassword);
									xmppManager.setNickname(newNickname);
									xmppManager.setReceive(newReceive);
									xmppManager.setName(newName);

									Log.d(LOGTAG, "username=" + newUsername);
									Log.d(LOGTAG, "password=" + newPassword);

									Editor editor = sharedPrefs.edit();
									editor.putString(Constants.XMPP_USERNAME,
											newUsername);// 更改SharedPreferences
															// 中的值
									editor.putString(Constants.XMPP_PASSWORD,
											newPassword);
									editor.putString(Constants.XMPP_NICKNAME,
											newNickname);
									editor.putString(Constants.XMPP_RECEIVE,
											newReceive);

									editor.putString(Constants.XMPP_NAME,
											newName);

									editor.commit();
									isRegisterSucceed = true;
									Log.i(LOGTAG,
											"Account registered successfully");
									if (!hasDropTask) {
										xmppManager.runTask();
									}

								}
							}
						}

					}
				};
				// 设置监听器
				connection.addPacketListener(packetListener, packetFilter);
				registration.setType(IQ.Type.SET);
				// registration.setTo(xmppHost);
				// Map<String, String> attributes = new HashMap<String,
				// String>();
				// attributes.put("username", rUsername);
				// attributes.put("password", rPassword);
				// registration.setAttributes(attributes);

				// 将数据发送出去
				registration.addAttribute("username", newUsername);
				registration.addAttribute("password", newPassword);
				registration.addAttribute("nickname", newNickname);
				registration.addAttribute("receive", newReceive);
				registration.addAttribute("name", newName);

				connection.sendPacket(registration);
				try {
					Thread.sleep(10 * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				synchronized (xmppManager) {
					if (!isRegisterSucceed) {// 判断是否服务器是否返回信息
						xmppManager.dropTask(1);
						xmppManager.runTask();
						xmppManager.startReconnectionThread();
						hasDropTask = true;
					}

				}

			} else {
				Log.i(LOGTAG, "Account registered already");

				xmppManager.runTask();
			}
		}
	}

	/**
	 * A runnable task to log into the server.
	 */
	private class LoginTask implements Runnable {

		final XmppManager xmppManager;

		private LoginTask() {
			this.xmppManager = XmppManager.this;
		}

		public void run() {
			Log.i(LOGTAG, "LoginTask.run()...");

			if (!xmppManager.isAuthenticated()) {
				Log.d(LOGTAG, "username=" + username);
				Log.d(LOGTAG, "password=" + password);

				try {
					xmppManager.getConnection().login(
							xmppManager.getUsername(),
							xmppManager.getPassword(), XMPP_RESOURCE_NAME);
					Log.d(LOGTAG, "Loggedn in successfully");

					// connection listener
					if (xmppManager.getConnectionListener() != null) {
						xmppManager.getConnection().addConnectionListener(
								xmppManager.getConnectionListener());
					}

					

						// packet filter
						PacketFilter packetFilter = new PacketTypeFilter(
								NotificationIQ.class);
						// packet listener
						PacketListener packetListener = xmppManager
								.getNotificationPacketListener();
						connection.addPacketListener(packetListener,
								packetFilter);
				
					connection.startHeartBeat();// 心跳数据包发送。即心跳
					synchronized (xmppManager) {
						xmppManager.notifyAll();
					}

				} catch (XMPPException e) {
					Log.e(LOGTAG, "LoginTask.run()... xmpp error");
					Log.e(LOGTAG, "Failed to login to xmpp server. Caused by: "
							+ e.getMessage());
					String INVALID_CREDENTIALS_ERROR_CODE = "401";
					String errorMessage = e.getMessage();
					if (errorMessage != null
							&& errorMessage
									.contains(INVALID_CREDENTIALS_ERROR_CODE)) {
						xmppManager.reregisterAccount();
						return;
					}
					xmppManager.startReconnectionThread();

				} catch (Exception e) {
					Log.e(LOGTAG, "LoginTask.run()... other error");
					Log.e(LOGTAG, "Failed to login to xmpp server. Caused by: "
							+ e.getMessage());
					xmppManager.startReconnectionThread();
				} finally {
					xmppManager.runTask();
				}

			} else {
				Log.i(LOGTAG, "Logged in already");
				xmppManager.runTask();
			}

		}
	}

}
