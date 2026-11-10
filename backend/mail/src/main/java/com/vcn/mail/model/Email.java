package com.vcn.mail.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Email {

    @EqualsAndHashCode.Include
    private String recipient;
    private String subject;
    @EqualsAndHashCode.Include
    private String message;
}