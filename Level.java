import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Random;

public class Level{

    public ArrayList<Track> tracks;
    private ArrayList<Wall> walls;


    public Level(int level_number){
        tracks = new ArrayList<Track>();
        walls = new ArrayList<Wall>();
        //set initial Track:
        tracks.add(new Track(new Point3D(0,0,1), new Point3D(0,0,1), 0.0, false));
        switch(level_number){
            case 1:
            setLevelOne();
            break;
            case 2:
            setLevelTwo();
            break;
            case 3:
            setLevelThree();
            break;
        }
    }

    //contains wall and track information for level 1:
    public void setLevelOne(){
        int num_tracks = 16;
        int num_walls = 330;
        double theta = 15;
        for(int i=0; i<num_tracks;i++){
            if(i<8){//half on left of initial
                tracks.add(new Track(tracks.get(i).getVertex(0), tracks.get(i).getVertex(3), theta, true));
                theta += 15;
            }
            else if(i==8){
                theta = 15;
                tracks.add(new Track(tracks.get(i-8).getVertex(1), tracks.get(i-8).getVertex(2), theta, false));
                theta += 15;
            }
            else{
                tracks.add(new Track(tracks.get(i).getVertex(1), tracks.get(i).getVertex(2), theta, false));
                theta += 15;
            }
        }
        // layout: 7,6,5,4,3,2,1,0,8,9,10,11,12,13,14,15
        //this loop adds this level's walls
        int wall_health=0;
        for(int j=1; j<10; j++){//makes 330 tracks3 times
            wall_health=1;

            walls.add(new Wall(tracks.get(8), wall_health));
            walls.add(new Wall(tracks.get(7), wall_health));
            walls.add(new Wall(tracks.get(6), wall_health));
            walls.add(new Wall(tracks.get(5), wall_health));
            walls.add(new Wall(tracks.get(4), wall_health));
            walls.add(new Wall(tracks.get(3), wall_health));
            walls.add(new Wall(tracks.get(2), wall_health));
            walls.add(new Wall(tracks.get(1), wall_health));
            walls.add(new Wall(tracks.get(0), wall_health));
            walls.add(new Wall(tracks.get(9), wall_health));
            walls.add(new Wall(tracks.get(10), wall_health));
            walls.add(new Wall(tracks.get(11), wall_health));
            walls.add(new Wall(tracks.get(12), wall_health));
            walls.add(new Wall(tracks.get(13), wall_health));
            walls.add(new Wall(tracks.get(14), wall_health));
            walls.add(new Wall(tracks.get(15), wall_health));
            walls.add(new Wall(tracks.get(16), wall_health));
            walls.add(new Wall(tracks.get(15), wall_health));
            walls.add(new Wall(tracks.get(14), wall_health));
            walls.add(new Wall(tracks.get(13), wall_health));
            walls.add(new Wall(tracks.get(12), wall_health));
            walls.add(new Wall(tracks.get(11), wall_health));
            walls.add(new Wall(tracks.get(10), wall_health));
            walls.add(new Wall(tracks.get(9), wall_health));
            walls.add(new Wall(tracks.get(0), wall_health));
            walls.add(new Wall(tracks.get(1), wall_health));
            walls.add(new Wall(tracks.get(2), wall_health));
            walls.add(new Wall(tracks.get(3), wall_health));
            walls.add(new Wall(tracks.get(4), wall_health));
            walls.add(new Wall(tracks.get(5), wall_health));
            walls.add(new Wall(tracks.get(6), wall_health));
            walls.add(new Wall(tracks.get(7), wall_health));

        }

    }

    //contains wall and track information for level 2:
    public void setLevelTwo(){
        int num_tracks = 16;
        int num_walls = 330;
        double theta = 90;
        for(int i=0; i<8;i++){//initial 6 tracks
            if(i == 5){//half on left of initial
                theta = 90;
                tracks.add(new Track(tracks.get(0).getVertex(0), tracks.get(i).getVertex(3), theta, true));
            }
            else if(i > 5){//half on left of initial
                if(i%2==0){theta = 0;}
                else{theta = 90;}
                Point3D fixed_point = new Point3D(-tracks.get(i).getVertex(3).x, tracks.get(i).getVertex(3).y, tracks.get(i).getVertex(3).z);
                tracks.add(new Track(tracks.get(i).getVertex(0), fixed_point, theta, true));
            }
            else{
                if(i%2==0){theta = 90;}
                else{theta = 0;}
                tracks.add(new Track(tracks.get(i).getVertex(1), tracks.get(i).getVertex(2), theta, false));
            }
        }
        // layout: 4,3,2,1,0,5,6,7,8
        //this loop adds this level's walls
        int wall_health=0;
        for(int j=1; j<6; j++){//makes 330 walls

            wall_health=j;
            walls.add(new Wall(tracks.get(0), wall_health));
            walls.add(new Wall(tracks.get(1), wall_health));
            walls.add(new Wall(tracks.get(5), wall_health));
            walls.add(new Wall(tracks.get(2), wall_health));
            walls.add(new Wall(tracks.get(6), wall_health));
            walls.add(new Wall(tracks.get(3), wall_health));
            walls.add(new Wall(tracks.get(7), wall_health));
            walls.add(new Wall(tracks.get(4), wall_health));
            walls.add(new Wall(tracks.get(8), wall_health));


        }

    }

    //contains wall and track information for level 3:
    public void setLevelThree(){
        int num_tracks = 14;
        int num_walls = 1000;
        double theta = 40;
        for(int i=0; i<num_tracks;i++){
            if(i<7){//half on left of initial
                tracks.add(new Track(tracks.get(i).getVertex(0), tracks.get(i).getVertex(3), theta, true));
            }
            else if(i==7){
                tracks.add(new Track(tracks.get(0).getVertex(1), tracks.get(0).getVertex(2), theta, false));
            }
            else{
                tracks.add(new Track(tracks.get(i).getVertex(1), tracks.get(i).getVertex(2), theta, false));
            }
        }

        // layout: 7,6,5,4,3,2,1,0,8,9,10,11,12,13,14,15
        //this loop adds this level's walls
        int wall_health=0;
        int cur_track =0;
        Random r = new Random();
        for(int j=1; j<1000; j++){
            wall_health = 1+r.nextInt(4);
            cur_track = r.nextInt(14);
            walls.add(new Wall(tracks.get(cur_track), wall_health));
        }

    }

    public Wall getWall(int index){return walls.get(index);}
    public ArrayList<Wall> getWalls(){return walls;}

    public ArrayList<Polygon> getPolygons(double eyeDistance){
        //after level set add shapes
        ArrayList <Polygon> track_shapes = new ArrayList<>();
        for(Track t: tracks){
            track_shapes.add(t.getPolygon(eyeDistance));
        }
        return track_shapes;
    }
}
