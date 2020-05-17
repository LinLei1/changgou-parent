package com.changgou.user.controller;

import com.alibaba.fastjson.JSON;
import com.changgou.user.config.TokenDecode;
import com.changgou.user.pojo.User;
import com.changgou.user.service.UserService;
import com.github.pagehelper.PageInfo;
import entity.BCrypt;
import entity.JwtUtil;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/****
 * @Author:传智播客
 * @Description:
 * @Date 2019/6/14 0:18
 *****/

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     */
    @GetMapping(value = "/login")
    public Result login(String username, String password, HttpServletResponse response){
        //查询用户信息
        User user = userService.findById(username);

        //对比密码
        if (BCrypt.checkpw(password,user.getPassword())){
            //创建用户令牌信息
            Map<String,Object> tokenMap = new HashMap<>();
            tokenMap.put("role","USER");
            tokenMap.put("success","SUCCESS");
            tokenMap.put("username",username);
            String token = JwtUtil.createJWT(UUID.randomUUID().toString(), JSON.toJSONString(tokenMap), null);

            //把令牌信息存入到Cookie
            Cookie cookie = new Cookie("Authorization", token);
            cookie.setDomain("localhost");
            cookie.setPath("/");
            response.addCookie(cookie);

            //密码匹配,登录成功,把令牌作为参数返回给用户
            return new Result(true,StatusCode.OK,"登录成功了!",token);
        }
        //密码匹配失败,登录失败,账号或密码有误
        return new Result(false,StatusCode.LOGINERROR,"账号或密码错误!");
    }

    /***
     * User分页条件搜索实现
     * @param user
     * @param page
     * @param size
     * @return
     */
    @PostMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody(required = false)  User user, @PathVariable  int page, @PathVariable  int size){
        //调用UserService实现分页条件查询User
        PageInfo<User> pageInfo = userService.findPage(user, page, size);
        return new Result(true,StatusCode.OK,"查询成功",pageInfo);
    }

    /***
     * User分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @GetMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable  int page, @PathVariable  int size){
        //调用UserService实现分页查询User
        PageInfo<User> pageInfo = userService.findPage(page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"查询成功",pageInfo);
    }

    /***
     * 多条件搜索品牌数据
     * @param user
     * @return
     */
    @PostMapping(value = "/search" )
    public Result<List<User>> findList(@RequestBody(required = false)  User user){
        //调用UserService实现条件查询User
        List<User> list = userService.findList(user);
        return new Result<List<User>>(true,StatusCode.OK,"查询成功",list);
    }

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}" )
    public Result delete(@PathVariable String id){
        //调用UserService实现根据主键删除
        userService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /***
     * 修改User数据
     * @param user
     * @param id
     * @return
     */
    @PutMapping(value={"/{id}","/load/{id}"})
    public Result update(@RequestBody  User user,@PathVariable String id){
        //设置主键值
        user.setUsername(id);
        //调用UserService实现修改User
        userService.update(user);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /***
     * 新增User数据
     * @param
     * @return
     */
    @PostMapping
    public Result add(@RequestBody User user){
        String pwd = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(pwd);
        userService.add(user);
        return new Result(true,StatusCode.OK,"添加成功");
    }

    /***
     * 根据ID查询User数据
     * @param id
     * @return
     */
    @GetMapping("/load/{id}")
    public Result<User> findById(@PathVariable String id){
        //调用UserService实现根据主键查询User
        User user = userService.findById(id);
        return new Result<User>(true,StatusCode.OK,"查询成功",user);
    }

    /***
     * 查询User全部数据
     * @return
     */
    //@PreAuthorize("ha")
    @GetMapping
    public Result<List<User>> findAll(){
        //调用UserService实现查询所有User
        List<User> list = userService.findAll();
        return new Result<List<User>>(true, StatusCode.OK,"查询成功",list) ;
    }


    /***
     * 添加用户积分
     * @param points
     * @return
     */
    @GetMapping(value = "/points/add")
    public Result addPoints(Integer points){
        //获取用户名
        String username = TokenDecode.getUserInfo().get("username");
        //调用Service增加积分
        userService.addPoints(username,points);
        return new Result(true,StatusCode.OK,"添加积分成功!");
    }
}
