import java.lang.Math;
import java.awt.Polygon;
import java.awt.geom.Point2D;


public class Track{

    //tracks are rectangles 300 units long, 40 units in width
    //these initial verticies create the first track that will appear on all levels
    private Point3D[] first_track_verticies = {new Point3D(-20,125,50), new Point3D(20,125,50), new Point3D(-20,125,350), new Point3D(20,125,350)};

    public final int NUM_MIDPOINTS = 50;
    private Point3D[] midpath_points = new Point3D[NUM_MIDPOINTS];

    private Point3D[] verticies = {first_track_verticies[0], first_track_verticies[1], first_track_verticies[2], first_track_verticies[3]};

    private double x_axis_angle;
    public Track neighboring_track;

    //constructor for 3D Track takes two verticies from a neighboring track, the front edge's angle with the x axis, and a boolean determining if the new track is to the left or right of its neighbor
    public Track(Point3D _front_vertex, Point3D _back_vertex, double _x_axis_angle, Boolean isLeftNeighbor){
        x_axis_angle = _x_axis_angle;

        if(_front_vertex.z == 1 && _back_vertex.z == 1){
            //check impossible cercumstances that are only set for the initial track
        }
        else{
            if(isLeftNeighbor){ //this track is left of its nieghboring track, set verticies accordingly
                verticies[1] = _front_vertex;
                verticies[2] = _back_vertex;
                //calculate the other two verticies using the angle with the x_axis_angle
                verticies[0] = findVertex(verticies[1]);
                verticies[3] = findVertex(verticies[2]);

            }
            else{ //this track is right of its nieghboring track, set verticies accordingly
                verticies[0] = _front_vertex;
                verticies[3] = _back_vertex;
                //calculate the other two verticies using the angle with the x_axis_angle
                verticies[1] = findVertex(verticies[0]);
                verticies[2] = findVertex(verticies[3]);
            }
        }
        //fills the midpath_points array with 3D points along the path connecting the front and back edge's midpoints
        setMidpathPoints();
        //asserts that the first vertex is in the right position, by extension testing the other verticies
        Boolean correct = verticies[0].x < verticies[1].x && verticies[0].x < verticies[2].x && verticies[0].z < verticies[2].z && verticies[0].z < verticies[3].z;
        assert correct;

    }

    public void setMidpathPoints(){

        double back_mid_x;
        double back_mid_y;
        double back_mid_z = verticies[3].z; //z value is always the same as the back verticies
        //finds back midpoint x value:
        if(verticies[3].x>verticies[2].x){back_mid_x = verticies[2].x+((verticies[3].x-verticies[2].x)/2);}
        else{back_mid_x = verticies[3].x+((verticies[2].x-verticies[3].x)/2);}
        //finds back midpoint y value:
        if(verticies[3].y>verticies[2].y){back_mid_y = verticies[2].y+((verticies[3].y-verticies[2].y)/2);}
        else{back_mid_y = verticies[3].y+((verticies[2].y-verticies[3].y)/2);}

        double front_mid_z = verticies[0].z; //z value is always the same as the front verticies

        double z_offset = (back_mid_z - front_mid_z)/NUM_MIDPOINTS;

        for(int i=1;i < 51;i++){
            Point3D point_on_midpath = new Point3D(back_mid_x,back_mid_y,back_mid_z-(i*z_offset));
            midpath_points[50-i] = point_on_midpath;
        }

    }

    //uses given angle and given Point3D,
    //returns the calculated Point3D needed to define the track's other two verticies.
    public Point3D findVertex(Point3D given_vertex){
        double x = given_vertex.x;
        double y = given_vertex.y;
        double z = given_vertex.z;

        //uses law of sins to determine desired point3D
        //this is doable because the z coord of the desired Point3D is the same as that of the known one's
        double other_angle = 180 - (90+x_axis_angle);
        double ajacent_side_length = 40 * Math.sin(Math.toRadians(other_angle));
        double opposite_side_length = 40 * Math.sin(Math.toRadians(x_axis_angle));
        //add/subtract calculated lengths to the given coordnates to find the desired point
        if(x < 0){ x -= ajacent_side_length; }
        else{ x += ajacent_side_length; }
        y -= opposite_side_length;

        //create and return calculated Point3D

        Point3D vertex = new Point3D(x,y,z);
        return vertex;
    }

    //returns 2D polygon equivalent to the 3D track
    public Polygon getPolygon(double eyeDistance){
        int[] x_points = new int[4];
        int[] y_points = new int[4];
        //convert 3D points to 2D points
        for(int i=0;i<4;i++){
        x_points[i] = (int) verticies[i].projectPoint3D(eyeDistance).getX();
        y_points[i] = (int) verticies[i].projectPoint3D(eyeDistance).getY();
        }
        Polygon polygon = new Polygon(x_points, y_points, 4);
        return polygon;
    }

    //this method checks if two tracks are identical
    public Boolean isEqualTo(Track other_track){
        if(other_track.getVertex(0).x == this.getVertex(0).x
        && other_track.getVertex(1).x == this.getVertex(1).x
        && other_track.getVertex(2).x == this.getVertex(2).x
        && other_track.getVertex(3).x == this.getVertex(3).x
        && other_track.getVertex(0).y == this.getVertex(0).y
        && other_track.getVertex(1).y == this.getVertex(1).y
        && other_track.getVertex(2).y == this.getVertex(2).y
        && other_track.getVertex(3).y == this.getVertex(3).y
        && other_track.getVertex(0).z == this.getVertex(0).z
        && other_track.getVertex(1).z == this.getVertex(1).z
        && other_track.getVertex(2).z == this.getVertex(2).z
        && other_track.getVertex(3).z == this.getVertex(3).z){
            return true;
        }
        else{ return false; }
    }

    //get and set vertex methods:
    public Point3D[] getWallMidpathPoints(){
        int size = midpath_points.length;
        Point3D[] reverse_points = new Point3D[size];
        //reverses points so walls come from the other direction
        for(int i=1;i<midpath_points.length+1;i++){
            reverse_points[midpath_points.length-i] = midpath_points[i-1];
        }
        return reverse_points;
    }
    public Point3D[] getMidpathPoints(){ return midpath_points; }
    public Point3D getMidpathPoint(int index){ return midpath_points[index]; }
    public Point3D getVertex(int index){ return verticies[index]; }
    public double getAngle(){ return x_axis_angle; }
    public void setVertex(int index, Point3D new_vertex){ verticies[index] = new_vertex; }

    public String toString(){
        String result = "";
        for(int i=0;i<4;i++){
            result +=  "\n Vertex "+(i+1)+": ("+verticies[i].x+", "+verticies[i].y+", "+verticies[i].z+")";
        }
        return result;
    }

}
