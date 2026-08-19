import java.util.Scanner;

public class Datos {
    public static double[] solicitar(int opcion) {
        Scanner sc = new Scanner(System.in);
        double[] valores;

        switch(opcion) {
            case 1: // Triángulo
                valores = new double[2];
                System.out.print("Ingrese la base: ");
                valores[0] = sc.nextDouble();
                System.out.print("Ingrese la altura: ");
                valores[1] = sc.nextDouble();
                break;
            case 2: // Círculo
                valores = new double[1];
                System.out.print("Ingrese el radio: ");
                valores[0] = sc.nextDouble();
                break;
            case 3: // Cuadrado
                valores = new double[1];
                System.out.print("Ingrese el lado: ");
                valores[0] = sc.nextDouble();
                break;
            default:
                valores = new double[0];
                System.out.println("Opción inválida");
        }
        return valores;
    }
}
