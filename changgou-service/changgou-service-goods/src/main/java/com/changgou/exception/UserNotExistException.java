package com.changgou.exception;

/**
 * 自定义异常
 */
public class UserNotExistException extends RuntimeException {


    private static final long serialVersionUID = 5403144579027495083L;

    private String id;  //这是个例子,表示抛异常时把用户id传来,显示到前端

    public UserNotExistException(String id){
        super("user not exist");
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
