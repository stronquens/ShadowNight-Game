package net.ausiasmarch.ShadowNight.modelo;

/**---------------------------------------------------------------------------
 * Curso Básico de desarrollo de VideoJuegos en Java 2D
 * Direccion de disparo
 * @author Armando Maya y Jose M Coronado
 *--------------------------------------------------------------------------*/

public enum ShotDirection {
    UL(0), U(1), UR(2), L(3),R(4), DL(5), D(6), DR(7);
    
    public int value;
    
    ShotDirection(int value){
        this.value = value; 
    }
    
    public int getValue(){
        return value;
    }
}