#!/bin/bash
g++ `pkg-config --cflags --libs opencv` -std=c++11 main.cpp -o main
