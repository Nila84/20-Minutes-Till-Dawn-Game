package view.enums;

public enum SettingsMenuText {
    // Titles
    SETTINGS_TITLE_EN("Settings"), SETTINGS_TITLE_FR("Paramètres"),

    // Key bindings
    UP_KEY_EN("Up Key"), UP_KEY_FR("Touche Haut"),
    DOWN_KEY_EN("Down Key"), DOWN_KEY_FR("Touche Bas"),
    LEFT_KEY_EN("Left Key"), LEFT_KEY_FR("Touche Gauche"),
    RIGHT_KEY_EN("Right Key"), RIGHT_KEY_FR("Touche Droite"),

    // Buttons
    SAVE_KEYS_EN("Save Keys"), SAVE_KEYS_FR("Enregistrer les touches"),
    CHANGE_THEME_EN("Change Theme"), CHANGE_THEME_FR("Changer le thème"),
    TOGGLE_SFX_EN("Toggle SFX"), TOGGLE_SFX_FR("Activer/Désactiver SFX"),
    NEXT_SONG_EN("Next Song"), NEXT_SONG_FR("Musique suivante"),
    AUTO_RELOAD_EN("Auto Reload"), AUTO_RELOAD_FR("Rechargement auto"),
    CHANGE_LANGUAGE_EN("Change Language"), CHANGE_LANGUAGE_FR("Changer la langue"),
    BACK_EN("Back"), BACK_FR("Retour"),

    // Messages
    KEYS_SAVED_EN("Keys Saved"), KEYS_SAVED_FR("Touches enregistrées"),
    KEYS_SAVED_MSG_EN("Key bindings have been updated successfully"),
    KEYS_SAVED_MSG_FR("Les raccourcis clavier ont été mis à jour avec succès"),
    INVALID_KEY_EN("Invalid Key"), INVALID_KEY_FR("Touche invalide"),
    INVALID_KEY_MSG_EN("One or more keys are not valid"),
    INVALID_KEY_MSG_FR("Une ou plusieurs touches ne sont pas valides"),
    EMPTY_KEYS_EN("Empty Keys"), EMPTY_KEYS_FR("Touches vides"),
    EMPTY_KEYS_MSG_EN("Keys cannot be empty"),
    EMPTY_KEYS_MSG_FR("Les touches ne peuvent pas être vides");

    private final String text;

    SettingsMenuText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static String getText(SettingsMenuText textType, boolean isEnglish) {
        if (isEnglish) {
            return textType.getText();
        } else {
            return valueOf(textType.name().replace("_EN", "_FR")).getText();
        }
    }
}