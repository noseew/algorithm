package org.song.enctyption.encryptionapply;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.song.enctyption.encryptionapply.service.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EncryptionApplyApplicationTests {

    @Autowired
    EncryptionService encryptionService;

    @Test
    public void contextLoads() {

        System.out.println(encryptionService.getUserName());
        System.out.println(encryptionService.getPassword());
    }

}
