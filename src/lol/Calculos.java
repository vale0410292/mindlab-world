public class Calculos {
    public static double calcular(int opcion, double[] datos) {
        double area = 0;
        switch(opcion) {
            case 1: // Triángulo
                area = (datos[0] * datos[1]) / 2;
                break;
            case 2: // Círculo
                area = Math.PI * Math.pow(datos[0], 2);
                break;
            case 3: // Cuadrado
                area = Math.pow(datos[0], 2);
                break;
        }
        return area;
    }
}
