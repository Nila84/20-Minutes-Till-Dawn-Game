package view.enums;

public enum ErrorMessages {
    EMPTY_USERNAME_EN("New username field is empty!", "Change information Error", "Error"),
    EMPTY_USERNAME_FR("Le champ du nouveau nom d'utilisateur est vide!", "Erreur de modification des informations", "Erreur"),
    USERNAME_EXISTS_EN("New username exists!", "Change information Error", "Error"),
    USERNAME_EXISTS_FR("Le nouveau nom d'utilisateur existe!", "Erreur de modification des informations", "Erreur"),

    EMPTY_PASSWORD_EN("Password field or new password field is empty!", "Change information failed!", "Error"),
    EMPTY_PASSWORD_FR("Le champ du mot de passe est vide!", "Échec de modification des informations!", "Erreur"),
    PASSWORD_MISMATCH_EN("Password confirmation and password doesn't match!", "Change information failed!", "Error"),
    PASSWORD_MISMATCH_FR("La confirmation du mot de passe ne correspond pas!", "Échec de modification des informations!", "Erreur"),
    INVALID_PASSWORD_EN("Your password format is invalid!", "Change information failed!", "Error"),
    INVALID_PASSWORD_FR("Le format de votre mot de passe est invalide!", "Échec de modification des informations!", "Erreur");

    private final String message;
    private final String header;
    private final String title;

    ErrorMessages(String message, String header, String title) {
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

    public static String getMessage(ErrorMessages errorType, boolean isEnglish) {
        return isEnglish ? errorType.getMessage() : valueOf(errorType.name().replace("_EN", "_FR")).getMessage();
    }

    public static String getHeader(ErrorMessages errorType, boolean isEnglish) {
        return isEnglish ? errorType.getHeader() : valueOf(errorType.name().replace("_EN", "_FR")).getHeader();
    }

    public static String getTitle(ErrorMessages errorType, boolean isEnglish) {
        return isEnglish ? errorType.getTitle() : valueOf(errorType.name().replace("_EN", "_FR")).getTitle();
    }
}

