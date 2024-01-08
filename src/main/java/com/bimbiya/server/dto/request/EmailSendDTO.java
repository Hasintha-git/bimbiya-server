package com.bimbiya.server.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Hasintha_S
 * @date 1/7/2024.
 */

@NoArgsConstructor
@Data
public class EmailSendDTO {
    private Long userId;
    private String toEmail;
    private String subject;
    private String body;
    private String strToEncrypt;
    private String secret;

}
