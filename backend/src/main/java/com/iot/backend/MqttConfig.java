package com.iot.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
public class MqttConfig {

    // 1. Canalul (The Pipe) - Aici curg mesajele primite
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    // 2. Adaptorul (The Listener) - Se conectează la Mosquitto
    @Bean
    public MessageProducer inbound() {
        // "tcp://localhost:1883" -> Adresa Brokerului
        // "JavaBackendClient" -> ID-ul unic al acestui client (trebuie să fie diferit de cel din C++)
        // "casa/living/temperatura" -> Topicul la care ascultăm
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter("tcp://localhost:1883", "JavaBackendClient", "casa/living/temperatura");

        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setOutputChannel(mqttInputChannel()); // Trimite mesajele în canalul de mai sus
        return adapter;
    }

    // 3. Handler-ul (The Processor) - Ce facem cu mesajul?
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return message -> {
            // Aici ajunge mesajul brut (JSON-ul)
            String payload = (String) message.getPayload();
            System.out.println("Am primit mesaj: " + payload);
        };
    }
}