package view.enums;

public enum ProfileMenuText {
    NEW_USERNAME_EN("New Username"), NEW_USERNAME_FR("Nouveau nom d'utilisateur"),
    USERNAME_PROMPT_EN("username"), USERNAME_PROMPT_FR("nom d'utilisateur"),
    CHANGE_USERNAME_EN("Change username"), CHANGE_USERNAME_FR("Changer le nom d'utilisateur"),
    NEW_PASSWORD_EN("New Password"), NEW_PASSWORD_FR("Nouveau mot de passe"),
    PASSWORD_PROMPT_EN("password"), PASSWORD_PROMPT_FR("mot de passe"),
    CONFIRMATION_EN("Confirmation Password"), CONFIRMATION_FR("Confirmation du mot de passe"),
    CHANGE_PASSWORD_EN("Change password"), CHANGE_PASSWORD_FR("Changer le mot de passe"),
    LOGOUT_EN("Logout"), LOGOUT_FR("Se déconnecter"),
    DELETE_ACCOUNT_EN("Delete account"), DELETE_ACCOUNT_FR("Supprimer le compte"),
    CHANGE_AVATAR_EN("Change Avatar"), CHANGE_AVATAR_FR("Changer l'avatar"),
    BACK_EN("Back"), BACK_FR("Retour");

    private final String text;

    ProfileMenuText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static String getText(view.enums.ProfileMenuText textType, boolean isEnglish) {
        if (isEnglish) {
            return textType.getText();
        } else {
            return valueOf(textType.name().replace("_EN", "_FR")).getText();
        }
    }
}