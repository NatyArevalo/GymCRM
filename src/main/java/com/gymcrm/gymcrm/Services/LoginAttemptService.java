package com.gymcrm.gymcrm.Services;

import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService {

    private static final int MAX_ATTEMPTS = 3;
    private static final long LOCK_TIME = 5 * 60 * 1000;

    private final ConcurrentHashMap<String, LoginAttempt> loginAttempts = new ConcurrentHashMap<>();

    public void loginFailed(String username) {
        LoginAttempt attempt = loginAttempts.getOrDefault(username, new LoginAttempt());
        attempt.incrementAttempts();
        loginAttempts.put(username, attempt);
    }

    public boolean isBlocked(String username) {
        LoginAttempt attempt = loginAttempts.get(username);
        if (attempt == null) {
            return false;
        }

        if (attempt.getAttempts() >= MAX_ATTEMPTS) {
            if (System.currentTimeMillis() - attempt.getLastFailedAttempt() > LOCK_TIME) {
                loginAttempts.remove(username); // Reset after lock time
                return false;
            }
            return true;
        }
        return false;
    }

    public void loginSucceeded(String username) {
        loginAttempts.remove(username);
    }

    private static class LoginAttempt {
        private int attempts = 0;
        private long lastFailedAttempt;

        public void incrementAttempts() {
            attempts++;
            lastFailedAttempt = System.currentTimeMillis();
        }

        public int getAttempts() {
            return attempts;
        }

        public long getLastFailedAttempt() {
            return lastFailedAttempt;
        }
    }
}
