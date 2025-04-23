package org.example.sasalele_pos.services;

import org.example.sasalele_pos.model.User;
import org.example.sasalele_pos.database.UserDAO;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {
    private User currentUser;
    private UserDAO userDAO = new UserDAO();
    private LogService logService = new LogService();

    public boolean registerUser(String username, String password, String role) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = new User(username, hashedPassword, role);
        userDAO.addUser(user);
        try {
            // Catat log
            logService.logAction(
                    "AUTH",
                    "Pengguna " + username + " telah membuat akun Sasalele."
            );

        } catch (Exception e) {
            logService.logAction("ERROR", "Pengguna " + username + " Gagal register: " + e.getMessage());
        }
        return true;
    }

    // Method login yang sudah ada
    public boolean loginUser(String username, String password) {
        User user = userDAO.getUser(username);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            this.currentUser = user; // ✅ Simpan user yang berhasil login
            try {
                // Catat log
                logService.logAction(
                        "AUTH",
                        "Pengguna" + username + " login Sasalele."
                );

            } catch (Exception e) {
                logService.logAction("ERROR", "Pengguna " + username + " Gagal login: " + e.getMessage());
            }
            return true;
        }
        return false;
    }

    // Method untuk mendapatkan user yang sedang login
    public User getCurrentUser() {
        return currentUser; // ✅ Kembalikan currentUser
    }

    public boolean userExists(String username) {
        return userDAO.checkUser(username);
    }
}