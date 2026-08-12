package com.myname.mymart.controller;

import com.myname.mymart.service.chat.ChatProvider;
import com.myname.mymart.service.chat.MockChatProvider;
import com.myname.mymart.util.JsonUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * POST /api/v1/chat  { message }  -> { reply }
 * Guardrails per Section 17: per-session rate limit (10 msgs/min), input length cap,
 * fixed prompt scope (enforced inside the ChatProvider), in-memory per-session cache.
 */
@WebServlet("/api/v1/chat")
public class ChatServlet extends HttpServlet {

    private static final int MAX_MESSAGES_PER_MINUTE = 10;
    private static final int MAX_MESSAGE_LENGTH = 500;

    private ChatProvider chatProvider;
    private final Map<String, RateWindow> rateLimits = new ConcurrentHashMap<>();
    private final Map<String, String> responseCache = new ConcurrentHashMap<>();

    @Override
    public void init() {
        // Swap for GeminiChatProvider(apiKey) once ai.chatbot.provider=gemini and a key are configured.
        chatProvider = new MockChatProvider();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(true);
        String sessionId = session.getId();

        if (!withinRateLimit(sessionId)) {
            JsonUtil.writeError(resp, 429, "RATE_LIMITED", "Too many messages — please wait a moment.");
            return;
        }

        String message = req.getParameter("message");
        if (message == null || message.isBlank()) {
            JsonUtil.writeError(resp, 400, "VALIDATION_ERROR", "Message is required");
            return;
        }
        if (message.length() > MAX_MESSAGE_LENGTH) {
            JsonUtil.writeError(resp, 400, "VALIDATION_ERROR", "Message too long (max " + MAX_MESSAGE_LENGTH + " chars)");
            return;
        }

        String cacheKey = sessionId + ":" + message.trim().toLowerCase();
        String reply = responseCache.computeIfAbsent(cacheKey,
                k -> chatProvider.getReply(message, "product/listing domain only"));

        JsonUtil.writeSuccess(resp, 200, Map.of("reply", reply));
    }

    private boolean withinRateLimit(String sessionId) {
        long now = System.currentTimeMillis();
        RateWindow window = rateLimits.computeIfAbsent(sessionId, k -> new RateWindow());
        synchronized (window) {
            if (now - window.windowStart > 60_000) {
                window.windowStart = now;
                window.count = 0;
            }
            if (window.count >= MAX_MESSAGES_PER_MINUTE) return false;
            window.count++;
            return true;
        }
    }

    private static class RateWindow {
        long windowStart = System.currentTimeMillis();
        int count = 0;
    }
}
