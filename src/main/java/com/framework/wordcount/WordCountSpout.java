package com.framework.wordcount;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class WordCountSpout extends BaseRichSpout {
    SpoutOutputCollector spoutOutputCollector;

    Random r = new Random();
    String[]texts = new String[]{
            "hello hello itaits",
            "itaits good good good",
            "good hello"
    };
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.spoutOutputCollector = spoutOutputCollector;
    }

    public void nextTuple() {
        String words = texts[r.nextInt(texts.length)];
        List list = new Values(words);
        spoutOutputCollector.emit(list);

        System.err.println("===words:" + words);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("word"));
    }
}
