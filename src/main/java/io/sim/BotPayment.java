package io.sim;

public class BotPayment extends Thread {
    
    private double valorKm = 3.25;
    
    private double litroGas = 5.87; 
    
    private double valorASerPago;// para ambos
    private Account contaPagar;
    private Account contaReceber;

    private String tipoCliente; //onde sera feito o pagamento

    public BotPayment(Account conta, Account conta2, String tipoCliente) {
        this.contaReceber = conta;
        this.contaPagar = conta2; 
        this.tipoCliente = tipoCliente;
    }

    //para o Drivers
    public void valorParaDriver(){
        contaPagar.setSaldo(contaPagar.getSaldo()-valorKm);
        contaReceber.setSaldo(contaReceber.getSaldo()+valorKm);
    }

    public void valorParaFuelStation(){
        this.valorASerPago = 15 * this.litroGas;
        contaPagar.setSaldo(contaPagar.getSaldo()-valorASerPago);
        contaReceber.setSaldo(contaReceber.getSaldo()+valorASerPago);
    }

    @Override
    public void run() {
        if(this.tipoCliente.equals("Drivers")){
            valorParaDriver();
        }
        else if(this.tipoCliente.equals("FuelStation")){
            valorParaFuelStation();
        }

    }
}
