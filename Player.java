import java.lang.Math;
import java.awt.Polygon;
import java.awt.Color;


public class Player{

    private Track currentTrack;
    Level currentLevel;
    private int health;
    private Color color;

    public Player(Level _currentLevel){
        currentLevel = _currentLevel;
        //put player on initial track for level
        currentTrack = currentLevel.tracks.get(0);
        health = 50;
        color = new Color(37,249,6);
    }

    //find player in 3D space based on current Track
    public Polygon getPolygon(double eyeDistance){
        //used to reduce clutter
        Point3D track_vertex0 = currentTrack.getVertex(0);
        Point3D track_vertex1 = currentTrack.getVertex(1);
        //used to store x,y coords of the midpoint between the current tracks front two verticies
        double midpoint_x;
        double midpoint_y;
        //calculates x coord based on which front vertex has a bigger x value
        if(track_vertex1.x>track_vertex0.x){midpoint_x = track_vertex0.x+((track_vertex1.x-track_vertex0.x)/2);}
        else{midpoint_x = track_vertex1.x + ((track_vertex0.x-track_vertex1.x)/2);}
        //calculates y coord based on which front vertex has a bigger y value
        if(track_vertex1.y>track_vertex0.y){midpoint_y = track_vertex0.y + ((track_vertex1.y-track_vertex0.y)/2);}
        else{midpoint_y = track_vertex1.y + ((track_vertex0.y-track_vertex1.y)/2);}


        Point3D left_vertex = new Point3D(track_vertex0.x, track_vertex0.y, track_vertex0.z);
        Point3D midpoint_vertex = new Point3D(midpoint_x, midpoint_y, track_vertex1.z-4);
        Point3D right_vertex = new Point3D(track_vertex1.x, track_vertex1.y, track_vertex1.z);

        Point3D second_midpoint = new Point3D(midpoint_vertex.x, midpoint_vertex.y, midpoint_vertex.z + 2);

        double anchor_offset_x;
        double anchor_offset_y;

        if(track_vertex1.x>track_vertex0.x){anchor_offset_x = (track_vertex1.x-track_vertex0.x)/10;}
        else{anchor_offset_x = -(track_vertex0.x-track_vertex1.x)/10;}
        if(track_vertex1.y>track_vertex0.y){anchor_offset_y = (track_vertex1.y-track_vertex0.y)/10;}
        else{anchor_offset_y = -(track_vertex0.y-track_vertex1.y)/10;}

        Point3D left_anchor = new Point3D(track_vertex0.x+anchor_offset_x, track_vertex0.y+anchor_offset_y, track_vertex0.z);
        Point3D right_anchor = new Point3D(track_vertex1.x-anchor_offset_x, track_vertex1.y-anchor_offset_y, track_vertex0.z);

        Point3D left_point = new Point3D(track_vertex0.x+(anchor_offset_x*3), track_vertex0.y+(anchor_offset_y*3), track_vertex0.z+3);
        Point3D right_point = new Point3D(track_vertex1.x-(anchor_offset_x*3), track_vertex1.y-(anchor_offset_y*3), track_vertex0.z+3);


        //added left_point right_point left_anchor right_anchor
        Point3D[] verticies = {left_point, left_vertex, midpoint_vertex, right_vertex, right_point, right_anchor, second_midpoint, left_anchor, left_point};

        int[] x_points = new int[9];
        int[] y_points = new int[9];
        //convert 3D points to 2D points
        for(int i=0;i<9;i++){
        x_points[i] = (int) verticies[i].projectPoint3D(eyeDistance).getX();
        y_points[i] = (int) verticies[i].projectPoint3D(eyeDistance).getY();
        }
        Polygon polygon = new Polygon(x_points, y_points, 9);
        return polygon;

    }

    public void moveCurrentTrack(String left_or_right){
        Track temp;
        if(left_or_right == "left"){
            temp = currentTrack;
            for(Track t:currentLevel.tracks){
                if(t.getVertex(1).x == temp.getVertex(0).x
                && t.getVertex(1).y == temp.getVertex(0).y
                && t.getVertex(1).z == temp.getVertex(0).z
                && t.getVertex(2).x == temp.getVertex(3).x
                && t.getVertex(2).y == temp.getVertex(3).y
                && t.getVertex(2).z == temp.getVertex(3).z){
                    System.out.println("T Found: "+ currentTrack);
                    currentTrack = t;
                }
            }
        }
        else if(left_or_right == "right"){
            temp = currentTrack;
            for(Track t:currentLevel.tracks){
                if(t.getVertex(0).x == temp.getVertex(1).x
                && t.getVertex(0).y == temp.getVertex(1).y
                && t.getVertex(0).z == temp.getVertex(1).z
                && t.getVertex(3).x == temp.getVertex(2).x
                && t.getVertex(3).y == temp.getVertex(2).y
                && t.getVertex(3).z == temp.getVertex(2).z){
                    System.out.println("T Found: "+ currentTrack);
                    currentTrack = t;
                }
            }
        }
    }

    public Track getCurrentTrack(){ return currentTrack; }
    public Color getColor(){ return color; }
    public int getHealth(){ return health; }
    public void setHealth(int new_health){
        health = new_health;
        switch(health){
            case 50:
            color = new Color(37,249,6);
            break;
            case 45:
            color = new Color(145,250,0);
            break;
            case 40:
            color = new Color(179,250,0);
            break;
            case 35:
            color = new Color(212,243,0);
            break;
            case 30:
            color = new Color(233,233,0);
            break;
            case 25:
            color = new Color(239,206,1);
            break;
            case 20:
            color = new Color(246,183,0);
            break;
            case 15:
            color = new Color(247,120,3);
            break;
            case 10:
            color = new Color(249,74,1);
            break;
            case 5:
            color = new Color(252,6,5);
            break;
        }
    }



}
