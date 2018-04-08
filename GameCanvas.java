import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.awt.geom.Area;
import java.awt.geom.*;
import java.lang.Math;
import java.util.*;
import javax.swing.Timer;

public class GameCanvas extends JPanel implements KeyListener, ActionListener {

    private ArrayList<Level> levels;
    private ArrayList<Wall> current_level_walls;
    private Wall final_wall;
    private ArrayList<Polygon> current_level_wall_shapes;
    private ArrayList<Polygon> wall_shapes;
    ArrayList<Polygon> polygons;
    ArrayList<Projectile> projectiles;
    ArrayList<Polygon> projectile_shapes;
    public int currentLevel;
    private Level active_level;
    Player player;
    private Boolean level_completed;
    private int timer = 0;
    private int score = 0;
    private Boolean game_over;

    private Font score_font;
    private Font game_over_font;


    //private Level saved_level;
    //private Player saved_playerState;
    //private ArrayList<Enemy> saved_enemyStates;
    double eyeDistance = 100;
    //constructor
    public GameCanvas(){
        level_completed = false;
        game_over = false;
        currentLevel = 1;
        score_font = new Font("CENTER_BASELINE", Font.BOLD, 60);
        game_over_font = new Font("SansSerif", Font.ITALIC, 32);
        projectile_shapes = new ArrayList<Polygon>();
        projectiles = new ArrayList<Projectile>();
        levels = new ArrayList<Level>();
        wall_shapes = new ArrayList<Polygon>();
        current_level_wall_shapes = new ArrayList<Polygon>();
        levels.add(new Level(1));
        //levels.add(new Level(2));
        active_level = levels.get(0);//active level set to start at one
        player = new Player(levels.get(currentLevel-1));
        current_level_walls = active_level.getWalls();
        final_wall = current_level_walls.get(current_level_walls.size()-1);
        for(Wall w: current_level_walls){ wall_shapes.add(w.getPolygon(eyeDistance)); }
        setPreferredSize(new Dimension(1200,1000));
        setBackground(Color.black);
        setFocusable(true);
        //requestFocus();
        addKeyListener(this);
        int speed = 60; //20th of a second delay between timer firings
        int pause = 1000; //pause before level begins - 1 sec
        Timer timer = new Timer(speed, this);
        timer.setInitialDelay(pause);
        timer.start();
    }

    public void paintComponent(Graphics g){

        Graphics2D g2d = (Graphics2D)g; //cast in order to use JAVA2D.
        super.paintComponent(g);  //without this no background color set.
        g2d.translate(getWidth()/2,getHeight()/2); // translate origin to center

        if(player.getHealth()<=0){
            g2d.setPaint(Color.black);
            g2d.fillRect(-getWidth()/4, -getHeight()/4, getWidth()/2, getHeight()/2);
            g2d.setPaint(Color.cyan);
            g2d.drawRect(-getWidth()/4, -getHeight()/4, getWidth()/2, getHeight()/2);
            g2d.setPaint(Color.white);
            g2d.setFont(game_over_font);
            g2d.drawString("Game Over", -100, -40);
            g2d.drawString(Integer.toString(score), -70, 0);

        }
        else{
            if(current_level_walls.get(current_level_walls.size()-1).getCenterPoint().z <=70) {//final wall has been deleted
                System.out.println("LEVEL COMPLETE");
                level_completed = true;

            }
            //fills polygon array with polygons made from the tracks of the current level
            polygons = levels.get(currentLevel-1).getPolygons(eyeDistance);
            //for testing Track class:
            Polygon player_shape = player.getPolygon(eyeDistance);

            Polygon player_track = new Polygon();
            for(Polygon p: polygons){
                if(Arrays.equals(player.getCurrentTrack().getPolygon(eyeDistance).xpoints, p.xpoints)
                && Arrays.equals(player.getCurrentTrack().getPolygon(eyeDistance).ypoints, p.ypoints)){
                    //if the current track is the one the player is on, save it to draw last
                    player_track = p;
                }
                else{
                    g2d.setPaint(Color.cyan);
                    g2d.draw(p);
                }
            }
            //after all other tracks have been drawn, draw the player and the track the player is on in yellow
            g2d.setPaint(Color.cyan);
            g2d.setFont(score_font);
            g2d.drawString(Integer.toString(score), -50, -100);
            g2d.setPaint(player.getColor());
            //the track the player is on opens on the front end, thats what this path 2D is:
            Path2D player_track_shape = new Path2D.Double();
            player_track_shape.moveTo(player_track.xpoints[0],player_track.ypoints[0]);
            player_track_shape.lineTo(player_track.xpoints[3],player_track.ypoints[3]);
            player_track_shape.lineTo(player_track.xpoints[2],player_track.ypoints[2]);
            player_track_shape.lineTo(player_track.xpoints[1],player_track.ypoints[1]);

            g2d.draw(player_track_shape);
            g2d.draw(player_shape);
            // try drawing the projectile
            try{ // if it has been destroyed, a NullPointerException will be thrown and the projectile will be skipped
                for(Polygon p: projectile_shapes){
                    g2d.draw(p);
                }
            }
            catch(NullPointerException e){}

            try{ // if wall has been destroyed, a NullPointerException will be thrown and that wall will be skipped
                for(Polygon w: current_level_wall_shapes){//loop through all walls in level

                    int index = current_level_wall_shapes.indexOf(w);
                    g2d.setPaint(current_level_walls.get(index).getColor());
                    g2d.draw(w);

                }
            }
            catch(NullPointerException e){}

        }
    }


