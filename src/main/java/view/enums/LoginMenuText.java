package view.enums;

public enum LoginMenuText {
    // Titles
    LOGIN_TITLE("Login", "Connexion"),

    // Labels and Prompts
    USERNAME_LABEL("Username", "Nom d'utilisateur"),
    USERNAME_PROMPT("Enter username", "Entrez votre nom d'utilisateur"),
    PASSWORD_LABEL("Password", "Mot de passe"),
    PASSWORD_PROMPT("Enter password", "Entrez votre mot de passe"),

    // Buttons and Links
    LOGIN_BUTTON("Login", "Se connecter"),
    REGISTER_LINK("Don't have an account? Create a new one", "Pas de compte? Créez-en un"),
    FORGOT_PASSWORD_LINK("Forgot password?", "Mot de passe oublié?"),
    GUEST_LINK("Play as guest", "Jouer en invité"),

    // Messages
    LOGIN_ERROR_TITLE("Login Error", "Erreur de connexion"),
    EMPTY_FIELDS_MSG("Username or password cannot be empty", "Le nom d'utilisateur ou le mot de passe ne peut pas être vide"),
    USERNAME_INCORRECT_MSG("Username is incorrect", "Nom d'utilisateur incorrect"),
    PASSWORD_INCORRECT_MSG("Password is incorrect", "Mot de passe incorrect"),
    ERROR_TITLE("Error", "Erreur"),
    LOGIN_ERROR_MSG("An error occurred during login", "Une erreur est survenue lors de la connexion"),
    REGISTER_ERROR_MSG("Failed to go to registration", "Échec de l'accès à l'enregistrement"),
    FORGOT_PASSWORD_ERROR_MSG("Failed to process password recovery", "Échec de la récupération du mot de passe"),
    GUEST_ERROR_MSG("Failed to start as guest", "Échec du démarrage en tant qu'invité");

    private final String englishText;
    private final String frenchText;

    LoginMenuText(String englishText, String frenchText) {
        this.englishText = englishText;
        this.frenchText = frenchText;
    }

    public String getText(boolean isEnglish) {
        return isEnglish ? englishText : frenchText;
    }
}