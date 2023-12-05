package io.sim;

import java.io.IOException;

import it.polito.appeal.traci.SumoTraciConnection;

public class EnvSimulator extends Thread{

    private SumoTraciConnection sumo;
    private Company company;
	private AlphaBank banco;
	private FuelEstation posto;
	
	public EnvSimulator(){
    }

    public void run(){
		
		/* SUMO */
		String sumo_bin = "sumo-gui";		
		String config_file = "map/map.sumo.cfg";
		
		// Sumo connection
		this.sumo = new SumoTraciConnection(sumo_bin, config_file);
		sumo.addOption("start", "1"); // auto-run on GUI show
		sumo.addOption("quit-on-end", "1"); // auto-close on end
		sumo.addOption("threads","4");

		try {
			sumo.runServer(12345);
			
			new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					while (!sumo.isClosed()) {
						sumo.do_timestep();
						Thread.sleep(500);////////////////////////////////////////velocidade 			
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
			
			//Itinerary
			Itinerary i1 = new Itinerary("data/dados.xml");
			
			//AlphaBank 
			banco = new AlphaBank();
			
			//Company 
			company = new Company(i1.getRotas());
			banco.addAccount(company.getContaCompany());
			
			company.setPriority(6);
			company.start();
		
			//FuelEstation 
			posto = new FuelEstation();
			banco.addAccount(posto.getContaPosto());
			posto.setPriority(4);

			company.join();
			posto.start();

			//Drivers 
			if (i1.isOn()) {
				for (int i = 1; i <= 1 ; i++) {
					Drivers d = new Drivers ("Motorista "+i, "CAR " + i, sumo, posto);
					banco.addAccount(d.getContaMotorista());
					d.setPriority(10);

					d.start();
				}
			}

			posto.join();         

			banco.setPriority(3);
			banco.start();
		
		
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

    }

}
