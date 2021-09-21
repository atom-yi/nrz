package com.yi.day7.server;

import com.yi.day7.pojo.User;
import lombok.Data;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Data
public class SessionMap {
    private static SessionMap INSTANCE = new SessionMap();

    private SessionMap() {
    }

    private Map<String, ServerSession> map = new ConcurrentHashMap<>();

    public static SessionMap inst() {
        return INSTANCE;
    }

    public void addSession(ServerSession session) {
        map.put(session.getSessionId(), session);
        System.out.println("用户登录 ID:" + session.getUser().getUid() +
                " 在线总数:" + map.size());
    }

    public ServerSession getSession(String sessionId) {
        return map.get(sessionId);
    }

    public List<ServerSession> getSessionsBy(String userId) {
        return map.values().stream()
                .filter(session -> session.getUser().getUid().equals(userId))
                .collect(Collectors.toList());
    }

    public void removeSession(String sessionId) {
        if (!map.containsKey(sessionId)) {
            return;
        }

        ServerSession session = map.get(sessionId);
        map.remove(sessionId);
        System.out.println("用户下线 ID:" + session.getUser().getUid() +
                " 在线总数:" + map.size());
    }

    public boolean hasLogin(User user) {
        for (Map.Entry<String, ServerSession> session : map.entrySet()) {
            User u = session.getValue().getUser();
            if (u.getUid().equals(user.getUid()) &&
                    u.getPlatform().equals(user.getPlatform())) {
                return true;
            }
        }
        return false;
    }
}
