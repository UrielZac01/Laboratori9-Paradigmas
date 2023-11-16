class SaldoEfectivoInsuficiente(Exception):
    pass

class SaldoCuentaInsuficiente(Exception):
    pass

class CajeroAutomatico:
    def __init__(self):
        self.saldo_efectivo = 100000
        self.cuentahabientes = {
            '123': {'nombre': 'Uriel', 'saldo': 50000},
            '456': {'nombre': 'Javier', 'saldo': 70000},
            '789': {'nombre': 'Emilio', 'saldo': 5200}
        }
        self.cuenta_actual = None

    def autenticar(self, cuenta):
        if cuenta in self.cuentahabientes:
            self.cuenta_actual = cuenta
            print(f"Bienvenido, {self.cuentahabientes[cuenta]['nombre']}!")
        else:
            print("Cuenta no válida. Por favor, inténtelo de nuevo.")

    def mostrar_datos_cuenta(self):
        if self.cuenta_actual is not None:
            datos_cuenta = self.cuentahabientes[self.cuenta_actual]
            print(f"Nombre: {datos_cuenta['nombre']}")
            print(f"Cuenta: {self.cuenta_actual}")
            print(f"Saldo disponible: ${datos_cuenta['saldo']}")
        else:
            print("Por favor, inicie sesión primero.")

    def deposito_propio(self, monto):
        if self.cuenta_actual is not None:
            self.cuentahabientes[self.cuenta_actual]['saldo'] += monto
            print(f"Depósito exitoso. Nuevo saldo: ${self.cuentahabientes[self.cuenta_actual]['saldo']}")
        else:
            print("Por favor, inicie sesión primero.")

    def deposito_otras_cuentas(self, cuenta_destino, monto):
        if self.cuenta_actual is not None:
            if monto <= self.saldo_efectivo:
                if cuenta_destino in self.cuentahabientes:
                    self.cuentahabientes[cuenta_destino]['saldo'] += monto
                    self.saldo_efectivo -= monto
                    print(f"Depósito a la cuenta {cuenta_destino} exitoso.")
                else:
                    print("La cuenta de destino no es válida.")
            else:
                raise SaldoEfectivoInsuficiente("Saldo en efectivo insuficiente para realizar el depósito.")
        else:
            print("Por favor, inicie sesión primero.")

    def transferencia(self, cuenta_destino, monto):
        if self.cuenta_actual is not None:
            if monto <= self.cuentahabientes[self.cuenta_actual]['saldo']:
                if cuenta_destino in self.cuentahabientes:
                    self.cuentahabientes[self.cuenta_actual]['saldo'] -= monto
                    self.cuentahabientes[cuenta_destino]['saldo'] += monto
                    print(f"Transferencia a la cuenta {cuenta_destino} exitosa.")
                else:
                    print("La cuenta de destino no es válida.")
            else:
                raise SaldoCuentaInsuficiente("Saldo en cuenta insuficiente para realizar la transferencia.")
        else:
            print("Por favor, inicie sesión primero.")

    def retiro_efectivo(self, monto):
        if self.cuenta_actual is not None:
            if monto <= self.saldo_efectivo and monto <= self.cuentahabientes[self.cuenta_actual]['saldo']:
                self.saldo_efectivo -= monto
                self.cuentahabientes[self.cuenta_actual]['saldo'] -= monto
                print(f"Retiro de ${monto} exitoso. Nuevo saldo: ${self.cuentahabientes[self.cuenta_actual]['saldo']}")
            else:
                raise SaldoEfectivoInsuficiente("Saldo en efectivo o en cuenta insuficiente para realizar el retiro.")
        else:
            print("Por favor, inicie sesión primero.")


# Función principal para interactuar con el usuario
def main():
    cajero = CajeroAutomatico()

    while True:
        print("\nMenú:")
        print("1. Iniciar sesión")
        print("2. Mostrar datos de cuenta")
        print("3. Depósito en cuenta propia")
        print("4. Depósito en otras cuentas")
        print("5. Transferencia a otras cuentas")
        print("6. Retiro de efectivo")
        print("0. Salir")

        opcion = input("Seleccione una opción: ")

        if opcion == '1':
            cuenta = input("Ingrese su número de cuenta: ")
            cajero.autenticar(cuenta)
        elif opcion == '2':
            cajero.mostrar_datos_cuenta()
        elif opcion == '3':
            monto = float(input("Ingrese el monto a depositar: "))
            cajero.deposito_propio(monto)
        elif opcion == '4':
            cuenta_destino = input("Ingrese la cuenta de destino: ")
            monto = float(input("Ingrese el monto a depositar: "))
            cajero.deposito_otras_cuentas(cuenta_destino, monto)
        elif opcion == '5':
            cuenta_destino = input("Ingrese la cuenta de destino: ")
            monto = float(input("Ingrese el monto a transferir: "))
            cajero.transferencia(cuenta_destino, monto)
        elif opcion == '6':
            monto = float(input("Ingrese el monto a retirar: "))
            cajero.retiro_efectivo(monto)
        elif opcion == '0':
            print("¡Hasta luego!")
            break
        else:
            print("Opción no válida. Por favor, seleccione una opción correcta.")

if __name__ == "__main__":
    main()