package io.sim;

import io.sim.json.JSONObject;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DadosRelatorio {
    public String timestamp;
    public String idCar;
    public String idRoute;
    public double speed;
    public double distance;
    public double fuelConsumption;
    public String fuelType;
    public double CO2Emission;
    public double longitude;
    public double latitude;
    public String idRoad;

    public DadosRelatorio(String idCar, String idRoute, double speed, double distance, double fuelConsumption, String fuelType, double CO2Emission, double longitude, double latitude,String idRoad ){
        this.idCar = idCar;
        this.idRoute = idRoute;
        this.speed = speed;
        this.distance = distance;
        this.fuelConsumption = fuelConsumption;
        this.fuelType = fuelType;
        this.CO2Emission = CO2Emission;
        this.longitude = longitude;
        this.latitude = latitude;
        this.timestamp = String.valueOf(timestampConverter());
        this.idRoad = idRoad;
    }

    public DadosRelatorio(JSONObject json){
        this.timestamp = json.getString("timestamp");
        this.idCar = json.getString("idCar");
        this.idRoute = json.getString("idRoute");
        this.speed = json.getDouble("speed");
        this.distance = json.getDouble("distance");
        this.fuelConsumption = json.getDouble("fuelConsumption");
        this.fuelType = json.getString("fuelType");
        this.CO2Emission = json.getDouble("CO2Emission");
        this.longitude = json.getDouble("longitude");
        this.latitude = json.getDouble("latitude");
        this.idRoad = json.getString("idRoad");
    }

    public String toJSONString(){
        JSONObject obj = new JSONObject();
        obj.put("timestamp", this.timestamp);
        obj.put("idCar", this.idCar);
        obj.put("idRoute", this.idRoute);
        obj.put("speed", this.speed);
        obj.put("distance", this.distance);
        obj.put("fuelConsumption", this.fuelConsumption);
        obj.put("fuelType", this.fuelType);
        obj.put("CO2Emission", this.CO2Emission);
        obj.put("longitude", this.longitude);
        obj.put("latitude", this.latitude);
        obj.put("idRoad", this.idRoad);
        return obj.toString();
    }

        

    public LocalDateTime timestampConverter() {
        
            long timestamp = System.currentTimeMillis(); // Substitua isso pelo seu timestamp

            Instant instant = Instant.ofEpochMilli(timestamp);
            LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

            return dateTime;
    
    }
}
