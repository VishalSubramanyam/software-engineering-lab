cmake_minimum_required(VERSION 3.10)
project(assignment2)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_EXPORT_COMPILE_COMMANDS ON)

include_directories(include)
set(wxWidgets_ROOT_DIR D:/Programs/wxWidgets-3.1.5)
set(wxWidgets_CONFIGURATION mswud)
find_package(wxWidgets REQUIRED gl core base adv)
include(${wxWidgets_USE_FILE})



link_directories(lib)
link_libraries(Geographic)


add_executable(main src/application_frame.cpp src/main_app.cpp src/main_frame.cpp src/node.cpp)
target_include_directories(main PRIVATE include)