package org.song.enctyption.encryptionapply.controller;

import com.alibaba.fastjson.JSONObject;
import org.song.enctyption.encryptionapply.annotations.SecurityParameter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: jiali.song@song.com
 * @date: 2018年07月18日 15:35:14
 **/

@RestController
@RequestMapping("/encryption")
public class EncryptionController {

    @PostMapping("/test")
    @SecurityParameter/*(inDecode = false, outEncode = false)*/
    public Object test(@RequestBody Map<String, Object> data) {
        System.out.println("req data: "+JSONObject.toJSONString(data));
        Map<String, Object> res = new HashMap<>();
        res.put("data", "detail");
        System.out.println("res data: "+ JSONObject.toJSONString(res));
        return res;
    }
}