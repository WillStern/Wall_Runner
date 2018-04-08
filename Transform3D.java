public class Transform3D{

   public int[][] getPerspectiveTransform(int e){

      int [][] matrix = {{1, 0,     0,  0},
                         {0, 1,     0,  0},
                         {0, 0,     1,  0},
                         {0, 0, (-1)/e, 1}};

      return matrix;

   }

   public double[][] getRotateInstanceZ(double theta){

      double [][] matrix = {{Math.cos(theta), -Math.sin(theta),   0,  0},
                           {Math.sin(theta), Math.cos(theta),     0,  0},
                           {     0,                  0,           1,  0},
                           {     0,                  0,           0,  1}};

      return matrix;

   }

   public double[][] getRotateInstanceX(double theta){

      double [][] matrix = {{   1,            0,                   0,              0},
                            {   0,     Math.cos(theta),     -Math.sin(theta),      0},
                            {   0,     Math.sin(theta),     Math.cos(theta),       0},
                            {   0,            0,                   0,             1}};

      return matrix;

   }

   public double[][] getRotateInstanceY(double theta){

      double [][] matrix = {{     Math.cos(theta),    0,     Math.sin(theta),   0},
                            {     0,                  1,              0,        0},
                            {     -Math.sin(theta),   0,     Math.cos(theta),   0},
                            {     0,                  0,           0,            1}};

      return matrix;

   }

   public double[][] getRotateInstanceArb(int vx, int vy, int vz, double theta){
       double factor = Math.sqrt(vx*vx + vy*vy + vz*vz);
        double unitx = vx/factor;
        double unity = vy/factor;
        double unitz = vz/factor;
        double c = 1-(Math.cos(theta));
        double s = Math.sin(theta);

        double[][] matrix = {{Math.cos(theta)+(unitx*unitx*c), (unitx*unity*c) - (unitz*s), (unitx*unitz*c)+(unity*s), 0},
                            {((unity*unitx*c)+(unitz*s)), Math.cos(theta)+(unity*unity*c), (unity*unitz*c) - (unitx*s), 0},
                            {(unitz*unitx*c) - (unity*s), (unitz*unity*c)+(unitx*s), Math.cos(theta)+(unitz*unitz*c), 0},
                            {0, 0, 0, 1}};
        return matrix;
    }


}
