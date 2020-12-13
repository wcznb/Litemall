//package cn.edu.xmu.footprint.service.mq;
//
//import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
//import org.apache.rocketmq.spring.annotation.ConsumeMode;
//import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
//import org.apache.rocketmq.spring.core.RocketMQListener;
//import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
//import org.apache.rocketmq.spring.core.RocketMQTemplate;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//
//@Service
//@RocketMQMessageListener(topic = "log-topic", selectorExpression = "1", consumeMode = ConsumeMode.CONCURRENTLY, consumeThreadMax = 10, consumerGroup = "log-group")
//public class FootprintListener implements RocketMQListener<String> {
//
////    @Resource
////    private RocketMQTemplate rocketMQTemplate;
//
//    @Override
//    public void onMessage(String message) {
//        System.out.println(message);
//    }
//}
