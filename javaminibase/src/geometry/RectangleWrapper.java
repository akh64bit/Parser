
package geometry;

import global.SdoGeom;
import java.awt.Rectangle;


public class RectangleWrapper// Not extending Rectangle... Instead of that creating a wrapper to keep it simple and not run into unexpected cases.
{
    private double area;
    private Point centroid;
    private final Rectangle rectangle;
    
    RectangleWrapper(SdoGeom geomObj)
    {   
        rectangle = new Rectangle((int)geomObj.getUpperLeft().getX(), (int)geomObj.getUpperLeft().getY(), geomObj.getWidth(), geomObj.getHeight());
        area = calArea();
        centroid = calCentroid();
    }
    
    RectangleWrapper(Rectangle rec)
    {   
        rectangle = rec;
        area = calArea();
        centroid = calCentroid();
    }
    
    
    
    private double calArea()
    {
        return getRectangle().height * getRectangle().width;
    }
    
    private Point calCentroid()
    {
        return new Point(getRectangle().x+((float)getRectangle().width/2), getRectangle().y-((float)getRectangle().height/2));
    }

    /**
     * @return the area
     */
    public double getArea() 
    {
        return area;
    }
    
    public boolean hasIntersection(RectangleWrapper r)
    {
        return this.rectangle.intersects(r.getRectangle());
    }

    /**
     * @return the centroid
     */
    public Point getCentroid() 
    {
        return centroid;
    }

    /**
     * @param area the area to set
     */
    public void setArea(double area) {
        this.area = area;
    }

    /**
     * @param centroid the centroid to set
     */
    public void setCentroid(Point centroid) {
        this.centroid = centroid;
    }
    
    public RectangleWrapper getIntersection(RectangleWrapper inputRectangleWrapper)
    {
        return new RectangleWrapper(getRectangle().intersection(inputRectangleWrapper.getRectangle()));
    }
    
    public double getDistance(RectangleWrapper inputRectangleWrapper)
    {
        return this.getCentroid().getDistance(inputRectangleWrapper.getCentroid());
    }

    /**
     * @return the rectangle
     */
    public Rectangle getRectangle() {
        return rectangle;
    }
    
    public static void main(String[] args)
    {
        SdoGeom s1=new SdoGeom(0,0,0,2,2,0,2,2);
        RectangleWrapper r1= new RectangleWrapper(s1);
        System.out.println("Area of rectangle : "+r1.getArea());
        SdoGeom s2=new SdoGeom(1,1,1,3,3,1,3,3);
        RectangleWrapper r2= new RectangleWrapper(s2);
        if(r1.hasIntersection(r2))
            System.out.println("Intersection of rectangles (): x-"+ r1.getIntersection(r2).getRectangle().x +", y-"+
                                                            r1.getIntersection(r2).getRectangle().y     +", width-"+
                                                            r1.getIntersection(r2).getRectangle().width +", height-"+
                                                            r1.getIntersection(r2).getRectangle().height);
        System.out.println("Distance between the rectangles : "+r1.getDistance(r2));
        
    }
}
