package view.enums;

public enum SuccessMessages {
    USERNAME_CHANGED_EN("Username was changed successfully!", "Change information was successful!", "Success"),
    USERNAME_CHANGED_FR("Le nom d'utilisateur a été changé avec succès!", "Modification des informations réussie!", "Succès"),

    PASSWORD_CHANGED_EN("Password changed successfully!", "Change information was successful!", "Success"),
    PASSWORD_CHANGED_FR("Le mot de passe a été changé avec succès!", "Modification des informations réussie!", "Succès"),

    LOGGED_OUT_EN("Logged out successfully!", "Logout successful!", "Success"),
    LOGGED_OUT_FR("Déconnexion réussie!", "Déconnexion réussie!", "Succès"),

    ACCOUNT_DELETED_EN("Account was deleted successfully!", "Delete Account was successful!", "Success"),
    ACCOUNT_DELETED_FR("Le compte a été supprimé avec succès!", "Suppression du compte réussie!", "Succès");

    private final String message;
    private final String header;
    private final String title;

    SuccessMessages(String message, String header, String title) {
        this.message = message;
        this.header = header;
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public String getHeader() {
        return header;
    }

    public String getTitle() {
        return title;
    }

    public static String getMessage(SuccessMessages successType, boolean isEnglish) {
        return isEnglish ? successType.getMessage() : valueOf(successType.name().replace("_EN", "_FR")).getMessage();
    }

    public static String getHeader(SuccessMessages successType, boolean isEnglish) {
        return isEnglish ? successType.getHeader() : valueOf(successType.name().replace("_EN", "_FR")).getHeader();
    }

    public static String getTitle(SuccessMessages successType, boolean isEnglish) {
        return isEnglish ? successType.getTitle() : valueOf(successType.name().replace("_EN", "_FR")).getTitle();
    }
}
