package com.vcn.mail.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue enableAccountQueue(@Value("${rabbitmq.queue.users-enable_account}") String name) {
        return new Queue(name, true);
    }

    @Bean
    public Queue resetPasswordQueue(@Value("${rabbitmq.queue.users-reset_password}") String name) {
        return new Queue(name, true);
    }

    /**
     * Este Bean sirve convertir correctamente el @Payload en un objeto Email
     * Convierte los objetos Java a JSON al enviar mensajes por RabbitMQ, y de JSON a objeto al recibirlos.
     * Spring Boot detecta cualquier bean de tipo MessageConverter y lo conecta automáticamente al RabbitTemplate
     * (para convertAndSend) y a los @RabbitListener. Sin él se usaría el SimpleMessageConverter,
     * que tira de serialización Java nativa: obliga a que las clases implementen Serializable,
     * acopla productor y consumidor a la misma clase exacta (mismo paquete incluido)
     * y es un riesgo conocido de seguridad al deserializar.
     * Por eso tiene que estar en los dos lados: en el microservicio que publica (orders, users) y en el que consume (mail).
     * Si este es el del consumidor y quieres seguir pudiendo publicar mensajes a mano desde la interfaz de RabbitMQ,
     * le faltaría el setAssumeSupportedContentType(true), porque esos mensajes llegan sin content_type
     * y el conversor los rechaza.
     */
    @Bean
    public JacksonJsonMessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
