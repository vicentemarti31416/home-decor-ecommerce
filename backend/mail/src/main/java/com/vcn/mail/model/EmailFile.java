package com.vcn.mail.model;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class EmailFile {

    @EqualsAndHashCode.Include
    private String recipient;
    private String subject;
    @EqualsAndHashCode.Include
    private String message;
    private MultipartFile file;
}
