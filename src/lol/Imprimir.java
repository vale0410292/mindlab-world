public class Imprimir {
    public static void mostrar(double area, int opcion) {
        String figura = "";
        switch(opcion) {
            case 1: figura = "triángulo"; break;
            case 2: figura = "círculo"; break;
            case 3: figura = "cuadrado"; break;
        }
        System.out.println("El área del " + figura + " es: " + area);
    }
}

