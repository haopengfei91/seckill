package com.example.returntype;

public class  CommonReturnType{

    private String status;
    // status = success, data 内返回前端需要的 json数据
    // status = fail, data 内使用通用的错误码格式

    private Object result;

    public static CommonReturnType create(Object result) {
        return create(result, "success");
    }

    public static CommonReturnType create(Object result, String status) {
        CommonReturnType type = new CommonReturnType();
        type.setStatus(status);
        type.setResult(result);
        return type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

}
