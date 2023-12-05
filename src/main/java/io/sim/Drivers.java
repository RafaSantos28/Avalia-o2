package io.sim;

import java.util.ArrayList;

import de.tudresden.sumo.cmd.Vehicle;
import io.sim.json.JSONObject;
import io.sim.servidorCliente.ClienteSocket;
import io.sim.servidorCliente.ClienteSocketHandler;
import it.polito.appeal.traci.SumoTraciConnection;

public class Drivers extends Thread{

    private Car car ;
    private String idDriver;
    private Account contaMotorista;
    
    private ArrayList<Route> routesExec = new ArrayList<>();
    private ArrayList<Route> routesFinalizadas = new ArrayList<>();

    private TransportService tS1;
    private ClienteSocket cliente;
    private FuelEstation posto;

    private SumoTraciConnection sumo;
    private String edgeAux = "";

    private long millisEdgeComeco;

    private ArrayList<ArrayList<Long>> registroDeTempos;

    private void calcularTempoEdges() throws Exception{
        if (!car.isExisting()){
            if (!edgeAux.isEmpty()){
                long millisEdgeFim = System.currentTimeMillis();
                long tempo = millisEdgeFim - millisEdgeComeco;
                registroDeTempos.get(registroDeTempos.size()-1).add(tempo);

                edgeAux = "";
            }
             return;
        }

        String edge = (String) this.sumo.do_job_get(Vehicle.getRoadID(this.car.getIdAuto()));
        
        if (!edge.equals(edgeAux) && routesExec.get(routesExec.size()-1).getEdges().contains(edge)){
            if(car.isExisting())
                car.enviarRelatorio();
            if (!edgeAux.equals("")){
                long millisEdgeFim = System.currentTimeMillis();
                long tempo = millisEdgeFim - millisEdgeComeco;
                registroDeTempos.get(registroDeTempos.size()-1).add(tempo);    
            }
            edgeAux = edge;
            millisEdgeComeco = System.currentTimeMillis();
        }
    }

    public Drivers(String idDriver, String idCar, SumoTraciConnection sumo, FuelEstation posto) throws Exception {
        this.idDriver = idDriver;
        this.posto = posto;
        this.contaMotorista = new Account(idDriver, 1234);
        this.sumo = sumo;

        this.registroDeTempos = new ArrayList<ArrayList<Long>>();

        car = new Car(idCar, sumo, idDriver);
        Thread t1 = new Thread(car);
        tS1 = new TransportService(true, idCar, car.getAuto(), sumo);
        t1.setPriority(8);
		tS1.setPriority(9);
        tS1.start();
		t1.start();
    }

    public String getIdDriver() {
        return idDriver;
    }

    public String getIdCar() {
        return car.getIdAuto();
    }

    public Account getContaMotorista() {
        return contaMotorista;
    }
    
    //metodos para array
    public void finalizarRota(Route rota) {
        this.routesExec.remove(rota);
        this.routesFinalizadas.add(rota);

    }

    public void pedirEIniciarRota(){
            JSONObject json = new JSONObject();
            json.put("tipo", "rota");
            cliente.enviarMensagem(json.toString());       
    }

    public void iniciarRota(Route rota)  {
        this.routesExec.add(rota);
        this.tS1.initializeRoute(rota);
        registroDeTempos.add(new ArrayList<Long>());
        millisEdgeComeco = System.currentTimeMillis();
        edgeAux = "";
    }

    public Route getRota(){
        return routesExec.get(0);
    }
    
    public void pagarPosto(){
        try{
            ClienteSocket clienteAlphaBank = new ClienteSocket();
            clienteAlphaBank.conectar(2000);

            JSONObject json = new JSONObject();
            json.put("tipo", "pagar posto");
            json.put("dados", idDriver);

            clienteAlphaBank.enviarMensagem(json.toString());
        }
        catch(Exception err){}
    }

    @Override
    public void run() {
        int cont=0;
        //Company
        try{
            cliente = new ClienteSocket();
            cliente.conectar(5000);

            cliente.escutar(new ClienteSocketHandler() {
            @Override
            public void handle(String msg) {               
                if (!car.isExisting()) iniciarRota(new Route(msg));
            }
            });

            while (true) {
                synchronized(car){
                    if(routesExec.size() == 0 && routesFinalizadas.size() == 0){
                        pedirEIniciarRota();
                        Thread.sleep(5000);
                        SalvarTempos.adicionarTemposDeRota(registroDeTempos.get(registroDeTempos.size()-1));
                        cont++;   
                    }
                    else if (!car.isExisting() && routesExec.size() >= 0 ){
                        if(cont==0){
                            SalvarTempos.adicionarTemposDeRota(registroDeTempos.get(registroDeTempos.size()-1));
                            Thread.sleep(30000);
                            pedirEIniciarRota();
                            Thread.sleep(5000);
                            cont++;
                        }else {
                            cont--;
                            Thread.sleep(5000);
                        }
                    }
                    
                    if(car.getFuelTank()<3 && posto.verificarBomba()==true){
                        if(contaMotorista.getSaldo()>0){
                            car.setVelocidade(0);
                            posto.abastecerCarro(car);
                            pagarPosto();
                            }
                        car.setVelocidade(25);
                    }
                    if(posto.verificarBomba()==false){
                        car.setVelocidade(0);
                    } 
                    Thread.sleep(10);   
                    calcularTempoEdges();
                }                           
            }    
        }
        catch(Exception err){
            err.printStackTrace();
        }
    }  
}
