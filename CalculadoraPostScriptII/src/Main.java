import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Stack;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in);
             PrintWriter logWriter = new PrintWriter(new FileWriter("logs.txt"))) {

            Stack<Double> stack = new Stack<>();

            while (true) {
                System.out.print("Ingrese una expresión PostScript (o 'quit' para salir): ");
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("quit")) {
                    break;
                }

                if (input.isEmpty()) {
                    System.out.println("La expresión está vacía.");
                    continue;
                }

                try {
                    evaluateExpression(input, stack);
                    System.out.println("Resultado: " + stack.peek());
                } catch (NumberFormatException e) {
                    System.out.println("Error: Entrada no válida.");
                    logWriter.println("Error: Entrada no válida.");
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                    logWriter.println("Error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error al abrir el archivo de registro.");
        }
    }

    private static void evaluateExpression(String expression, Stack<Double> stack) {
        String[] tokens = expression.split("\\s+");
        for (String token : tokens) {
            if (token.matches("-?\\d+(\\.\\d+)?")) {
                stack.push(Double.parseDouble(token));
            } else if (token.equals("add")) {
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("No hay suficientes operandos para la operación 'add'.");
                }
                double operand2 = stack.pop();
                double operand1 = stack.pop();
                stack.push(operand1 + operand2);
            } else if (token.equals("sub")) { // Agregar soporte para resta
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("No hay suficientes operandos para la operación 'sub'.");
                }
                double operand2 = stack.pop();
                double operand1 = stack.pop();
                stack.push(operand1 - operand2);
            } else if (token.equals("mul")) { // Agregar soporte para multiplicación
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("No hay suficientes operandos para la operación 'mul'.");
                }
                double operand2 = stack.pop();
                double operand1 = stack.pop();
                stack.push(operand1 * operand2);
            } else if (token.equals("div")) { // Agregar soporte para división
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("No hay suficientes operandos para la operación 'div'.");
                }
                double operand2 = stack.pop();
                double operand1 = stack.pop();
                if (operand2 == 0) {
                    throw new IllegalArgumentException("División por cero no permitida.");
                }
                stack.push(operand1 / operand2);
            } else if (token.equals("pop")) {
                if (!stack.isEmpty()) {
                    stack.pop();
                } else {
                    throw new IllegalArgumentException("La pila está vacía, no se puede ejecutar 'pop'.");
                }
            } else if (token.equals("dup")) {
                if (!stack.isEmpty()) {
                    double topValue = stack.peek();
                    stack.push(topValue);
                } else {
                    throw new IllegalArgumentException("La pila está vacía, no se puede ejecutar 'dup'.");
                }
            } else if (token.equals("exch")) {
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("No hay suficientes operandos para la operación 'exch'.");
                }
                double operand2 = stack.pop();
                double operand1 = stack.pop();
                stack.push(operand2);
                stack.push(operand1);
            } else {
                throw new IllegalArgumentException("Operador no reconocido: " + token);
            }
        }
    }
}