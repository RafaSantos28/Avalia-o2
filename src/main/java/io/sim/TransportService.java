package io.sim;

import de.tudresden.sumo.cmd.Route;
import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.sumo.objects.SumoStringList;
import it.polito.appeal.traci.SumoTraciConnection;

public class TransportService extends Thread {

	private String idTransportService;
	private boolean on_off;
	private SumoTraciConnection sumo;
	private Auto auto;

	public TransportService(boolean _on_off, String _idTransportService, Auto _auto,
			SumoTraciConnection _sumo) {

		this.on_off = _on_off;
		this.idTransportService = _idTransportService;
		this.auto = _auto;
		this.sumo = _sumo;
	}

	@Override
	public void run() {
		try {
			while (this.on_off) {
				try {
					this.sumo.do_timestep();
				} catch (Exception e) {
				}
				Thread.sleep(this.auto.getAcquisitionRate());
				if (this.getSumo().isClosed()) {
					this.on_off = false;
					System.out.println("SUMO is closed...");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initializeRoute(io.sim.Route route) {

		SumoStringList edge = new SumoStringList();
		edge.clear();
		
		for (String e : route.getEdges().split(" ")) {
			edge.add(e);
		}

		String idRoute = route.getId();

		try {
			sumo.do_job_set(Route.add(idRoute, edge));
			
			sumo.do_job_set(Vehicle.addFull(this.auto.getIdAuto(), 				//vehID
											idRoute, 							//routeID 
											"DEFAULT_VEHTYPE", 					//typeID 
											"now", 								//depart  
											"0", 								//departLane 
											"0", 								//departPos 
											"0",								//departSpeed
											"current",							//arrivalLane 
											"max",								//arrivalPos 
											"current",							//arrivalSpeed 
											"",									//fromTaz 
											"",									//toTaz 
											"", 								//line 
											this.auto.getPersonCapacity(),		//personCapacity 
											this.auto.getPersonNumber())		//personNumber
					) ;
			
			sumo.do_job_set(Vehicle.setColor(this.auto.getIdAuto(), this.auto.getColorAuto()));
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public boolean isOn_off() {
		return on_off;
	}

	public void setOn_off(boolean _on_off) {
		this.on_off = _on_off;
	}

	public String getIdTransportService() {
		return this.idTransportService;
	}

	public SumoTraciConnection getSumo() {
		return this.sumo;
	}

	public Auto getAuto() {
		return this.auto;
	}
}