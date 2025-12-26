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
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

@Configuration
public class MqttConfig {

    // 1. Canalul - Aici curg mesajele primite
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    // 2. Adaptorul - Se conectează la Mosquitto
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

    // 3. Handler-ul
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return message -> {
            String payload = (String) message.getPayload();

            ObjectMapper mapper = new ObjectMapper();

            try {
                SensorData data = mapper.readValue(payload, SensorData.class);

                System.out.println(data.toString());

                if (data.getValue() > 24.0) {
                    System.out.println(" ⚠️ ALERTA: Temperatura ridicata");
                }

            } catch (IOException e) {
                System.err.println("Nu am putut citi JSON-ul: " + e.getMessage());
            }
        };
    }
}

