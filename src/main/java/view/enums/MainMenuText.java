package view.enums;

public enum MainMenuText {
    // User info
    SCORE_EN("Score: "), SCORE_FR("Score: "),

    // Button texts
    TALENT_MENU_EN("Talent Menu"), TALENT_MENU_FR("Menu des Talents"),
    PRE_GAME_EN("Pre-Game Menu"), PRE_GAME_FR("Menu Pré-Jeu"),
    RESUME_GAME_EN("Resume Game"), RESUME_GAME_FR("Reprendre Partie"),
    PROFILE_MENU_EN("Profile Menu"), PROFILE_MENU_FR("Menu Profil"),
    SCOREBOARD_EN("Scoreboard"), SCOREBOARD_FR("Tableau des Scores"),
    SETTINGS_EN("Settings"), SETTINGS_FR("Paramètres"),
    LOGOUT_EN("Logout"), LOGOUT_FR("Déconnexion"),
    EXIT_EN("Exit"), EXIT_FR("Quitter"),

    // Alerts
    ACCESS_DENIED_EN("Access Denied"), ACCESS_DENIED_FR("Accès Refusé"),
    MUST_LOGIN_EN("You must be logged in"), MUST_LOGIN_FR("Vous devez être connecté"),
    NO_SAVE_EN("No Save Found"), NO_SAVE_FR("Aucune Sauvegarde"),
    ERROR_EN("Error"), ERROR_FR("Erreur");

    private final String text;

    MainMenuText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static String getText(MainMenuText textType, boolean isEnglish) {
        if (isEnglish) {
            return textType.getText();
        } else {
            return valueOf(textType.name().replace("_EN", "_FR")).getText();
        }
    }
}