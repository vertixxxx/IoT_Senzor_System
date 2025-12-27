#include <iostream>
#include <thread>   // Pentru sleep
#include <chrono>   // Pentru timp
#include <cstdlib>  // Pentru random
#include <ctime>    // Pentru random seed

#include <nlohmann/json.hpp>
#include <mqtt/async_client.h>

using json = nlohmann::json;
using namespace std;
using namespace std::chrono;

// Configuratii MQTT
const string SERVER_ADDRESS = "tcp://localhost:1883";
const string CLIENT_ID = "Senzor_C_Plus_Plus_Client";
const string TOPIC = "casa/living/temperatura";

int main() {
    mqtt::async_client client(SERVER_ADDRESS, CLIENT_ID);

    mqtt::connect_options connOpts;
    connOpts.set_clean_session(true);

    try {
        // 2. Conectare la Broker
        cout << "Conectare la brokerul MQTT..." << endl;
        client.connect(connOpts)->wait();
        cout << "Conectat cu succes!" << endl;

        double temperaturaCurenta = 50.0; // Pornim de la 50 de grade
        srand(time(0)); // Inițializăm generatorul de numere aleatoare

        while (true) {
            temperaturaCurenta += 2.5; // Crește temperatura cu 2.5 grade la fiecare pas

            double zgomot = ((rand() % 100) / 100.0) - 0.5;
            double valoareFinala = temperaturaCurenta + zgomot;

            // Resetare
            if (temperaturaCurenta > 100.0) {
                cout << "⚠️ SUPRAINCALZIRE DETECTATA! Pornire racire de urgenta..." << endl;
                temperaturaCurenta = 50.0; // Resetăm brusc temperatura
                valoareFinala = 50.0;
            }

            json j;
            j["sensor_id"] = "Motor_Principal_Hala_1";
            j["value"] = valoareFinala;
            
            // Timestamp curent (milisecunde de la 1970)
            j["timestamp"] = duration_cast<milliseconds>(system_clock::now().time_since_epoch()).count();

            // Transformăm JSON în string
            string payload = j.dump();

            // QOS 1 = Confirmare că a ajuns măcar o dată
            client.publish(TOPIC, payload.c_str(), payload.size(), 1, false)->wait();

            cout << "Trimis: " << payload << endl;

            // Simulam citirea la fiecare 1 secundă
            this_thread::sleep_for(seconds(1));
        }

        client.disconnect()->wait();

    } catch (const mqtt::exception& exc) {
        cerr << "Eroare MQTT: " << exc.what() << endl;
        return 1;
    }

    return 0;
}