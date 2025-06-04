package view.enums;

public enum TalentMenuText {
    // Titles
    TALENT_MENU_TITLE_EN("TALENT MENU"), TALENT_MENU_TITLE_FR("MENU DES TALENTS"),
    CHEAT_CODES_EN("CHEAT CODES"), CHEAT_CODES_FR("TRICHES"),
    HEROES_EN("HEROES"), HEROES_FR("HÉROS"),
    ABILITIES_EN("ABILITIES"), ABILITIES_FR("CAPACITÉS"),
    KEY_BINDINGS_EN("KEY BINDINGS"), KEY_BINDINGS_FR("RACCOURCIS CLAVIER"),
    BACK_EN("BACK TO MAIN MENU"), BACK_FR("RETOUR AU MENU PRINCIPAL"),

    // Cheat codes
    REDUCE_TIME_EN("• Reduce Game Time:"), REDUCE_TIME_FR("• Réduire le temps:"),
    REDUCE_TIME_DESC_EN("Shortens remaining game time by 50%"), REDUCE_TIME_DESC_FR("Réduit le temps restant de 50%"),
    LEVEL_UP_EN("• Level Up Character:"), LEVEL_UP_FR("• Monter de niveau:"),
    LEVEL_UP_DESC_EN("Instantly levels up with full animations"), LEVEL_UP_DESC_FR("Monte instantanément de niveau"),
    RESTORE_HEALTH_EN("• Restore Health:"), RESTORE_HEALTH_FR("• Restaurer santé:"),
    RESTORE_HEALTH_DESC_EN("Fully restores health when empty"), RESTORE_HEALTH_DESC_FR("Restaure complètement la santé"),
    TRIGGER_BOSS_EN("• Trigger Boss Fight:"), TRIGGER_BOSS_FR("• Combos de boss:"),
    TRIGGER_BOSS_DESC_EN("Skips to boss fight immediately"), TRIGGER_BOSS_DESC_FR("Passe directement au boss"),
    INFINITE_AMMO_EN("• Infinite Ammo:"), INFINITE_AMMO_FR("• Munitions infinies:"),
    INFINITE_AMMO_DESC_EN("Unlimited ammo for 60 sec"), INFINITE_AMMO_DESC_FR("Munitions illimitées pour 60 sec"),

    // Key bindings
    MOVE_UP_EN("Move Up"), MOVE_UP_FR("Déplacement Haut"),
    MOVE_DOWN_EN("Move Down"), MOVE_DOWN_FR("Déplacement Bas"),
    MOVE_LEFT_EN("Move Left"), MOVE_LEFT_FR("Déplacement Gauche"),
    MOVE_RIGHT_EN("Move Right"), MOVE_RIGHT_FR("Déplacement Droite"),
    SHOOT_EN("Shoot"), SHOOT_FR("Tirer"),
    NOT_SET_EN("NOT SET"), NOT_SET_FR("NON DÉFINI");

    private final String text;

    TalentMenuText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static String getText(TalentMenuText textType, boolean isEnglish) {
        if (isEnglish) {
            return textType.getText();
        } else {
            return valueOf(textType.name().replace("_EN", "_FR")).getText();
        }
    }
}