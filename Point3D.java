import java.awt.geom.Point2D;


public class Point3D{
    //coords are public so a get method is not nessasary
   public double x;
   public double y;
   public double z;

   private Transform3D t;
   double w = 1;


   public Point3D(double xcoord, double ycoord, double zcoord)
   {
      t = new Transform3D();
      x = xcoord;
      y = ycoord;
      z = zcoord;
   }
   //set methods for coords
   public void setX(double new_x){ x = new_x; }
   public void setY(double new_y){ y = new_y; }
   public void setZ(double new_z){ z = new_z; }

   public Point3D rotate(double[][] matrix){
       double[] vect = {x, y, z, w};
       double[] tempOutput = new double[4];

       for(int i = 0; i < 4; i++){
            for(int j=0;j < 4;j++){
                tempOutput[i] += matrix[i][j] * vect[j];
            }

        }
        double new_x = tempOutput[0]/tempOutput[3];
        double new_y = tempOutput[1]/tempOutput[3];
        double new_z = tempOutput[2]/tempOutput[3];
        return new Point3D(new_x,new_y,new_z);
    }

    public double[][] rotateX(double theta){
        double[][] rotMatrix = t.getRotateInstanceX(Math.toRadians(theta));
        return rotMatrix;
    }
    public double[][] rotateY(double theta){
        double[][] rotMatrix = t.getRotateInstanceY(Math.toRadians(theta));
        return rotMatrix;
    }
    public double[][] rotateZ(double theta){
        double[][] rotMatrix = t.getRotateInstanceZ(Math.toRadians(theta));
        return rotMatrix;
    }


    //Methods to project 3D points to 2D points
    public Point2D projectPoint3D(double eyeDistance){

        Point3D new_3Dpoint = this;

        double x1 = x/(z/eyeDistance);
        double y1 = y/(z/eyeDistance);
        if(z>200){
            x1 = -x1;
        }
        Point2D new_point = new Point2D.Double(x1, y1);
        return new_point;
    }

}
