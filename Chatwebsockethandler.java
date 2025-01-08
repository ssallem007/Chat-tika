public class ChatWebSocketHandler extends TextWebSocketHandler {
    private final ChatMessageRepository chatMessageRepository;
    private final Map<WebSocketSession, String> userSessions = new HashMap<>(); // الجلسات المرتبطة بالمستخدمين

    public ChatWebSocketHandler(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        userSessions.put(session, "Anonymous"); // تسجيل الجلسة كمستخدم مجهول
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        if (payload.startsWith("USERNAME:")) {
            String username = payload.substring(9);
            userSessions.put(session, username);
            return;
        }

        String[] parts = payload.split(":", 3);
        String recipient = parts[0];
        String text = parts[2];
        String sender = userSessions.get(session);

        // إنشاء رسالة وحفظها
        String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(sender);
        chatMessage.setRecipient(recipient.equals("ALL") ? null : recipient);
        chatMessage.setMessage(text);
        chatMessage.setTimestamp(timestamp);
        chatMessageRepository.save(chatMessage);

        // إرسال الرسالة
        for (WebSocketSession userSession : userSessions.keySet()) {
            String username = userSessions.get(userSession);
            if (recipient.equals("ALL") || username.equals(recipient)) {
                userSession.sendMessage(new TextMessage("[" + timestamp + "] " + sender + ": " + text));
            }
        }
    }
}
