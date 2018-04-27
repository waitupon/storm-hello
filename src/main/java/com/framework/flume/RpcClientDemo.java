package com.framework.flume;

import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.api.RpcClient;
import org.apache.flume.api.RpcClientFactory;
import org.apache.flume.event.EventBuilder;

import java.nio.charset.Charset;

public  class RpcClientDemo {

        public static void main(String[] args) {
            MyRpcClientFacade client = new MyRpcClientFacade();
            client.init("127.0.0.1",41414);

            for(int i=0;i<10;i++){
                String sampleData = " Hello Flume!ERROR" + i;
                client.sendDataToFlume(sampleData);
                System.out.println("发送数据：" + sampleData);
            }

        }

}

class MyRpcClientFacade{
    private RpcClient client;
    private String hostname;
    private int port;

    public MyRpcClientFacade(){}

    public void init(String hostname,int port){
        this.hostname = hostname;
        this.port = port;
        this.client = RpcClientFactory.getDefaultInstance(hostname,port);
    }


    public void sendDataToFlume(String data){
        Event event = EventBuilder.withBody(data, Charset.forName("UTF-8"));

        try {
            client.append(event);
        } catch (EventDeliveryException e) {
            client.close();
            client = null;
            this.client = RpcClientFactory.getDefaultInstance(hostname,port);
        }
    }

    public void cleanUp(){
        client.close();
    }

}