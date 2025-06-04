package view.enums;

public enum RegisterMenuText {
    // Titles
    REGISTER_TITLE("Register", "S'inscrire"),

    // Labels
    USERNAME_LABEL("Username", "Nom d'utilisateur"),
    PASSWORD_LABEL("Password", "Mot de passe"),
    COLOR_LABEL("Favorite Color", "Couleur préférée"),

    // Prompts
    USERNAME_PROMPT("Enter username", "Entrez votre nom d'utilisateur"),
    PASSWORD_PROMPT("Enter password", "Entrez votre mot de passe"),
    COLOR_PROMPT("e.g., Blue", "ex. Bleu"),

    // Buttons
    REGISTER_BUTTON("Register", "S'inscrire"),
    BACK_BUTTON("Back", "Retour"),

    // Messages
    REGISTER_ERROR_TITLE("Registration Error", "Erreur d'inscription"),
    EMPTY_FIELDS_MSG("Username or password cannot be empty",
            "Le nom d'utilisateur ou le mot de passe ne peut pas être vide"),
    INVALID_PASSWORD_MSG("Password must contain at least 8 characters, one special character (@%$#&*()_), one uppercase letter and one digit",
            "Le mot de passe doit contenir au moins 8 caractères, un caractère spécial (@%$#&*()_), une majuscule et un chiffre"),
    USERNAME_EXISTS_MSG("A user with this username already exists",
            "Un utilisateur avec ce nom existe déjà"),
    EMPTY_COLOR_MSG("Favorite color cannot be empty",
            "La couleur préférée ne peut pas être vide"),
    ERROR_TITLE("Error", "Erreur"),
    REGISTER_ERROR_MSG("Registration failed", "Échec de l'inscription"),
    BACK_ERROR_MSG("Failed to go back", "Échec du retour");

    private final String englishText;
    private final String frenchText;

    RegisterMenuText(String englishText, String frenchText) {
        this.englishText = englishText;
        this.frenchText = frenchText;
    }

    public String getText(boolean isEnglish) {
        return isEnglish ? englishText : frenchText;
    }
}