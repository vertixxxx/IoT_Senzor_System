package com.iot.backend;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class SensorService {

    private List<SensorData> dataList = new ArrayList<>();

    public void addSensorData(SensorData data) {
        dataList.add(data);
        //Pastram 100 de inregistrari maxim
        if (dataList.size() > 100) {
            dataList.remove(0);
        }
    }

    public List<SensorData> getAllData() {
        return dataList;
    }
}