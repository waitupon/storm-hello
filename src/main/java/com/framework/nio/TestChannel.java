package com.framework.nio;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class TestChannel {



    @Test
    public void copy() throws Exception {
        RandomAccessFile srcRaf = new RandomAccessFile("/Users/wenhao/tmp/123.jpeg","r");
        FileChannel srcChannel = srcRaf.getChannel();
        
        
        RandomAccessFile destRaf = new RandomAccessFile("/Users/wenhao/tmp/456.jpeg","rw");
        FileChannel destChannel = destRaf.getChannel();

        ByteBuffer buffer =  ByteBuffer.allocate(1024);

        while(srcChannel.read(buffer)!= -1){
            buffer.flip();
            destChannel.write(buffer);
            buffer.clear();
        }

        srcChannel.close();
        srcRaf.close();

        destChannel.close();
        destRaf.close();
    }


    @Test
    public void testMapping() throws Exception {
        RandomAccessFile srcRaf = new RandomAccessFile("/Users/wenhao/tmp/123.jpeg","rw");
        FileChannel srcChannel = srcRaf.getChannel();


        MappedByteBuffer buffer = srcChannel.map(FileChannel.MapMode.READ_WRITE, 0, srcRaf.length());


        RandomAccessFile destRaf = new RandomAccessFile("/Users/wenhao/tmp/456.jpeg","rw");
        FileChannel destChannel = destRaf.getChannel();

        destChannel.write(buffer);

        srcChannel.close();
        srcRaf.close();

        destChannel.close();
        destRaf.close();
    }
}
