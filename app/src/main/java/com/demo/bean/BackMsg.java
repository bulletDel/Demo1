package com.demo.bean;

import org.json.JSONObject;

/**
 * 访问网络返回消息
 *
 * @author pc
 */
public class BackMsg {
    private String respCode; //网络请求返回码
    private String respMsg;  //返回结果文字信息
    private boolean isSuccess; //请求结果是否成功
    private JSONObject obj;  //返回数据已json格式，进行保存

    public BackMsg() {
    }

    public BackMsg(String respMsg) {
        this.respMsg = respMsg;
        isSuccess = false;
    }

    public BackMsg(String respMsg, String respCode) {
        this.respMsg = respMsg;
        this.respCode = respCode;
    }


    public BackMsg(boolean isSuccess, JSONObject obj) {
        this.isSuccess = isSuccess;
        this.obj = obj;
    }

    public BackMsg(String respMsg, boolean isSuccess,
                   JSONObject obj) {
        this.respMsg = respMsg;
        this.isSuccess = isSuccess;
        this.obj = obj;
    }

    public BackMsg(String respCode, String respMsg, boolean isSuccess) {
        this.respCode = respCode;
        this.respMsg = respMsg;
        this.isSuccess = isSuccess;
    }

    public BackMsg(String respCode, String respMsg, boolean isSuccess,
                   JSONObject obj) {
        this.respCode = respCode;
        this.respMsg = respMsg;
        this.isSuccess = isSuccess;
        this.obj = obj;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public JSONObject getObj() {
        return obj;
    }

    public void setObj(JSONObject obj) {
        this.obj = obj;
    }

    public String convertToJson() {
        return "{\"respCode\":\"" + respCode + "\",\"respMsg\":\"" + respMsg + "\",\"isSuccess\":\""
                + isSuccess + "\",\"obj\":\"" + (obj != null ? obj.toString() : "") + "\"}";
    }
}
