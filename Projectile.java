import java.awt.Polygon;


public class Projectile{

    private Track currentTrack;
    private Point3D[] path_points;
    private Point3D centerPoint;
    private int index;

    public Projectile(Track track_fired_on){
        index = 0;
        currentTrack = track_fired_on;
        path_points = track_fired_on.getMidpathPoints();
        centerPoint = path_points[0];

    }
    //returns 2D Polygon equivalent of the 3D projectile
    public Polygon getPolygon(double eyeDistance){
        //define the front two verticies of the currentTrack
        Point3D track_vertex0 = currentTrack.getVertex(0);
        Point3D track_vertex1 = currentTrack.getVertex(1);
        //use center point to define the center of the sphape
        int cubeSize = 3;

        Point3D front_up_left = new Point3D (centerPoint.x-cubeSize, centerPoint.y-cubeSize, centerPoint.z-cubeSize);
        Point3D front_down_left = new Point3D (centerPoint.x-cubeSize, centerPoint.y+cubeSize, centerPoint.z-cubeSize);
        Point3D front_down_right = new Point3D (centerPoint.x+cubeSize, centerPoint.y+cubeSize, centerPoint.z-cubeSize);
        Point3D front_up_right = new Point3D (centerPoint.x+cubeSize, centerPoint.y-cubeSize, centerPoint.z-cubeSize);

        Point3D back_up_left = new Point3D (front_up_left.x, front_up_left.y, centerPoint.z+cubeSize);
        Point3D back_down_left = new Point3D (front_down_left.x, front_down_left.y, centerPoint.z+cubeSize);
        Point3D back_down_right = new Point3D (front_down_right.x, front_down_right.y, centerPoint.z+cubeSize);
        Point3D back_up_right = new Point3D (front_up_right.x, front_up_right.y, centerPoint.z+cubeSize);

        //aranges verticies in the order they will be drawn
        Point3D[] cube_verticies = {front_up_left, front_down_left, front_down_right, front_up_right,
                                    front_up_left, back_up_left, back_up_right, front_up_right,
                                    front_down_right, back_down_right, back_up_right, back_down_right,
                                    back_down_left, back_up_left, back_down_left, front_down_left};

        int[] x_points = new int[16];
        int[] y_points = new int[16];
        //convert 3D points to 2D points
        for(int i=0;i<16;i++){
            if(cube_verticies[i].z <= 200){ //same fix as for the track positioning
                x_points[i] = (int) -cube_verticies[i].projectPoint3D(eyeDistance).getX();
                y_points[i] = (int) cube_verticies[i].projectPoint3D(eyeDistance).getY();
            }
            else{
                x_points[i] = (int) cube_verticies[i].projectPoint3D(eyeDistance).getX();
                y_points[i] = (int) cube_verticies[i].projectPoint3D(eyeDistance).getY();
            }
        }
        Polygon polygon = new Polygon(x_points, y_points, 16);
        return polygon;
    }

    //getters and setters:
    public Track getCurrentTrack(){ return currentTrack; }
    public Point3D getCenterPoint(){ return centerPoint; }
    public int getIndex(){ return index; }
    public void setCenterPoint(){ centerPoint = path_points[index]; }
    public void setIndex(int new_index){ index = new_index; }

}
