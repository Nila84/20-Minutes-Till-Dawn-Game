package view.enums;

public enum ScoreboardMenuText {
    // Titles
    SCOREBOARD_TITLE("Scoreboard", "Classement"),

    // Buttons
    TIME_BUTTON("Living Time", "Temps de survie"),
    KILLS_BUTTON("Kills Num", "Nombre de kills"),
    SCORE_BUTTON("Score", "Score"),
    USERNAME_BUTTON("Username", "Nom d'utilisateur"),
    BACK_BUTTON("Back", "Retour"),

    // Labels
    SCORE_LABEL("Score", "Score"),
    KILLS_LABEL("Kills", "Kills"),
    TIME_LABEL("Survival", "Survie"),

    // Messages
    ERROR_TITLE("Error", "Erreur"),
    BACK_ERROR_MSG("Failed to go back", "Échec du retour");

    private final String englishText;
    private final String frenchText;

    ScoreboardMenuText(String englishText, String frenchText) {
        this.englishText = englishText;
        this.frenchText = frenchText;
    }

    public String getText(boolean isEnglish) {
        return isEnglish ? englishText : frenchText;
    }
}