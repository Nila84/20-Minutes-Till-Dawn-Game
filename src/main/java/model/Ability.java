package model;

import controller.App;
import java.util.*;

public class Ability {
    private final String name;
    private final String description;
    private final int duration;
    private final AbilityType type;
    private final double value;
    private boolean isActive;
    private long activationTime;
    private int originalDamage;
    private double originalSpeed;

    public enum AbilityType {
        VITALITY,
        DAMAGER,
        PROCREASE,
        AMOCREASE,
        SPEEDY
    }

    public static final Map<AbilityType, Ability> DEFAULT_ABILITIES = new HashMap<>();
    static {
        DEFAULT_ABILITIES.put(AbilityType.VITALITY,
                new Ability
                        ("VITALITY", "افزایش ماکسیمم HP به اندازه یک واحد", 0, 1.0));
        DEFAULT_ABILITIES.put(AbilityType.DAMAGER,
                new Ability
                        ("DAMAGER", "افزایش 25 درصدی میزان دمپج سلاح به مدت 10 ثانیه",
                                10, 0.25));
        DEFAULT_ABILITIES.put(AbilityType.PROCREASE,
                new Ability
                        ("PROCREASE", "افزایش یک واحدی Projectile سلاح", 0, 1.0));
        DEFAULT_ABILITIES.put(AbilityType.AMOCREASE,
                new Ability
                        ("AMOCREASE", "افزایش 5 واحدی حداکثر تعداد تیرهای سلاح", 0, 5.0));
        DEFAULT_ABILITIES.put(AbilityType.SPEEDY,
                new Ability
                        ("SPEEDY", "دو برابر شدن سرعت حرکت بازیکن به مدت 10 ثانیه",
                                10, 2.0));
    }

    public Ability(String name, String description, int duration, double value) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.value = value;
        this.type = AbilityType.valueOf(name);
        this.isActive = false;
    }
    public Ability(String name) {
        this.name = name;
        this.isActive = false;
        switch (name){
            case "VITALITY" :
                this.description = "افزایش ماکسیمم HP به اندازه یک واحد";
                this.duration = 0;
                this.value = 1.0;
                this.type = AbilityType.valueOf(name);
                break;
            case "DAMAGER" :
                this.description = "افزایش 25 درصدی میزان دمپج سلاح به مدت 10 ثانیه";
                this.duration = 10;
                this.value = 0.25;
                this.type = AbilityType.valueOf(name);
                break;
            case "PROCREASE" :
                this.description = "افزایش یک واحدی Projectile سلاح";
                this.duration = 0;
                this.value = 1.0;
                this.type = AbilityType.valueOf(name);
                break;
            case "AMOCREASE" :
                this.description = "افزایش 5 واحدی حداکثر تعداد تیرهای سلاح";
                this.duration = 0;
                this.value = 5.0;
                this.type = AbilityType.valueOf(name);
                break;
            case "SPEEDY" :
                this.description = "دو برابر شدن سرعت حرکت بازیکن به مدت 10 ثانیه";
                this.duration = 10;
                this.value = 2.0;
                this.type = AbilityType.valueOf(name);
                break;
            default:
                throw new IllegalArgumentException("Unknown ability: " + name);
        }
    }

    public void activate() {
        this.isActive = true;
        this.activationTime = System.currentTimeMillis();

        applyImmediateEffects();
    }

    public void update() {
        if (isActive && duration > 0) {
            long elapsed = (System.currentTimeMillis() - activationTime) / 1000;
            if (elapsed >= duration) {
                deactivate();
            }
        }
    }

    public void deactivate() {
        if (isActive) {
            this.isActive = false;
            removeTemporaryEffects();
        }
    }

    private void applyImmediateEffects() {
        switch(type) {
            case VITALITY:
                App.getCurrentUser().getSelectedHero().setHp(1);
                break;
            case DAMAGER:
                originalDamage = App.getCurrentUser().getSelectedWeapon().getDamagePerProjectile();
                int increasedDamage = (int) (originalDamage * (1 + value));
                App.getCurrentUser().getSelectedWeapon().setDamagePerProjectile(increasedDamage);
                break;
            case PROCREASE:
                App.getCurrentUser().getSelectedWeapon().setProjectileCount(1);
                break;
            case AMOCREASE:
                App.getCurrentUser().getSelectedWeapon().setMaxAmmo(5);
                break;
            case SPEEDY:
                originalSpeed = App.getCurrentUser().getSelectedHero().getSpeed();
                double increasedSpeed = originalSpeed * value;
                App.getCurrentUser().getSelectedHero().setSpeed((int) increasedSpeed);
                break;
        }
    }

    private void removeTemporaryEffects() {
        switch(type) {
            case DAMAGER:
                App.getCurrentUser().getSelectedWeapon().setDamagePerProjectile(originalDamage);
                break;

            case SPEEDY:
                App.getCurrentUser().getSelectedHero().setSpeed((int) originalSpeed);
                break;

            case VITALITY:
            case PROCREASE:
            case AMOCREASE:
                break;
        }
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getDuration() { return duration; }

    public static Ability getDefaultAbility(AbilityType type) {
        return DEFAULT_ABILITIES.get(type);
    }

    public static List<Ability> getThreeUniqueRandomAbilities() {
        List<Ability> uniqueAbilities = new ArrayList<>();
        List<AbilityType> availableTypes = new ArrayList<>(Arrays.asList(AbilityType.values()));

        while (uniqueAbilities.size() < 3 && !availableTypes.isEmpty()) {
            int randomIndex = (int) (Math.random() * availableTypes.size());
            AbilityType selectedType = availableTypes.get(randomIndex);
            uniqueAbilities.add(DEFAULT_ABILITIES.get(selectedType));
            availableTypes.remove(randomIndex);
        }
        return uniqueAbilities;
    }
}