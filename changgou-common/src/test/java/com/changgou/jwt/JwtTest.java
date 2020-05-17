package com.changgou.jwt;

import io.jsonwebtoken.*;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {

    /**
     * 令牌生成
     */
    @Test
    public void testCreateToken(){
        //构建JWT令牌的对象
        JwtBuilder builder = Jwts.builder();
        builder.setIssuer("召唤师峡谷");  //令牌颁发者
        builder.setIssuedAt(new Date());  //颁发时间
        builder.setExpiration(new Date(System.currentTimeMillis()+300000));  //设置令牌过期时间为20秒

        //自定义载荷信息
        Map<String,Object> userInfo = new HashMap<>();
        userInfo.put("company","华为大公司");
        userInfo.put("adress","中南海");
        userInfo.put("money",3500);
        builder.addClaims(userInfo);

        builder.setSubject("Jwt令牌测试");  //主题信息
        builder.signWith(SignatureAlgorithm.HS256,"itcast");  //1: 签名算法 2:密钥(盐)
        String token = builder.compact();  //生成令牌
        System.out.println(token);
    }

    /**
     * 令牌解析
     */
    @Test
    public void parseToken(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiLlj6zllKTluIjls6HosLciLCJpYXQiOjE1ODYzMDc5ODQsImV4cCI6MTU4NjMwODI4NCwibW9uZXkiOjM1MDAsImNvbXBhbnkiOiLljY7kuLrlpKflhazlj7giLCJhZHJlc3MiOiLkuK3ljZfmtbciLCJzdWIiOiJKd3Tku6TniYzmtYvor5UifQ.D2vvjxRYeYJ5bRUNlexf0HlxLmqiZ_r1x9Le00oxto4";
        Claims claims = Jwts.parser()
                .setSigningKey("itcast")  //密钥
                .parseClaimsJws(token)
                .getBody();  //获取解析后的数据
        System.out.println(claims.toString());
    }
}
