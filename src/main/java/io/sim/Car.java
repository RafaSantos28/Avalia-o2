package io.sim;

import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.sumo.objects.SumoColor;
import io.sim.json.JSONObject;
import io.sim.servidorCliente.ClienteSocket;
import it.polito.appeal.traci.SumoTraciConnection;

public class Car extends Vehicle implements Runnable{ //n sei se ta certo

    private Auto auto;
    
    private String idDriver;
    private String idCar;

    //Variaveis para o KM rodado
    private double distance=0;
    private double diferencaKM=0;
    private static double totalRodado;
    private int auxInicialKM=0;
    private int auxFinalKM=0;   
    
    private int cont=0;

    //variaveis para o consumo de combustivel
    private double fuelTank = 10;
    private double consumido=0;

    // fuelType: 1-diesel, 2-gasoline, 3-ethanol, 4-hybrid
    private int fuelType = 2;
	private int fuelPreferential = 2;
	private double fuelPrice = 3.40;
	private int personCapacity = 1;
	private int personNumber = 1;
    private SumoColor green = new SumoColor(0, 255, 0, 126);
    private SumoTraciConnection sumo;


    String edgeAux = " "; // errado, tem q ser global
        
    public Car(String idCar, SumoTraciConnection sumo, String idDriver){
        this.idCar = idCar;
        this.idDriver = idDriver;
        
		try {
            this.sumo = sumo;
            this.auto = new Auto(true, idCar, green,idDriver, sumo, 500, fuelType, fuelPreferential, fuelPrice, personCapacity, personNumber);
            this.auto.setPriority(7);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }   

	public Auto getAuto() { 
        return auto;
    }

    public String getIdAuto() {
        return idCar;
    }

    public boolean isExisting() {
        try{
            return this.sumo.do_job_get(Vehicle.getIDList()).toString().contains(idCar);
        }
        catch(Exception err){
            err.printStackTrace();
            return false;
        }
    }

    public void kmRodado() throws Exception {
        if (cont == 0){
            diferencaKM=0;
        }
        distance = this.auto.getDistance();
        
        if ((distance - diferencaKM )> 0) {
            totalRodado = totalRodado +(distance - diferencaKM);
            diferencaKM = distance;
            cont++;
        }

        else if((distance - diferencaKM )< 0){
            cont = 0;
        }   
        maisUmKM();
    }

    public void maisUmKM(){
        auxInicialKM=((int) totalRodado);
        if (auxInicialKM != auxFinalKM) {
            auxFinalKM = auxInicialKM;
            msgKM();
        }    
    }

    public void msgKM(){
        //Company Cliente
        try{
            ClienteSocket cliente = new ClienteSocket();
            cliente.conectar(5000);
            JSONObject json = new JSONObject();
            json.put("tipo", "mais um KM rodado");
            json.put("dados", idDriver);
            Thread.sleep(100);
            cliente.enviarMensagem(json.toString());
        }
        catch(Exception err){} 
    }

    public void consumoCombustivel()throws Exception{
        consumido = this.auto.consumo();
        if (consumido> 0) {
            fuelTank = fuelTank-consumido;
        }
    }

    public double getFuelTank() {
        return fuelTank;
    }

    public void setVelocidade(double velocidade) throws Exception{
        this.auto.setSpeed(velocidade);
    }

    public void setFuelTank(double fuelTank_) throws InterruptedException {
        Thread.sleep(120000);
        this.fuelTank = this.fuelTank+ fuelTank_;
    }

    public void enviarRelatorio() throws Exception{
        ClienteSocket cliente = new ClienteSocket();
        cliente.conectar(5000);
        JSONObject json = new JSONObject();
        DadosRelatorio dadosRelatorio = new DadosRelatorio(idCar,auto.getIdRoute(),auto.getSpeed(),auto.getDistance(),auto.consumo(),"Gasolina",auto.getCO2Emission(),auto.getPosicao()[0],auto.getPosicao()[1],(String) this.sumo.do_job_get(Vehicle.getRoadID(this.idCar)));
        json.put("tipo", "gerar relatorio");
        json.put("dados",dadosRelatorio.toJSONString());
        Thread.sleep(100);
        cliente.enviarMensagem(json.toString());
    }

    public void setVelocidadePorEdges() throws Exception{
        String edge = (String) this.sumo.do_job_get(Vehicle.getRoadID(this.idCar));
        if (!edge.equals(edgeAux) && fuelTank > 3){
            edgeAux = edge;
            this.auto.setSpeed(1000); //alterar
        }
    }

    @Override
    public void run(){
        auto.start();
        new Thread(new Runnable(){
            @Override
            public void run(){
                try{
                    while(true){
                        Thread.sleep(5000);  
                        if(!isExisting()) break;
                        enviarRelatorio();
                    }
                }
                catch(Exception err){}
            }
        }).start();

        try {
            while (true) {
                Thread.sleep(500);
                if(isExisting()){
                    kmRodado(); 
                    consumoCombustivel();
                    auto.getPosicao();
                    setVelocidadePorEdges(); //velocidade maxima por edges
                }
                else{
                    break;              
                }
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }   
    } 

}


