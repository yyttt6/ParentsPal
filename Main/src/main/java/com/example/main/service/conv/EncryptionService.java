package com.example.main.service.conv;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

    private final StringEncryptor stringEncryptor;

    @Autowired
    public EncryptionService(StringEncryptor stringEncryptor) {
        this.stringEncryptor = stringEncryptor;
    }

    public String encrypt(String content) {
        return stringEncryptor.encrypt(content);
    }

    public String decrypt(String encryptedContent) {
        return stringEncryptor.decrypt(encryptedContent);
    }
}