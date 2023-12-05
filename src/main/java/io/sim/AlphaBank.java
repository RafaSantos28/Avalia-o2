package io.sim;

import java.util.ArrayList;

import io.sim.json.JSONObject;
import io.sim.servidorCliente.Servidor;
import io.sim.servidorCliente.ServidorHandler;

public class AlphaBank extends Thread {

	private ArrayList<Account> contas = new ArrayList<Account>();

	public AlphaBank() { 
	}

	public void addAccount(Account conta) {
		this.contas.add(conta);
	}

	public Account getAccount() {
		for(Account conta : contas) {
			return conta;
		}
		return null;
	}

	@Override
		public void run() {
			
			// Criar servidor
			new Servidor(2000,new ServidorHandler() { 
		    private BotPayment botPayment;
			public String handle(String msg) {
				JSONObject json = new JSONObject(msg);

				String tipo = json.getString("tipo");

                if (tipo.equalsIgnoreCase("pagar motorista")){
					String idDriver = json.getString("dados");

					for(Account conta : contas) {
						if(conta.getTipoCliente().equals("Company")) {
							Account contaPagar = conta;
							for(Account conta2 : contas) {
								if(conta2.getTipoCliente().equals(idDriver)) {
									Account contaReceber = conta2;
									this.botPayment = new BotPayment(contaReceber, contaPagar, "Drivers");
									botPayment.start();
									botPayment.setPriority(5);
								}
							}
						}
					}
				};
				
                if(tipo.equalsIgnoreCase("pagar posto")){
					String idDriver =json.getString("dados");
					
                	for(Account conta : contas) {
						if(conta.getTipoCliente().equals(idDriver)) {
							Account contaPagar = conta;
							for(Account conta2 : contas) {
								if(conta2.getTipoCliente().equals("FuelStation")) {
									Account contaReceber = conta2;
									this.botPayment = new BotPayment(contaReceber, contaPagar, "FuelStation");
									botPayment.start();
									botPayment.setPriority(5);
								}
							}
						}
                	}
				}
                return msg;    
                }

            }).start();
			
		}

}
