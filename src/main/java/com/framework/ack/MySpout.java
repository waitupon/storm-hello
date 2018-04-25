package com.framework.ack;

import com.framework.helloworld.SimpleTopology;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.io.*;
import java.util.Map;

public class MySpout implements IRichSpout {

    int index = 0;

    FileInputStream fis;
    InputStreamReader isr;
    BufferedReader br;
    SpoutOutputCollector collector = null;
    String str = null;
    String path = null;

    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.collector = spoutOutputCollector;
        path = this.getClass().getResource("/").getPath();

        try {
            fis = new FileInputStream("track.log");
            isr = new InputStreamReader(fis, "UTF-8");
            br = new BufferedReader(isr);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void close() {
        try {
            br.close();
            isr.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void activate() {

    }

    public void deactivate() {

    }

    public void nextTuple() {
        try {
            while((str = this.br.readLine())!=null){
                this.collector.emit(new Values(str, str.split("\t")[1]));
            }
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void ack(Object msgId) {
        System.err.println("[" + Thread.currentThread().getName() + "]" + "  spout ack " + msgId.toString());
    }

    public void fail(Object msgId) {
        System.err.println("[" + Thread.currentThread().getName() + "]" + "  spout fail " + msgId.toString());
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
         declarer.declare(new Fields("log","session_id"));
    }

    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
