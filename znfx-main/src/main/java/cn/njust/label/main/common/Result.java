package cn.njust.label.main.common;
//与前端交互数据采用json格式
public class Result<T> {
    private String code;
    private String msg;
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Result(T data) {
        this.data = data;
    }

    public Result() {
    }
    // 0 成功 1：失败
    public static Result success(){
        Result result = new Result<>();
        result.setCode("200");
        result.setMsg("成功");
        return result;
    }
    public static <T> Result<T> success(T data){
        Result<T> result = new Result<>(data);
        result.setCode("200");
        result.setMsg("成功");
        return result;
    }
    public static Result fail(){
        Result result = new Result<>();
        result.setCode("1");
        result.setMsg("失败");
        return result;
    }
    public static <T> Result<T> fail(T data){
        Result<T> result = new Result<>(data);
        result.setCode("1");
        result.setMsg("失败");
        return result;
    }
}
