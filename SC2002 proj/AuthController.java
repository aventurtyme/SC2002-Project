public class AuthController {
    public boolean login(UserRepository users, String id, String pw) {
        return users.findById(id).map(u -> u.checkPassword(pw)).orElse(false);
    }
    public void changePassword(User u, String newPw) {
        u.changePassword(newPw);
    }
}
