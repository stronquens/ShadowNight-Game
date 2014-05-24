package net.ausiasmarch.ShadowNight.modelo;

/**
 * ---------------------------------------------------------------------------
 * Curso Básico de desarrollo de VideoJuegos en Java 2D
 * Puntuaciones
 * @author Armando Maya y Jose M Coronado
 *--------------------------------------------------------------------------*/

public enum Score {
    Ghost(10), Speeder(20), Tree(30), Witch(40);
    
    public int value;
    
    Score(int value){
        this.value = value; 
    }
    
    public int getValue(){
        return value;
    }
}
