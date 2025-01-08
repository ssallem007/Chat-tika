@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String displayName;
    private String profilePictureUrl;
    private String status;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles; // مثل ADMIN, MODERATOR, USER
}
