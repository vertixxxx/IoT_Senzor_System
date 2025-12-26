package com.iot.backend;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SensorData {

    @JsonProperty("sensor_id") // Îi spune lui Jackson să mapeze câmpul "sensor_id" aici
    private String sensorId;

    private double value;
    private long timestamp;

    public SensorData() {}

    public String getSensorId() { return sensorId; }
    public void setSensorId(String sensorId) { this.sensorId = sensorId; }

    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "Senzor: " + sensorId + " | Temp: " + value + "°C";
    }
}