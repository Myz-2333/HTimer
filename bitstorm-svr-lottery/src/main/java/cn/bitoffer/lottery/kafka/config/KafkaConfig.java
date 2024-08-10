//package cn.bitoffer.lottery.kafka.config;
//
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.annotation.EnableKafka;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.config.KafkaListenerContainerFactory;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
//import org.springframework.kafka.listener.ContainerProperties;
//
//import java.util.Map;
//
//@Configuration
//@EnableKafka
//public class KafkaConfig {
//
//    @Autowired
//    private KafkaProperties properties;
//
//    /**
//     * 创建一个新的消费者工厂
//     * 创建多个工厂的时候 SpringBoot就不会自动帮忙创建工厂了；所以默认的还是自己创建一下
//     * @return
//     */
//    @Bean
//    public ConsumerFactory<Object, Object> kafkaConsumerFactory() {
//        Map<String, Object> map =  properties.buildConsumerProperties();
//        DefaultKafkaConsumerFactory<Object, Object> factory = new DefaultKafkaConsumerFactory<>( map);
//        return factory;
//    }
//
//    /**
//     * 创建一个新的消费者工厂
//     * 但是修改为不自动提交
//     *
//     * @return
//     */
//    @Bean
//    public ConsumerFactory<Object, Object> kafkaManualConsumerFactory() {
//        Map<String, Object> map =  properties.buildConsumerProperties();
//        map.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,false);
//        DefaultKafkaConsumerFactory<Object, Object> factory = new DefaultKafkaConsumerFactory<>( map);
//        return factory;
//    }
//
//
//    /**
//     * 手动提交的监听器工厂 (使用的消费组工厂必须 kafka.consumer.enable-auto-commit = false)
//     * @return
//     */
//    @Bean
//    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Integer, String>> kafkaManualAckListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<Integer, String> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(kafkaManualConsumerFactory());
//        //设置提交偏移量的方式 当Acknowledgment.acknowledge()侦听器调用该方法时，立即提交偏移量
//        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
//        return factory;
//    }
//
//    /**
//     * 监听器工厂 批量消费
//     * @return
//     */
//    @Bean
//    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Integer, String>> batchFactory() {
//        ConcurrentKafkaListenerContainerFactory<Integer, String> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(kafkaConsumerFactory());
//        factory.setBatchListener(true);
//        return factory;
//    }
//}
