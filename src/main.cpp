#include <iostream>
#include <thread>
#include <chrono>
#include <cstdlib>
#include <ctime>
#include <mqtt/async_client.h>
#include <nlohmann/json.hpp>

using json = nlohmann::json;
using namespace std::chrono;

const std::string SERVER_ADDRESS = "tcp://localhost:1883";
const std::string CLIENT_ID = "Senzor_Temp_Windows";
const std::string TOPIC = "casa/living/temperatura";

int main() {
    std::srand(std::time(nullptr));

    mqtt::async_client client(SERVER_ADDRESS, CLIENT_ID);
    mqtt::connect_options connOpts;
    connOpts.set_clean_session(true);

    try {
        std::cout << "Incercare conectare la broker..." << std::endl;
        client.connect(connOpts)->wait();
        std::cout << "Conectat cu succes!" << std::endl;

        while (true) {
            double temperatura = 20.0 + (std::rand() % 50) / 10.0;
            
            json j;
            j["sensor_id"] = "Senzor_C++";
            j["value"] = temperatura;
            j["timestamp"] = std::time(nullptr);

            std::string payload = j.dump();
            client.publish(TOPIC, payload.c_str(), payload.size(), 1, false)->wait();

            std::cout << "Trimis: " << payload << std::endl;
            std::this_thread::sleep_for(seconds(5));
        }
    }
    catch (const mqtt::exception& exc) {
        std::cerr << "Eroare: " << exc.what() << std::endl;
        std::cerr << "NOTA: Asigura-te ca ai instalat si pornit Mosquitto Broker!" << std::endl;
        return 1;
    }
    return 0;
}