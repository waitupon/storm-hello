package com.framework.logfilter;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.kafka.*;
import org.apache.storm.kafka.bolt.KafkaBolt;
import org.apache.storm.kafka.bolt.mapper.FieldNameBasedTupleToKafkaMapper;
import org.apache.storm.kafka.bolt.selector.DefaultTopicSelector;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class LogFilterTopology {


    public static void main(String[] args) {
        //kafka配置
        String topic = "testflume";
        ZkHosts zkHosts = new ZkHosts("node1:2181");
        SpoutConfig spoutConfig = new SpoutConfig(zkHosts, topic, "/MyKafka" , "MyTrack");
        List<String> zkServers = new ArrayList<String>();
        System.err.println(zkHosts.brokerZkStr);
        for (String host : zkHosts.brokerZkStr.split(",")) {
            zkServers.add(host.split(":")[0]);
        }
        spoutConfig.zkServers = zkServers;
        spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
        spoutConfig.zkPort = 2181;
        spoutConfig.socketTimeoutMs = 60 * 1000;

        KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);

        //----
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("kafka_spout",kafkaSpout,3);
        builder.setBolt("filter",new FilterBolt()).shuffleGrouping("kafka_spout");

        //数据写出
        KafkaBolt kafkaBolt = new KafkaBolt().withTopicSelector(new DefaultTopicSelector("logError"))
                .withTupleToKafkaMapper(new FieldNameBasedTupleToKafkaMapper());
//
        builder.setBolt("kafka_bolt",kafkaBolt,1).shuffleGrouping("filter");

        Config conf = new Config();

        Properties props = new Properties();
        props.put("metadata.broker.list", "node1:9092");
        props.put("request.required.acks", "1");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        conf.put("kafka.broker.properties", props);

        /**
         * Kafka生产者ACK机制 0 ： 生产者不等待Kafka broker完成确认，继续发送下一条数据 1 ：
         * 生产者等待消息在leader接收成功确认之后，继续发送下一条数据 -1 ：
         * 生产者等待消息在follower副本接收到数据确认之后，继续发送下一条数据
         */
        props.put("request.required.acks", "1");
        props.put("serializer.class", "kafka.serializer.StringEncoder");


        conf.put("kafka.broker.properties", props);
        conf.put(Config.STORM_ZOOKEEPER_SERVERS, Arrays.asList(new String[] { "localhost"}));

        // 本地方式运行
        LocalCluster localCluster = new LocalCluster();
        localCluster.submitTopology("mytopology", conf, builder.createTopology());
    }
}