    public void update(int increment){
        for(Wall w: current_level_walls){
            if(w != null && current_level_walls.indexOf(w) < current_level_wall_shapes.size()){ // ensure current projectile has still not been destroyed already
                int wall_index = w.getIndex();
                //as long as the wall hasnt reached the end of the track
                if(wall_index < 49){// 59 here is NUM_MIDPOINTS
                    w.setIndex(wall_index+increment);
                    w.setCenterPoint();

                    Polygon wall_shape = w.getPolygon(eyeDistance);
                    current_level_wall_shapes.set(current_level_walls.indexOf(w), wall_shape); //replaces the last position of this wall
                }
                Point3D player_center = getMidpoint(player.getCurrentTrack().getVertex(0), player.getCurrentTrack().getVertex(1));
                double px = player_center.x;
                double wx = - w.getCenterPoint().x;
                double py = player_center.y;
                double wy = w.getCenterPoint().y;
                double pz = player_center.z;
                double wz = w.getCenterPoint().z;
                double x_diff = 0;
                double y_diff = 0;
                double z_diff = 0;
                if(px>wx){x_diff = px-wx;}
                else{x_diff = wx-px;}
                if(py>wy){y_diff = py-wy;}
                else{y_diff = wy-py;}
                if(pz>wz){z_diff = pz-wz;}
                else{z_diff = wz-pz;}
                if(w != null && x_diff <= 5 && y_diff <= 5 && z_diff <= 24 && wz >= 67 && w.getCurrentTrack().isEqualTo(player.getCurrentTrack()) ){
                    // check if projectile hits wall and destroy both if so.
                    System.out.println("COLLISION");
                    player.setHealth(player.getHealth()-1);
                    System.out.println("Player health to: "+player.getHealth());
                    w.setIndex(48);
                    w.setCenterPoint();

                }
            }

        }
        for(Projectile p: projectiles){ //update all projectiles
            if(p != null){ // ensure current projectile has not been destroyed already
                Boolean wall_found = false;
                for(Wall w: current_level_walls){
                    if(!wall_found){//only check more walls if one hasnt been found
                        double px = p.getCenterPoint().x;
                        double wx = - w.getCenterPoint().x;
                        double py = p.getCenterPoint().y;
                        double wy = w.getCenterPoint().y;
                        double pz = p.getCenterPoint().z;
                        double wz = w.getCenterPoint().z;

                        double x_diff = 0;
                        double y_diff = 0;
                        double z_diff = 0;


                        if(px>wx){x_diff = px-wx;}
                        else{x_diff = wx-px;}
                        if(py>wy){y_diff = py-wy;}
                        else{y_diff = wy-py;}
                        if(pz>wz){z_diff = pz-wz;}
                        else{z_diff = wz-pz;}

                        if(w != null && x_diff <= 19 && y_diff <= 19 && z_diff <= 19 && wz >= 70){
                            // check if projectile hits wall and destroy both if so.
                            if(w.getHealth() == 1){
                                w.setIndex(48);
                                w.setCenterPoint();
                            } else{ w.setHealth(w.getHealth()-1); }
                            score += 100;
                            p.setIndex(49);
                            p.setCenterPoint();
                            wall_found = true;
                        }
                    }
                }
            }
            if(p != null){ // ensure current projectile has still not been destroyed already
                int proj_index = p.getIndex();
                //as long as the projectile hasnt reached the end of the track
                if(proj_index < 49){// 50 here is NUM_MIDPOINTS
                    p.setIndex(proj_index+increment);
                    p.setCenterPoint();
                    Polygon proj_shape = p.getPolygon(eyeDistance);
                    projectile_shapes.set(projectiles.indexOf(p), proj_shape); //replaces the last position of this projectile

                }
                else if(proj_index >= 49){ // if the projectile reaches the end of the track, remove it
                    projectile_shapes.set(projectiles.indexOf(p), new Polygon());
                    projectiles.set(projectiles.indexOf(p), null );
                    //to ensure no ConcurrentModificationException is thrown, we must replace rather than remove

                }
            }
        }

    }

