package com.framework.helloworld;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/22 0022.
 */
public class SimpleSpout extends BaseRichSpout {
    Map map;
    TopologyContext topologyContext;
    SpoutOutputCollector spoutOutputCollector;

    int i=0;

    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.map = map;
        this.topologyContext = topologyContext;
        this.spoutOutputCollector = spoutOutputCollector;

    }

    public void nextTuple() {
        i++;
       // List num = new Value();
        List num = new Values(i);
        spoutOutputCollector.emit(num);

        System.err.println("==========Num:" + i);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("num"));
    }
}
