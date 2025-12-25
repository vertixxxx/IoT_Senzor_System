cmake_minimum_required(VERSION 3.10)

# --- LINIA MAGICĂ: Spunem explicit unde este scriptul vcpkg ---
# (Asigură-te că aceasta este calea corectă din PC-ul tău, bazată pe log-urile anterioare)
set(CMAKE_TOOLCHAIN_FILE "C:/Users/hsvdg/OneDrive/Documents/VSCode_Projects/Sensor_Simulator/vcpkg/scripts/buildsystems/vcpkg.cmake")

project(IoTSimulator)

set(CMAKE_CXX_STANDARD 17)

# 1. Găsim pachetele (Acum le va găsi sigur)
find_package(nlohmann_json CONFIG REQUIRED)
find_package(PahoMqttCpp CONFIG REQUIRED)

# 2. Definim executabilul
add_executable(SenzorApp src/main.cpp)

# 3. Legăm librăriile
target_link_libraries(SenzorApp PRIVATE 
    nlohmann_json::nlohmann_json 
    PahoMqttCpp::paho-mqttpp3
)