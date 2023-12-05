package io.sim;

public class Account extends Thread {
    
    private double saldo;
    private String tipoCliente;

    public Account(String tipoCliente, int senha) {
        
        this.tipoCliente = tipoCliente;

        if (this.tipoCliente.contains("Motorista")){
            this.saldo = 1000;
        } else if (this.tipoCliente.equals("Company")){
            this.saldo = 1000000;
        } else if (this.tipoCliente.equals("FuelStation")){
            this.saldo = 10000;
        }
    }

    public double getSaldo() {
        return saldo;
    }

    public String getTipoCliente() {
        return tipoCliente;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    @Override
    public void run() {}

}
