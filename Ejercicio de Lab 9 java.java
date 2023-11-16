import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class SaldoEfectivoInsuficienteException extends Exception {
    SaldoEfectivoInsuficienteException(String message) {
        super(message);
    }
}

class SaldoCuentaInsuficienteException extends Exception {
    SaldoCuentaInsuficienteException(String message) {
        super(message);
    }
}

class CajeroAutomatico {
    private int saldoEfectivo = 100000;
    private Map<String, Cuentahabiente> cuentahabientes = new HashMap<>();
    private String cuentaActual = null;

    void autenticar(String cuenta) {
        if (cuentahabientes.containsKey(cuenta)) {
            cuentaActual = cuenta;
            System.out.println("Bienvenido, " + cuentahabientes.get(cuenta).getNombre() + "!");
        } else {
            System.out.println("Cuenta no valida. Por favor, intentelo de nuevo.");
        }
    }

    void mostrarDatosCuenta() {
        if (cuentaActual != null) {
            Cuentahabiente datosCuenta = cuentahabientes.get(cuentaActual);
            System.out.println("Nombre: " + datosCuenta.getNombre());
            System.out.println("Cuenta: " + cuentaActual);
            System.out.println("Saldo disponible: $" + datosCuenta.getSaldo());
        } else {
            System.out.println("Por favor, inicie sesion primero.");
        }
    }

    void depositoPropio(double monto) {
        if (cuentaActual != null) {
            cuentahabientes.get(cuentaActual).depositar(monto);
            System.out.println("Deposito exitoso. Nuevo saldo: $" + cuentahabientes.get(cuentaActual).getSaldo());
        } else {
            System.out.println("Por favor, inicie sesion primero.");
        }
    }

    void depositoOtrasCuentas(String cuentaDestino, double monto) throws SaldoEfectivoInsuficienteException {
        if (cuentaActual != null) {
            if (monto <= saldoEfectivo) {
                if (cuentahabientes.containsKey(cuentaDestino)) {
                    cuentahabientes.get(cuentaDestino).depositar(monto);
                    saldoEfectivo -= monto;
                    System.out.println("Deposito a la cuenta " + cuentaDestino + " exitoso.");
                } else {
                    System.out.println("La cuenta de destino no es valida.");
                }
            } else {
                throw new SaldoEfectivoInsuficienteException("Saldo en efectivo insuficiente para realizar el deposito.");
            }
        } else {
            System.out.println("Por favor, inicie sesion primero.");
        }
    }

    void transferencia(String cuentaDestino, double monto) throws SaldoCuentaInsuficienteException {
        if (cuentaActual != null) {
            if (monto <= cuentahabientes.get(cuentaActual).getSaldo()) {
                if (cuentahabientes.containsKey(cuentaDestino)) {
                    cuentahabientes.get(cuentaActual).retirar(monto);
                    cuentahabientes.get(cuentaDestino).depositar(monto);
                    System.out.println("Transferencia a la cuenta " + cuentaDestino + " exitosa.");
                } else {
                    System.out.println("La cuenta de destino no es valida.");
                }
            } else {
                throw new SaldoCuentaInsuficienteException("Saldo en cuenta insuficiente para realizar la transferencia.");
            }
        } else {
            System.out.println("Por favor, inicie sesion primero.");
        }
    }

    void retiroEfectivo(double monto) throws SaldoEfectivoInsuficienteException {
        if (cuentaActual != null) {
            if (monto <= saldoEfectivo && monto <= cuentahabientes.get(cuentaActual).getSaldo()) {
                saldoEfectivo -= monto;
                cuentahabientes.get(cuentaActual).retirar(monto);
                System.out.println("Retiro de $" + monto + " exitoso. Nuevo saldo: $" + cuentahabientes.get(cuentaActual).getSaldo());
            } else {
                throw new SaldoEfectivoInsuficienteException("Saldo en efectivo o en cuenta insuficiente para realizar el retiro.");
            }
        } else {
            System.out.println("Por favor, inicie sesion primero.");
        }
    }
}

class Cuentahabiente {
    private String nombre;
    private double saldo;

    Cuentahabiente(String nombre, double saldo) {
        this.nombre = nombre;
        this.saldo = saldo;
    }

    String getNombre() {
        return nombre;
    }

    double getSaldo() {
        return saldo;
    }

    void depositar(double monto) {
        saldo += monto;
    }

    void retirar(double monto) {
        saldo -= monto;
    }
}

public class main {
    public static void main(String[] args) {
        CajeroAutomatico cajero = new CajeroAutomatico();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Iniciar sesion");
            System.out.println("2. Mostrar datos de cuenta");
            System.out.println("3. Deposito en cuenta propia");
            System.out.println("4. Deposito en otras cuentas");
            System.out.println("5. Transferencia a otras cuentas");
            System.out.println("6. Retiro de efectivo");
            System.out.println("0. Salir");

            System.out.print("Seleccione una opcion: ");
            String opcion = scanner.nextLine();

            try {
                switch (opcion) {
                    case "1":
                        System.out.print("Ingrese su numero de cuenta: ");
                        String cuenta = scanner.nextLine();
                        cajero.autenticar(cuenta);
                        break;
                    case "2":
                        cajero.mostrarDatosCuenta();
                        break;
                    case "3":
                        System.out.print("Ingrese el monto a depositar: ");
                        double montoDepositoPropio = Double.parseDouble(scanner.nextLine());
                        cajero.depositoPropio(montoDepositoPropio);
                        break;
                    case "4":
                        System.out.print("Ingrese la cuenta de destino: ");
                        String cuentaDestino = scanner.nextLine();
                        System.out.print("Ingrese el monto a depositar: ");
                        double montoDepositoOtrasCuentas = Double.parseDouble(scanner.nextLine());
                        cajero.depositoOtrasCuentas(cuentaDestino, montoDepositoOtrasCuentas);
                        break;
                    case "5":
                        System.out.print("Ingrese la cuenta de destino: ");
                        String cuentaTransferencia = scanner.nextLine();
                        System.out.print("Ingrese el monto a transferir: ");
                        double montoTransferencia = Double.parseDouble(scanner.nextLine());
                        cajero.transferencia(cuentaTransferencia, montoTransferencia);
                        break;
                    case "6":
                        System.out.print("Ingrese el monto a retirar: ");
                        double montoRetiro = Double.parseDouble(scanner.nextLine());
                        cajero.retiroEfectivo(montoRetiro);
                        break;
                    case "0":
                        System.out.println("Hasta luego!");
                        scanner.close();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Opcion no valida. Por favor, seleccione una opcion correcta.");
                }
            } catch (SaldoEfectivoInsuficienteException | SaldoCuentaInsuficienteException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un valor numerico valido.");
            }
        }
    }
}
