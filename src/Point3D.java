public class Point3D {
    
    float x, y, z;
    float w = 1;

    public Point3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3D(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    
    public void rotate(float angleXY, float angleYZ, float angleZX) {
        rotateXY(angleXY);
        rotateYZ(angleYZ);
        rotateZX(angleZX);
    }
    
    public void rotateXY(float angleXY){
        // Rotate in the XY plane
        angleXY *= Math.PI / 180;
        // Convert angle from Deg to Rad
        float cos = (float) Math.cos(angleXY);
        float sin = (float) Math.sin(angleXY);
        float matrixXY[][] = { 
                { cos, -sin, 0, 0 }, 
                { sin,  cos, 0, 0 },
                {   0,    0, 1, 0 }, 
                {   0,    0, 0, 1 }
        };
        Matrix3D rotationMatrixXY = new Matrix3D(matrixXY);
        Point3D rotatedPoint = Matrix3D.multiplyMatrixAndPoint(rotationMatrixXY, this);
        this.x = rotatedPoint.x;
        this.y = rotatedPoint.y;
        this.z = rotatedPoint.z;
    }
    
    public void rotateYZ(float angleYZ){
        // Rotate in the YZ plane
        angleYZ *= Math.PI / 180;
        // Convert angle from Deg to Rad
        float cos = (float) Math.cos(angleYZ);
        float sin = (float) Math.sin(angleYZ);
        float matrixYZ[][] = { 
                { 1,   0,    0, 0 }, 
                { 0, cos, -sin, 0 },
                { 0, sin,  cos, 0 }, 
                { 0,   0,    0, 1 } 
        };
        Matrix3D rotationMatrixYZ = new Matrix3D(matrixYZ);
        Point3D rotatedPoint = Matrix3D.multiplyMatrixAndPoint(rotationMatrixYZ, this);
        this.x = rotatedPoint.x;
        this.y = rotatedPoint.y;
        this.z = rotatedPoint.z;
    }
    
    public void rotateZX(float angleZX){
        // Rotate in the ZX plane
        angleZX *= Math.PI / 180;
        // Convert angle from Deg to Rad
        float cos = (float) Math.cos(angleZX);
        float sin = (float) Math.sin(angleZX);
        float matrixZX[][] = { 
                {  cos, 0, sin, 0 }, 
                {    0, 1,   0, 0 },
                { -sin, 0, cos, 0 }, 
                {    0, 0,   0, 1 } 
        };
        Matrix3D rotationMatrixZX = new Matrix3D(matrixZX);
        Point3D rotatedPoint = Matrix3D.multiplyMatrixAndPoint(rotationMatrixZX, this);
        x = rotatedPoint.x;
        y = rotatedPoint.y;
        z = rotatedPoint.z;
    }

    public void scale(float scaleX, float scaleY, float scaleZ) {
        // Scale the point with different axis multiplicators
        float matrix[][] = { 
                { scaleX,      0,      0, 0 }, 
                {      0, scaleY,      0, 0 },
                {      0,      0, scaleZ, 0 }, 
                {      0,      0,      0, 1 } 
        };
        Matrix3D scaleMatrix = new Matrix3D(matrix);
        Point3D scaledPoint = Matrix3D.multiplyMatrixAndPoint(scaleMatrix, this);
        this.x = scaledPoint.x;
        this.y = scaledPoint.y;
        this.z = scaledPoint.z;
    }

    public void translate(float transX, float transY, float transZ) {
        // Translate the point across different axis's 
        float matrix[][] = { 
                { 1, 0, 0, transX }, 
                { 0, 1, 0, transY }, 
                { 0, 0, 1, transZ },
                { 0, 0, 0,      1 }
        };
        Matrix3D translateMatrix = new Matrix3D(matrix);
        Point3D translatedPoint = Matrix3D.multiplyMatrixAndPoint(translateMatrix, this);
        this.x = translatedPoint.x;
        this.y = translatedPoint.y;
        this.z = translatedPoint.z;
    }

    public Point3D clone() {
        // Clone this point
        return new Point3D(x, y, z, w);
    }
}
