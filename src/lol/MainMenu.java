int opcion;
public class MainMenu {
    public static void main(String[] args) {
        // 1. Mostrar menú y obtener opción
        int opcion = Menu.mostrar();

        // 2. Pedir datos según opción
        double[] datos = Datos.solicitar(opcion);

        // 3. Calcular área
        double area = Calculos.calcular(opcion, datos);

        // 4. Imprimir resultado
        Imprimir.mostrar(area, opcion);
    }
}
