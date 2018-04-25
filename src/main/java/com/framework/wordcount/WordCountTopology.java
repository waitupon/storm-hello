package com.framework.wordcount;

import com.framework.helloworld.SimpleBolt;
import com.framework.helloworld.SimpleSpout;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;

/**
 * Created by Administrator on 2018/4/22 0022.
 */
public class WordCountTopology {

    public static void main(String[] args) {
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("wordCountSpout",new WordCountSpout());
        builder.setBolt("splitBolt",new SplitBolt()).shuffleGrouping("wordCountSpout");
        builder.setBolt("WordCountBolt",new WordCountBolt()).shuffleGrouping("splitBolt");


        if(args.length>0){
            try {
                StormSubmitter.submitTopology(args[0],new Config(),builder.createTopology());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("wordCount",new Config(),builder.createTopology());
        }
    }
}
