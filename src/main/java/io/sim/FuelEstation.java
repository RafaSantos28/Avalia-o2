package io.sim;

public class FuelEstation extends Thread {

    //Cliente do Alpha BAnk
    private Account contaPosto;

    private int quantidadeBombas = 0;//quantiudade de bombas por vez
    private double litrosAbastecer = 15; //litros que o carro ira abastecer
    
    public FuelEstation(){
        this.contaPosto = new Account("FuelStation", 7891);
    }
    
    public Account getContaPosto() {
        return contaPosto;
    }

    public void abastecerCarro(Car carro) throws InterruptedException{
        if(verificarBomba()){
            quantidadeBombas++;
            carro.setFuelTank(litrosAbastecer);
            quantidadeBombas--;
        }
    }

    public boolean verificarBomba(){
        if(quantidadeBombas <= 2){
            return true;
        }
        return false;
    }

    @Override
    public void run() {

    }
}
