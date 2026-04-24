package farm.controller;

import farm.model.Animal;

// Clase que muestra los animales. Se determinan una velocidad y posición aleatorias para cada animal
public class AnimalVisual {
    private Animal animal;
    private double posX;
    private double posY;
    private double velX;
    private double velY;

    public AnimalVisual(Animal animal) {
        this.animal = animal;
        this.posX = 50 + Math.random() * 400;
        this.posY = 50 + Math.random() * 300;
        this.velX = (Math.random() * 2) - 1;
        this.velY = (Math.random() * 2) - 1;
        if (this.velX == 0) this.velX = 1;
        if (this.velY == 0) this.velY = 1;
    }

    public Animal getAnimal() { return animal; }
    public double getPosX() { return posX; }
    public void setPosX(double posX) { this.posX = posX; }
    public double getPosY() { return posY; }
    public void setPosY(double posY) { this.posY = posY; }
    public double getVelX() { return velX; }
    public void setVelX(double velX) { this.velX = velX; }
    public double getVelY() { return velY; }
    public void setVelY(double velY) { this.velY = velY; }
}