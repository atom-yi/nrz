package com.yi.day7.pojo;

import com.yi.day7.proto.ProtoMessage;
import lombok.Data;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class User {
    private static final AtomicInteger NO = new AtomicInteger();
    String uid = String.valueOf(NO);
    String devId = UUID.randomUUID().toString();
    String token = UUID.randomUUID().toString();
    String nickName = "nickName";
    PLAT_TYPE platform = PLAT_TYPE.MAC;

    public enum PLAT_TYPE {
        WINDOWS(1), MAC(2), ANDROID(3), IOS(4), WEB(5), OTHER(6);
        private int type;
        PLAT_TYPE(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }

    private String sessionId;

    public void setPlatform(int type) {
        PLAT_TYPE[] values = PLAT_TYPE.values();
        for (int i = 0; i < values.length; i++) {
            if (values[i].getType() == type) {
                this.platform = values[i];
            }
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", devId='" + devId + '\'' +
                ", token='" + token + '\'' +
                ", nickName='" + nickName + '\'' +
                ", platform=" + platform +
                '}';
    }

    public static User fromMsg(ProtoMessage.LoginRequest info) {
        User user = new User();
        user.uid = info.getUid();
        user.devId = info.getDeviceId();
        user.token = info.getToken();
        user.setPlatform(info.getPlatform());
        return user;
    }
}
