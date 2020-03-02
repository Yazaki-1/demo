package com.example.demo.model.bean;

import com.alibaba.fastjson.JSON;
import lombok.Data;

@Data
public class LayuiDTO {
    public  Long status;
    public  String message;
    public  Object data;

    public  LayuiDTO() {
    }

    public  LayuiDTO(Object data) {
        this.status = Long.valueOf(200);
        this.message = "成功";
        this.data = data;
    }

    public  String toJson(){
        return JSON.toJSONString(this);
    }
    public  String toJson200(){
        this.status = Long.valueOf(200);
        this.message = "成功";
        return JSON.toJSONString(this);
    }
    public  String toJson500(){
        this.status= Long.valueOf(500);
        this.message="失败";
        return JSON.toJSONString(this);
    }
    public  String toJson500(String msg){
        this.status= Long.valueOf(500);
        this.message=msg;
        return JSON.toJSONString(this);
    }
}