    public void actionPerformed(ActionEvent evt) {
        //...Perform a task...
        if(timer < wall_shapes.size()){
            current_level_wall_shapes.add(wall_shapes.get(timer));
        }
        if(level_completed && eyeDistance < 1700){
            eyeDistance += 10;
            repaint();
        }
        else if(level_completed && eyeDistance >= 1700){
            //draw next level:
            currentLevel=3;
            current_level_walls.clear();
            current_level_wall_shapes.clear();
            projectiles.clear();
            projectile_shapes.clear();
            wall_shapes.clear();
            active_level = new Level(currentLevel);
            levels.add(new Level(currentLevel));
            player = new Player(levels.get(currentLevel-2));
            current_level_walls = active_level.getWalls();
            for(Wall w: current_level_walls){ wall_shapes.add(w.getPolygon(eyeDistance)); }
            eyeDistance = 100;
            level_completed = false;
            repaint();
        }
        else if(!level_completed){
            //updateEnemies(1);
            update(1);
            repaint();
        }
        timer+=1;
        repaint();
        //System.out.println("Working...");
    }

    public Point3D getMidpoint(Point3D p1, Point3D p2){
        Point3D track_vertex0 = p1;
        Point3D track_vertex1 = p2;
        //used to store x,y coords of the midpoint between the current tracks front two verticies
        double midpoint_x;
        double midpoint_y;
        //calculates x coord based on which front vertex has a bigger x value
        if(track_vertex1.x>track_vertex0.x){midpoint_x = track_vertex0.x+((track_vertex1.x-track_vertex0.x)/2);}
        else{midpoint_x = track_vertex1.x + ((track_vertex0.x-track_vertex1.x)/2);}
        //calculates y coord based on which front vertex has a bigger y value
        if(track_vertex1.y>track_vertex0.y){midpoint_y = track_vertex0.y + ((track_vertex1.y-track_vertex0.y)/2);}
        else{midpoint_y = track_vertex1.y + ((track_vertex0.y-track_vertex1.y)/2);}

        return new Point3D(midpoint_x, midpoint_y, p1.z);
    }

    public void transitionLevels(){
        for(double i = eyeDistance; i<1700; i+=5){
            eyeDistance += 5;
            repaint();
        }
        eyeDistance = 100;
        currentLevel++;
        repaint();
    }

    //methods for user interaction
    public void keyPressed(KeyEvent ke){
        System.out.println(ke.getKeyChar()+":"+ke.getKeyCode()); //prints and key pressed and according key code
        if(ke.getKeyCode() == ke.VK_LEFT){
            //Move Player Left
            player.moveCurrentTrack("left");
            repaint();
        }
        else if(ke.getKeyCode() == ke.VK_RIGHT){
            //Move Player Right
            player.moveCurrentTrack("right");
            repaint();
        }
        else if(ke.getKeyCode() == ke.VK_SPACE){
            Projectile p = new Projectile(player.getCurrentTrack());
            projectiles.add(p);
            projectile_shapes.add(p.getPolygon(eyeDistance));
            repaint();

        }
        else if(ke.getKeyCode() == ke.VK_UP){
            eyeDistance += 5;
            repaint();
            //Animates flying through level and then draws next level:
            //transitionLevels();
        }
        else if(ke.getKeyCode() == ke.VK_DOWN){
            eyeDistance -= 5;
            repaint();
            //Animates flying through level and then draws next level:
            //transitionLevels();
        }
    }
    public void keyReleased(KeyEvent ke){}
    public void keyTyped(KeyEvent ke){}

}
