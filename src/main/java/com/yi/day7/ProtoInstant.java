package com.yi.day7;

import java.security.InvalidParameterException;

public class ProtoInstant {
    public static final short MAGIC_CODE = 0x86;
    public static final short VERSION_CODE = 0x01;

    public interface Platform {
        int WINDOWS = 1;
        int MAC = 2;
        int ANDROID = 3;
        int IOS = 4;
        int WEB = 5;
        int UNKNOWN = 6;
    }

    public enum ResultCodeEnum {
        SUCCESS(0, "SUCCESS"),
        AUTH_FAILED(1, "登录失败"),
        NO_TOKEN(2, "没有授权码"),
        UNKNOWN_ERROR(3, "未知错误"),
        ;
        private Integer code;
        private String desc;
        ResultCodeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public Integer getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static ResultCodeEnum fromCode(int code) {
            ResultCodeEnum[] values = ResultCodeEnum.values();
            for (ResultCodeEnum result : values) {
                if (code == result.code) {
                    return result;
                }
            }
            throw new InvalidParameterException();
        }
    }
}
