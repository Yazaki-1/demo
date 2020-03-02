package com.example.demo.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

public class LayuiRequest {
    public static String code200(Object data){
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",200);
        jsonObject.put("msg","成功");
        if (data==null){
            return jsonObject.toJSONString();
        }
        if (data.getClass()== List.class){
            jsonObject.put("data", JSONArray.parseArray(JSON.toJSONString(data)));
        }else if (data.getClass()== Map.class){
            jsonObject.put("data", JSONObject.parseObject(JSON.toJSONString(data)));
        }else {
            jsonObject.put("data",data);
        }
        return jsonObject.toJSONString();
    }

    public  static String code500(String msg){
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",500);
        if (msg==null){
            msg="错误";
        }
        jsonObject.put("msg",msg);
        return jsonObject.toJSONString();
    }

    public static String Layui200(Object data){
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",200);
        jsonObject.put("msg","成功");
        if (data==null){
            return jsonObject.toJSONString();
        }
        if (data.getClass()== List.class){
            jsonObject.put("data", JSONArray.parseArray(JSON.toJSONString(data)));
        }else if (data.getClass()== Map.class){
            jsonObject.put("data", JSONObject.parseObject(JSON.toJSONString(data)));
        }else {
            jsonObject.put("data",data);
        }
        return jsonObject.toJSONString();
    }
}
