package view.enums;

public enum GameScreenText {
    // Game UI elements
    TIME_EN("Time: "), TIME_FR("Temps: "),
    HP_EN("HP: "), HP_FR("Vie: "),
    KILLS_EN("Kills: "), KILLS_FR("Tués: "),
    WEAPONS_EN("Weapons: "), WEAPONS_FR("Armes: "),
    ABILITY_EN("Ability: "), ABILITY_FR("Capacité: "),
    LEVEL_EN("Level: "), LEVEL_FR("Niveau: "),

    // Win message
    WIN_EN("You Won :)"), WIN_FR("Vous avez gagné :)"),

    // Ability names
    VITALITY_EN("VITALITY"), VITALITY_FR("VITALITÉ"),
    DAMAGER_EN("DAMAGER"), DAMAGER_FR("DÉGÂTS"),
    PROCREASE_EN("PROCREASE"), PROCREASE_FR("PROJECTILES"),
    AMOCREASE_EN("AMOCREASE"), AMOCREASE_FR("MUNITIONS"),
    SPEEDY_EN("SPEEDY"), SPEEDY_FR("VITESSE");

    private final String text;

    GameScreenText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static String getText(GameScreenText textType, boolean isEnglish) {
        if (isEnglish) {
            return textType.getText();
        } else {
            return valueOf(textType.name().replace("_EN", "_FR")).getText();
        }
    }
}