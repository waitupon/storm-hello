package com.framework.ack;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;

public class Main {
    public static void main(String[] args) {
        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("spout", new MySpout(), 1);
        builder.setBolt("bolt", new MyBolt(), 1).shuffleGrouping("spout");

        Config conf = new Config() ;
        conf.setDebug(false);
        conf.setMessageTimeoutSecs(30);

        if (args.length > 0) {
            try {
                StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
            } catch (AlreadyAliveException e) {
                e.printStackTrace();
            } catch (InvalidTopologyException e) {
                e.printStackTrace();
            } catch (AuthorizationException e) {
                e.printStackTrace();
            }
        }else {
            LocalCluster localCluster = new LocalCluster();
            localCluster.submitTopology("mytopology", conf, builder.createTopology());
        }
    }
}
