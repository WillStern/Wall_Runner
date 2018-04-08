import java.awt.Polygon;
import java.awt.Color;


public class Wall{

    private Track currentTrack;
    private Point3D[] path_points;
    private Point3D centerPoint;
    private int index;
    private final int WALL_DEPTH = 3;
    private int wall_health;
    private Color wall_color;

    public Wall(Track track_wall_is_on, int _wall_health){
        index = 0;
        currentTrack = track_wall_is_on;
        path_points = track_wall_is_on.getWallMidpathPoints();
        centerPoint = path_points[0];

        wall_health = _wall_health;

        switch(wall_health){
            case 1:
            wall_color = Color.cyan;
            break;
            case 2:
            wall_color = Color.blue;
            break;
            case 3:
            wall_color = Color.magenta;
            break;
            case 4:
            wall_color = Color.pink;
            break;
            case 5:
            wall_color = Color.red;
            break;
        }
    }

    //returns 2D Polygon equivalent of the 3D projectile
    public Polygon getPolygon(double eyeDistance){

        //define the front two verticies of the currentTrack
        Point3D track_vertex0 = currentTrack.getVertex(0);
        Point3D track_vertex1 = currentTrack.getVertex(1);
        //points for front face
        Point3D left_base_vertex_1 = new Point3D(track_vertex0.x, track_vertex0.y, centerPoint.z-WALL_DEPTH);
        Point3D right_base_vertex_1 = new Point3D(track_vertex1.x, track_vertex1.y, centerPoint.z-WALL_DEPTH);
        Point3D left_top_vertex_1 = findVertex(left_base_vertex_1);
        Point3D right_top_vertex_1 = findVertex(right_base_vertex_1);
        // for back face, same points as before just different z based on wall depth
        Point3D left_base_vertex_2 = new Point3D(left_base_vertex_1.x, left_base_vertex_1.y, left_base_vertex_1.z+WALL_DEPTH);
        Point3D right_base_vertex_2 = new Point3D(right_base_vertex_1.x, right_base_vertex_1.y, right_base_vertex_1.z+WALL_DEPTH);
        Point3D left_top_vertex_2 = new Point3D(left_top_vertex_1.x, left_top_vertex_1.y, left_top_vertex_1.z+(2*WALL_DEPTH));
        Point3D right_top_vertex_2 = new Point3D(right_top_vertex_1.x, right_top_vertex_1.y, right_top_vertex_1.z+(2*WALL_DEPTH));

        //aranges verticies in the order they will be drawn
        Point3D[] wall_verticies = {left_top_vertex_1, left_base_vertex_1, right_base_vertex_1, right_top_vertex_1,
                                    left_top_vertex_1, left_top_vertex_2, right_top_vertex_2, right_top_vertex_1,
                                    right_base_vertex_1, right_base_vertex_2, right_top_vertex_2, right_base_vertex_2,
                                    left_base_vertex_2, left_top_vertex_2, left_base_vertex_2, left_base_vertex_1};

        int[] x_points = new int[16];
        int[] y_points = new int[16];
        //convert 3D points to 2D points
        for(int i=0;i<16;i++){
            if(centerPoint.z <= 50){ //if at end of track dont draw
                x_points[i] = (int) track_vertex0.projectPoint3D(eyeDistance).getX();
                y_points[i] = (int) track_vertex1.projectPoint3D(eyeDistance).getY();
            }
            else{
                if(wall_verticies[i].z <= 200){ //same fix as for the track positioning
                    x_points[i] = (int) -wall_verticies[i].projectPoint3D(eyeDistance).getX();
                    y_points[i] = (int) wall_verticies[i].projectPoint3D(eyeDistance).getY();
                }
                else{
                    x_points[i] = (int) wall_verticies[i].projectPoint3D(eyeDistance).getX();
                    y_points[i] = (int) wall_verticies[i].projectPoint3D(eyeDistance).getY();
                }
            }
        }
        Polygon polygon = new Polygon(x_points, y_points, 16);
        return polygon;

    }


    public Point3D findVertex(Point3D given_vertex){
        double x = given_vertex.x;
        double y = given_vertex.y;
        double z = given_vertex.z - WALL_DEPTH;

        //uses law of sins to determine desired point3D
        //this is doable because the z coord of the desired Point3D is the same as that of the known one's
        double angle = currentTrack.getAngle()+90;//straight out from track
        double other_angle = 180 - (90+angle);
        double ajacent_side_length = 40 * Math.sin(Math.toRadians(other_angle));
        double opposite_side_length = 40 * Math.sin(Math.toRadians(angle));
        //add/subtract calculated lengths to the given coordnates to find the desired point
        if(x < 0){ x -= ajacent_side_length; }
        else{ x += ajacent_side_length; }
        y -= opposite_side_length;

        //create and return calculated Point3D

        Point3D vertex = new Point3D(x,y,z);
        return vertex;
    }

    //getters and setters:
    public Track getCurrentTrack(){ return currentTrack; }
    public Point3D getCenterPoint(){ return centerPoint; }
    public int getIndex(){ return index; }
    public Color getColor(){ return wall_color; }
    public int getHealth(){ return wall_health; }
    public void setCenterPoint(){ centerPoint = path_points[index]; }
    public void setIndex(int new_index){ index = new_index; }
    public void setHealth(int new_health){
        wall_health = new_health;
        switch(wall_health){
            case 1:
            wall_color = Color.cyan;
            break;
            case 2:
            wall_color = Color.blue;
            break;
            case 3:
            wall_color = Color.magenta;
            break;
            case 4:
            wall_color = Color.pink;
            break;
            case 5:
            wall_color = Color.red;
            break;
        }
     }

}
