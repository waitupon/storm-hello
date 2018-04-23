package com.framework.wordcount;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.util.HashMap;
import java.util.Map;

public class WordCountBolt extends BaseRichBolt {
    Map<String,Integer>dbMap = new HashMap<String, Integer>();

    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {

    }

    public void execute(Tuple tuple) {
        String word = tuple.getString(0);
        int count = 1;
        if(dbMap.containsKey(word)){
            count = dbMap.get(word).intValue() + 1;
        }
        System.out.println("###word:" + word + ", count=" + count);
        dbMap.put(word,count);

    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
