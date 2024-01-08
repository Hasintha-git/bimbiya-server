package com.bimbiya.server.service.impl;

import com.bimbiya.server.dto.request.EmailSendDTO;
import com.bimbiya.server.entity.SystemUser;
import com.bimbiya.server.mapper.ResponseGenerator;
import com.bimbiya.server.repository.UserRepository;
import com.bimbiya.server.service.NotificationService;
import com.bimbiya.server.util.MessageConstant;
import com.bimbiya.server.util.ResponseCode;
import com.bimbiya.server.util.enums.Status;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @author Hasintha_S
 * @date 1/7/2024.
 */

@Service
@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationServiceImpl implements NotificationService {

    private ResponseGenerator responseGenerator;

    private JavaMailSender mailSender;

    private UserRepository userRepository;
    @Override
    public ResponseEntity<Object> sendEmail(EmailSendDTO emailSendDTO, Locale locale) throws Exception {

        Random random = new Random();

        // Generate a random 6-digit number
        int min = 100000; // Minimum 6-digit number
        int max = 999999; // Maximum 6-digit number
        int randomNumber = random.nextInt(max - min + 1) + min;
        log.debug(">>>>>>>>>> OTP is: "+randomNumber);

        SystemUser systemUser = Optional.ofNullable(userRepository.findByIdAndStatusNot(emailSendDTO.getUserId(), Status.deleted))
                .orElse(null);
        if (Objects.isNull(systemUser)) {
            return responseGenerator
                    .generateErrorResponse(emailSendDTO, HttpStatus.CONFLICT,
                            ResponseCode.NOT_FOUND ,  MessageConstant.USER_NOT_FOUND, new Object[] {emailSendDTO.getToEmail()},
                            locale);
        }

        log.debug("USER DETAILS >>>>>>>> "+systemUser);
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom("bimbiyasl@gmail.com");
        message.setTo(systemUser.getEmail());
        message.setSubject("Recovery Password with Bimbiya");
        String body = "Dear User,\n\n"
                + "Your password recovery OTP is: " + randomNumber + ".\n\n"
                + "Best regards,\n"
                + "Bimbiya Team";
        message.setText(body);
        mailSender.send(message);
        System.out.println("Email sent successfully!");


        Date systemDate = new Date();
        systemUser.setLastUpdatedTime(systemDate);
        systemUser.setOtp(randomNumber);
        userRepository.save(systemUser);

        return responseGenerator
                .generateSuccessResponse(emailSendDTO, HttpStatus.OK, ResponseCode.SAVED_SUCCESS,
                        MessageConstant.EMAIL_SEND, locale, null);

    }

    @Override
    public String encript(EmailSendDTO emailSendDTO, Locale locale) throws Exception {
            return encrypt(emailSendDTO.getStrToEncrypt(), "dgtL@301");
    }

    private static SecretKeySpec secretKey;
    private static byte[] key;

    public static String encrypt(String strToEncrypt, String secret) {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }
    public static void setKey(String myKey) {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
