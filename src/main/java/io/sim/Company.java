package io.sim;

import java.util.ArrayList;

import io.sim.json.JSONObject;
import io.sim.servidorCliente.ClienteSocket;
import io.sim.servidorCliente.ClienteSocketHandler;
import io.sim.servidorCliente.Servidor;
import io.sim.servidorCliente.ServidorHandler;


public class Company extends Thread {

        private Account contaCompany;

        // conjunto de rotas
        private ArrayList<Route> rotas = new ArrayList<>();
        private ArrayList<Route> rotasExecucao = new ArrayList<>();
        private ArrayList<Route> rotasExecutadas = new ArrayList<>();

        //construtor da classe com as rotas
        public Company(ArrayList<Route> rotas) {
                this.rotas = rotas;
                contaCompany = new Account("Company", 4567);
                CriarRelatorio.inicializarPlanilha();
        }
        
        public Account getContaCompany() {
                return contaCompany;
        }

        // metodos array 
        public void rotasExecucao(Route rota) {
                this.rotasExecucao.add(rota);
        }
        
        public void rotasExecutadas(Route rota) {
                this.rotasExecucao.remove(rota);
                this.rotasExecutadas.add(rota);
        }

        public void pedirTransferencia(String idDriver){
            try{
                ClienteSocket cliente = new ClienteSocket();
                cliente.conectar(2000);

                JSONObject json = new JSONObject();
                json.put("tipo", "pagar motorista");
                json.put("dados", idDriver);

                cliente.enviarMensagem(json.toString());
                cliente.escutar(new ClienteSocketHandler() {
                    @Override
                    public void handle(String msg) {
                    }
              });
            }
            catch(Exception err){}
        }

        @Override
        public void run() {
            // Criar servidor
            new Servidor(5000,new ServidorHandler() { 
                @Override
                public String handle(String msg) {
                    JSONObject json = new JSONObject(msg);

                    String tipoDaMensagem = json.getString("tipo");

                    if (tipoDaMensagem.equalsIgnoreCase("rota")){
                        synchronized (rotas){
                            Route rotaAtual = rotas.remove(0);
                            rotasExecucao(rotaAtual);
                            return rotaAtual.toJSON();
                        }
                    }

                    if (tipoDaMensagem.equalsIgnoreCase("mais um KM rodado" )) {
                        String idDriver = json.getString("dados");
                        pedirTransferencia(idDriver);
                    }
                    
                    if (tipoDaMensagem.equalsIgnoreCase("gerar relatorio")){
                        DadosRelatorio dadosRelatorio = new DadosRelatorio(new JSONObject(json.getString("dados")));
                        CriarRelatorio.adicionarAoRelatorio(dadosRelatorio);
                    }

                    return msg;    
                }
            }).start();  
        } 
}
