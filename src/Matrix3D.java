public class Matrix3D {
    
    public float m[][];

    public Matrix3D(float m[][]) {
        this.m = m;
    }

    public Matrix3D(){
        m = new float[4][4];
    }

    public static Point3D multiplyMatrixAndPoint(Matrix3D mat, Point3D p) {
        // It uses a 4D matrix to make us of Homogeneous Coordinates, to be
        // ..able to translate with matrix operations
        float pt[] = { 0, 0, 0, 0 };
        
        float vals[] = { p.x, p.y, p.z, p.w };
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                pt[i] += mat.m[i][j] * vals[j];
            }
        }
        return new Point3D(pt[0], pt[1], pt[2], pt[3]);
    }
}