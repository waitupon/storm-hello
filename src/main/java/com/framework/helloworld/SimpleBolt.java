package com.framework.helloworld;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.util.Map;

/**
 * Created by Administrator on 2018/4/22 0022.
 */
public class SimpleBolt extends BaseRichBolt {
    Map map;
    TopologyContext topologyContext;
    SpoutOutputCollector spoutOutputCollector;

    int sum = 0;

    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.map = map;
        this.topologyContext = topologyContext;
        this.spoutOutputCollector = spoutOutputCollector;
    }

    public void execute(Tuple tuple) {
        Integer num = tuple.getIntegerByField("num");
        sum += num;
        System.err.println("=========Sum:" + sum);

    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
