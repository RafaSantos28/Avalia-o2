package io.sim;

import de.tudresden.sumo.cmd.Simulation;
import de.tudresden.sumo.cmd.Vehicle;

import it.polito.appeal.traci.SumoTraciConnection;
import de.tudresden.sumo.objects.SumoColor;
import de.tudresden.sumo.objects.SumoPosition2D;

public class Auto extends Thread {

	private String idAuto;
	private SumoColor colorAuto;
	private SumoTraciConnection sumo;

	private boolean on_off;
	private long acquisitionRate;
	private int fuelType; 			// 1-diesel, 2-gasoline, 3-ethanol, 4-hybrid
	private int fuelPreferential; 	// 1-diesel, 2-gasoline, 3-ethanol, 4-hybrid
	private double fuelPrice; 		// price in liters
	private int personCapacity;		// the total number of persons that can ride in this vehicle
	private int personNumber;		// the total number of persons which are riding in this vehicle

	
	public Auto(boolean _on_off, String _idAuto, SumoColor _colorAuto, String _driverID, SumoTraciConnection _sumo, long _acquisitionRate,
			int _fuelType, int _fuelPreferential, double _fuelPrice, int _personCapacity, int _personNumber) throws Exception {

		this.on_off = _on_off;
		this.idAuto = _idAuto;
		this.colorAuto = _colorAuto;
		this.sumo = _sumo;
		this.acquisitionRate = _acquisitionRate;
		
		if((_fuelType < 0) || (_fuelType > 4)) {
			this.fuelType = 4;
		} else {
			this.fuelType = _fuelType;
		}
		
		if((_fuelPreferential < 0) || (_fuelPreferential > 4)) {
			this.fuelPreferential = 4;
		} else {
			this.fuelPreferential = _fuelPreferential;
		}

		this.fuelPrice = _fuelPrice;
		this.personCapacity = _personCapacity;
		this.personNumber = _personNumber;
	}

	@Override
	public void run() {
		while (this.on_off) {
			try {
				Auto.sleep(this.acquisitionRate);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isOn_off() {
		return this.on_off;
	}

	public void setOn_off(boolean _on_off) {
		this.on_off = _on_off;
	}

	public long getAcquisitionRate() {
		return this.acquisitionRate;
	}

	public void setAcquisitionRate(long _acquisitionRate) {
		this.acquisitionRate = _acquisitionRate;
	}

	public String getIdAuto() {
		return this.idAuto;
	}

	public SumoTraciConnection getSumo() {
		return this.sumo;
	}

	public int getFuelType() {
		return this.fuelType;
	}

	public void setFuelType(int _fuelType) {
		if((_fuelType < 0) || (_fuelType > 4)) {
			this.fuelType = 4;
		} else {
			this.fuelType = _fuelType;
		}
	}

	public double getFuelPrice() {
		return this.fuelPrice;
	}

	public void setFuelPrice(double _fuelPrice) {
		this.fuelPrice = _fuelPrice;
	}

	public SumoColor getColorAuto() {
		return this.colorAuto;
	}

	public int getFuelPreferential() {
		return this.fuelPreferential;
	}

	public void setFuelPreferential(int _fuelPreferential) {
		if((_fuelPreferential < 0) || (_fuelPreferential > 4)) {
			this.fuelPreferential = 4;
		} else {
			this.fuelPreferential = _fuelPreferential;
		}
	}

	public int getPersonCapacity() {
		return this.personCapacity;
	}

	public int getPersonNumber() {
		return this.personNumber;
	}

	public double getDistance() throws Exception{
		double distancia = (double) sumo.do_job_get(Vehicle.getDistance(this.idAuto));
		distancia = (distancia/1000);
		return distancia;
	}

	public String getIdRoute() throws Exception{
		return sumo.do_job_get(Vehicle.getRouteID(idAuto)).toString();
	}

	public double getSpeed() throws Exception{
		double speed = (double) sumo.do_job_get(Vehicle.getSpeed(this.idAuto));
		return speed;
	}

	public double getCO2Emission() throws Exception{
		return (double) sumo.do_job_get(Vehicle.getCO2Emission(this.idAuto));
	}

	public double consumo() throws Exception{
		double consumo = (double) sumo.do_job_get(Vehicle.getFuelConsumption(this.idAuto));
		return consumo/100000;
	}

	public void setSpeed(double speed) throws Exception{
		sumo.do_job_set(Vehicle.setSpeed(idAuto, speed));

	}

	public double[] getPosicao() throws Exception{
		
			SumoPosition2D sumoPosition2D;
			sumoPosition2D = (SumoPosition2D) sumo.do_job_get(Vehicle.getPosition(this.idAuto));
			double x, y;
			x = sumoPosition2D.x;
			y = sumoPosition2D.y;

			SumoPosition2D geoLocalizacao = (SumoPosition2D) sumo.do_job_get(Simulation.convertGeo(x, y,false));
			
			return new double[]{geoLocalizacao.x, geoLocalizacao.y};
	}


}