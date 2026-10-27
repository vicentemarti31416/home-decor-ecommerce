package com.vcn.users.config;

import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
@Configuration
public class RabbitConfig {

    @Bean
    public JacksonJsonMessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
