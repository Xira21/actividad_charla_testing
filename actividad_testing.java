import java.math.BigDecimal;
import java.math.RoundingMode;

class Banco {
    private BigDecimal saldo;

    public Banco(double saldoInicial) {
        this.saldo = BigDecimal.valueOf(saldoInicial);
    }

    public void depositar(double cantidad) {
        if (cantidad > 0) {
            saldo = saldo.add(BigDecimal.valueOf(cantidad));
        } else {
            System.out.println("Error: No se pueden depositar valores negativos");
        }
    }

    public void retirar(double cantidad) {
        if (cantidad > 0 && saldo.compareTo(BigDecimal.valueOf(cantidad)) >= 0) {
            saldo = saldo.subtract(BigDecimal.valueOf(cantidad));
        } else if (cantidad <= 0) {
            System.out.println("Error: No se pueden retirar valores negativos o cero");
        } else {
            System.out.println("Fondos insuficientes");
        }
    }

    public double getSaldo() {
        return saldo.setScale(2, RoundingMode.HALF_UP).doubleValue(); // Redondea a 2 decimales
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("Ejecutando pruebas...");

        testDeposito();
        testRetiroExitoso();
        testRetiroFallido();
        testRetiroNegativo();
        testDepositoNegativo();
        testRetiroExacto();
        testDepositoCero();
        testRetiroCero();
        testDecimales();
        testConcurrencia();

        System.out.println("Pruebas finalizadas.");
    }

    public static void assertEquals(double esperado, double obtenido, String mensaje) {
        if (Math.abs(esperado - obtenido) < 0.0001) {
            System.out.println("✅ " + mensaje);
        } else {
            System.out.println("❌ ERROR: " + mensaje + " (Esperado: " + esperado + ", Obtenido: " + obtenido + ")");
        }
    }

    public static void testDeposito() {
        Banco banco = new Banco(100);
        banco.depositar(50);
        assertEquals(150, banco.getSaldo(), "Test depósito");
    }

    public static void testRetiroExitoso() {
        Banco banco = new Banco(100);
        banco.retirar(50);
        assertEquals(50, banco.getSaldo(), "Test retiro exitoso");
    }

    public static void testRetiroFallido() {
        Banco banco = new Banco(100);
        banco.retirar(150);
        assertEquals(100, banco.getSaldo(), "Test retiro fallido (saldo insuficiente)");
    }

    public static void testRetiroNegativo() {
        Banco banco = new Banco(100);
        banco.retirar(-50); 
        assertEquals(100, banco.getSaldo(), "Test retiro negativo");
    }

    public static void testDepositoNegativo() {
        Banco banco = new Banco(100);
        banco.depositar(-50);
        assertEquals(100, banco.getSaldo(), "Test depósito negativo");
    }

    public static void testRetiroExacto() {
        Banco banco = new Banco(100);
        banco.retirar(100);
        assertEquals(0, banco.getSaldo(), "Test retiro exacto (saldo debería ser 0)");
    }

    public static void testDepositoCero() {
        Banco banco = new Banco(100);
        banco.depositar(0);
        assertEquals(100, banco.getSaldo(), "Test depósito de 0");
    }

    public static void testRetiroCero() {
        Banco banco = new Banco(100);
        banco.retirar(0);
        assertEquals(100, banco.getSaldo(), "Test retiro de 0");
    }

    public static void testDecimales() {
        Banco banco = new Banco(100);
        banco.depositar(0.1);
        banco.depositar(0.2);
        System.out.println("Test decimales: Esperado = 100.3, Obtenido = " + banco.getSaldo());
        assertEquals(100.3, banco.getSaldo(), "Test decimales");
    }

    public static void testConcurrencia() {
        Banco banco = new Banco(100);
        
        Thread t1 = new Thread(() -> banco.depositar(50));
        Thread t2 = new Thread(() -> banco.depositar(30));
        Thread t3 = new Thread(() -> banco.retirar(20));

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(160, banco.getSaldo(), "Test concurrencia (depósitos simultáneos)");
    }
}
