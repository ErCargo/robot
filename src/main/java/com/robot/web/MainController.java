package com.robot.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Controller
public class MainController {
    @RequestMapping(value = "/", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    String home(@RequestBody Request request) throws Exception {
        String content = request.getText().getContent();
        Body body = new Body();
        body.setMsgtype("text");
        body.setText(Text.builder().content(tuling(content)).build());
        body.setAt(At.builder().atDingtalkIds(Arrays.asList(request.getSenderId())).build());
        return JSON.toJSONString(body);
    }

    public String tuling(String context) throws Exception {
        String APIKEY = "402536689fcf4282ae1f213e70c6a819";
        String getURL = "http://www.tuling123.com/openapi/api?key=" + APIKEY + "&info=" + context;
        URL getUrl = new URL(getURL);
        HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
        connection.connect();
        StringBuffer sb = null;

        // 取得输入流，并使用Reader读取
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            sb = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } finally {
            // 断开连接
            connection.disconnect();
        }
        return sb == null ? "啥都没返回！" : JSON.parseObject(new String(sb), TuLingBody.class).getText();
    }
    
    @Data
    public static class Request {
        private String msgtype;
        private Text text;
        private Long createAt;
        private String conversationType;
        private String conversationId;
        private String conversationTitle;
        private String senderId;
        private String senderNick;
        private String senderStaffId;
        private boolean isAdmin;
        private String context;
        private String chatbotCorpId;
        private String chatbotUserId;
        private List<User> atUsers;
    }
    
    @Data
    public static class User {
        String dingtalkId;
    }

    @Data
    public static class Body {
        private String msgtype;
        private Text text;
        private At at;
        private boolean isAtAll;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Text {
        private String content;
    }

    @Data
    public static class Image {
        private String picURL;
    }

    @Data
    public static class MarkDown {
        private String title;
        private String text;
    }

    @Data
    public static class ActionCard {
        private String title;
        private String text;
        private String hideAvatar;
        private String btnOrientation;
        private String singleTitle;
        private String singleURL;
        private List<Btn> btns;
    }

    @Data
    public static class Btn {
        private String title;
        private String actionURL;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class At {
        private List<String> atMobiles;
        private List<String> atDingtalkIds;
    }
    
    @Data
    public static class TuLingBody {
        private Long code;
        private String text;
    }
}
