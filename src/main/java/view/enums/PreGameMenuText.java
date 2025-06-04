package view.enums;

public enum PreGameMenuText {
    // Hero names
    SHANA_EN("SHANA"), SHANA_FR("SHANA"),
    DIAMOND_EN("DIAMOND"), DIAMOND_FR("DIAMOND"),
    SCARLET_EN("SCARLET"), SCARLET_FR("SCARLET"),
    LILITH_EN("LILITH"), LILITH_FR("LILITH"),
    DASHER_EN("DASHER"), DASHER_FR("DASHER"),

    // Weapon names
    REVOLVER_EN("Revolver"), REVOLVER_FR("Revolver"),
    SHOTGUN_EN("Shotgun"), SHOTGUN_FR("Fusil à pompe"),
    SMG_DUAL_EN("SMGs Dual"), SMG_DUAL_FR("SMGs Double"),

    // Time label
    TIME_LABEL_EN("Game Time: "), TIME_LABEL_FR("Temps de jeu: "),
    MINUTES_EN(" minutes"), MINUTES_FR(" minutes"),

    // Buttons
    START_GAME_EN("Start Game"), START_GAME_FR("Commencer le jeu"),
    BACK_EN("Back"), BACK_FR("Retour");

    private final String text;

    PreGameMenuText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static String getText(PreGameMenuText textType, boolean isEnglish) {
        if (isEnglish) {
            return textType.getText();
        } else {
            return valueOf(textType.name().replace("_EN", "_FR")).getText();
        }
    }
}