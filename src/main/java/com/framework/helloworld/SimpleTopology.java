package com.framework.helloworld;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;

/**
 * Created by Administrator on 2018/4/22 0022.
 */
public class SimpleTopology {

    public static void main(String[] args) {
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("simpleSpout",new SimpleSpout());
        builder.setBolt("simpleBolt",new SimpleBolt()).shuffleGrouping("simpleSpout");

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("simple",new Config(),builder.createTopology());

    }
}
