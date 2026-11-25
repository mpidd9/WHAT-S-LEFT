package whats_left.model;

/**
 * Menyimpan pengguna yang sedang login selama aplikasi berjalan.
 */
public final class Session {
    private static User currentUser;

    private Session() {
    }

    public static void login(User user) {
        currentUser = user;
    }

    public static void logout() {
        currentUser = null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    /** ID bisnis milik akun Business yang login, atau -1 jika tidak ada. */
    public static int getBusinessId() {
        if (currentUser != null && currentUser.getBusinessId() != null) {
            return currentUser.getBusinessId();
        }
        return -1;
    }

    /** Nama lengkap customer yang login, atau string kosong. */
    public static String getCustomerName() {
        if (currentUser != null && currentUser.isCustomer()
                && currentUser.getFullName() != null) {
            return currentUser.getFullName();
        }
        return "";
    }

    /** Nomor telepon customer yang login, atau string kosong. */
    public static String getCustomerPhone() {
        if (currentUser != null && currentUser.isCustomer()
                && currentUser.getPhone() != null) {
            return currentUser.getPhone();
        }
        return "";
    }
}
