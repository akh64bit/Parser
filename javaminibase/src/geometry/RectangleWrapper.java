
package geometry;

import global.SdoGeom;
import java.awt.Rectangle;


public class RectangleWrapper// Not extending Rectangle... Instead of that creating a wrapper to keep it simple and not run into unexpected cases.
{
    

    private final SdoGeom sdoGeom;
    private final Rectangle rectangle;
    private double area;
    private Point centroid;

    
    RectangleWrapper(SdoGeom geomObj)
    {   
        sdoGeom = geomObj;
        rectangle = new Rectangle((int)geomObj.getUpperLeft().getX(), (int)geomObj.getUpperLeft().getY(), geomObj.getWidth(), geomObj.getHeight());
        area = calArea();
        centroid = calCentroid();
        
    }
    
    RectangleWrapper(Rectangle rec)
    {   
        sdoGeom = getGeomObject(rec);
        rectangle = rec;
        area = calArea();
        centroid = calCentroid();
    }
    
    SdoGeom getGeomObject(Rectangle rec)
    {
        int x1=rec.x;
        int y1=rec.y;
        int x2=x1+rec.width;
        int y2=y1-rec.height;
        int x3=x1;
        int y3=y2;
        int x4=x2;
        int y4=y1;
        return new SdoGeom(x1,y1,x2,y2,x3,y3,x4,y4); 
        
    }
    
    private double calArea()
    {
        return getRectangle().height * getRectangle().width;
    }
    
    private Point calCentroid()
    {
        return new Point((float)getRectangle().getCenterX(), (float)getRectangle().getCenterY());
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
    
    public void print()
    {
        System.out.println("Rectangle values: x-"+ getRectangle().x +", y-"+
                                                            getRectangle().y     +", width-"+
                                                            getRectangle().width +", height-"+
                                                            getRectangle().height +",centroid-("+centroid.getX()+","+centroid.getY()+")");
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
        r1.print();
        SdoGeom s2=new SdoGeom(1,1,3,4,1,4,3,1);
        RectangleWrapper r2= new RectangleWrapper(s2);
        r2.print();
        System.out.println("Area of rectangle r1: "+r1.getArea());
        
        if(r1.hasIntersection(r2))
            System.out.println("Intersection of rectangles (): x-"+ r1.getIntersection(r2).getRectangle().x +", y-"+
                                                            r1.getIntersection(r2).getRectangle().y     +", width-"+
                                                            r1.getIntersection(r2).getRectangle().width +", height-"+
                                                            r1.getIntersection(r2).getRectangle().height);
        System.out.println("Distance between the rectangles : "+r1.getDistance(r2));
        
    }
}
