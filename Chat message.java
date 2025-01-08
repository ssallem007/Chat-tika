@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender;
    private String recipient; // اسم المستخدم المستلم
    private String message;
    private String timestamp;
    private String fileUrl; // رابط الملف المرفق
}
