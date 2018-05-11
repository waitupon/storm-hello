package com.framework.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class ChatServer {

    private Selector selector;

    public int PORT = 8888;



    public ChatServer() {
        init();
    }

    private void init() {
        try {
            selector = Selector.open();
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            ServerSocket socket = serverChannel.socket();
            socket.bind(new InetSocketAddress(PORT));

            //设置非阻塞
            serverChannel.configureBlocking(false);
            serverChannel.register(selector,SelectionKey.OP_ACCEPT);
            System.out.println("server is started");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void launch(){
        try {
            while(true){
                int count = selector.select();

                if(count>0){
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

                    while(iterator.hasNext()){
                        SelectionKey key = iterator.next();

                        if(key.isAcceptable()){
                            System.out.println(key.toString() + " : 接收");

                            iterator.remove();
                            ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();

                            SocketChannel socket = serverChannel.accept();
                            socket.configureBlocking(false);
                            socket.register(selector,SelectionKey.OP_READ);
                        }

                        if(key.isReadable()){
                            System.out.println(key.toString() + ": 读");
                            readMsg(key);
                        }

                        if(key.isWritable()){
                            System.out.println(key.toString() + ": 写");
                            writeMsg(key);
                        }

                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void readMsg(SelectionKey key) {
        SocketChannel channel = null;
        try {
            channel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count = channel.read(buffer);

            StringBuffer buf = new StringBuffer();
            if(count>0){
                buffer.flip();
                buf.append(new String(buffer.array(),0,count));
            }

            String msg = buf.toString();
            System.out.println(msg);

            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            while(iterator.hasNext()){
                SelectionKey sKey = iterator.next();
                //包含cancel操作  关闭
                sKey.interestOps(sKey.interestOps() | SelectionKey.OP_WRITE);
            }

            buffer.clear();
        } catch (IOException e) {
            key.channel();

            try {
                channel.socket().close();
                channel.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    private void writeMsg(SelectionKey key) {

    }




}
