package com.wis.mes.push;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.lang.StringUtils;


@ServerEndpoint("/websocketWithSession/{key}")
public class WebSocketWithSession{

   public static final Map<String,WebSocketWithSession> map = new HashMap<String,WebSocketWithSession>();
   
    //与客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    
    //连接成功调用的方法
    @OnOpen
    public void onOpen(@PathParam(value="key") String key,Session session) {
    	this.session = session;
        if(StringUtils.isNotEmpty(key) && !map.containsKey(key)){
        	map.put(key, this);
        }
    }

   //连接关闭调用的方法
    @OnClose
    public void onClose(@PathParam(value="key") String key) {
    	map.remove(key);
    }


   //发生错误时调用
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);

    }
    
    
    
    /**
     * 群发消息
     * message 消息内容
     * **/
    @OnMessage
    public void onMessage(String message) {
    	for (Entry<String, WebSocketWithSession> entry:map.entrySet()) {
				try {
					entry.getValue().sendMessage(message);
				} catch (IOException e) {
					e.printStackTrace();
			}
		}
    }
    /**
     * 指定用户发送消息
     * userId 用户ID
     * message 消息内容
     * **/
    public void sendMsg(String key,String message) {
    	for (Entry<String, WebSocketWithSession> entry:map.entrySet()) {
			if(entry.getKey().equals(key)){
				try {
					entry.getValue().sendMessage(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    	
    }
}
