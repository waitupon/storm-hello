package com.framework.ack;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Map;

public class MyBolt implements IRichBolt {

    int num =0;
    OutputCollector outputCollector ;

    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.outputCollector = outputCollector;
    }

    public void execute(Tuple tuple) {
        String log = tuple.getStringByField("log");

        if(log != null) {
            num ++ ;
            System.err.println(Thread.currentThread().getName()+"   lines  :"+num +"   session_id:"+log.split("\t")[1]);
        }

        outputCollector.ack(tuple);
//        outputCollector.ack(tuple);
    }

    public void cleanup() {

    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields(""));
    }

    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
