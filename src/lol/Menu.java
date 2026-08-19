import java.util.Scanner;

public class Menu {
    public static int mostrar() {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== MENÚ DE ÁREAS ===");
        System.out.println("1. Triángulo");
        System.out.println("2. Círculo");
        System.out.println("3. Cuadrado");
System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");
        int opcion = sc.nextInt();
        return opcion;
    }
}

