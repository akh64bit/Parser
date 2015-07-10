/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geometry;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 *
 * @author Neha
 */
public class Point 
{
    private float x;
    private float y;
    public Point(float x, float y)
    {
        this.x = x;
        this.y = y;
    }
    
    public double getDistance(Point p)
    {
        return sqrt(pow(this.getX()-p.getX(),2) + pow(this.getY()-p.getY(),2));
    }

    /**
     * @return the x
     */
    public float getX() {
        return x;
    }

    /**
     * @return the y
     */
    public float getY() {
        return y;
    }

    /**
     * @param x the x to set
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * @param y the y to set
     */
    public void setY(float y) {
        this.y = y;
    }
}
