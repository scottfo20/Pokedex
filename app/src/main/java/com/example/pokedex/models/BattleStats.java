package com.example.pokedex.models;

public class BattleStats {
    private int attacks = 0;
    private int defenses = 0;
    private int escapes = 0;
    private int heals = 0;
    private int attackspecials = 0;

    public void incrementAttack() { attacks++; }
    public void incrementDefense() { defenses++; }
    public void incrementEscape() { escapes++; }
    public void incrementHeal() { heals++; }
    public void incrementSpecAttack() { attackspecials++; }


    public int getAttacks() { return attacks; }
    public int getDefenses() { return defenses; }
    public int getEscapes() { return escapes; }
    public int getHeals() { return heals; }

    public int getAttackspecials() { return attackspecials; }

    @Override
    public String toString() {
        return "Estadísticas del combate:\n" +
                "Ataques: " + attacks + "\n" +
                "Defensas: " + defenses + "\n" +
                "Intentos de escape: " + escapes + "\n" +
                "Curaciones: " + heals+ "\n" +
                "Ataques expeciales:" + attackspecials;
    }
}