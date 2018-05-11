package com.framework.nio;

import org.junit.Test;

import java.nio.ByteBuffer;

public class MyBuffer {

    @Test
    public void allocateBuffer(){
        ByteBuffer buffer = ByteBuffer.allocate(10);


        buffer.put((byte) 1);
        buffer.put((byte) 2);
        buffer.put((byte) 3);
        buffer.put((byte) 4);

        //buffer.flip();
        buffer.position(0);

        System.out.println(buffer.get());
        System.out.println(buffer.get());
        System.out.println(buffer.get());
        System.out.println(buffer.get());

    }
}
