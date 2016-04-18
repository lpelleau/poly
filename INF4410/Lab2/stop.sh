#!/bin/bash
V=${1:-2}

PID=$(ps -aux | grep rmiregistry | head -n1 | cut -d' ' -f $V)
kill -9 ${PID} 2> /dev/null
PID=$(ps -aux | grep rmiregistry | tail -n1 | cut -d' ' -f $V)
kill -9 ${PID} 2> /dev/null

for i in {1..10}
do
  PID=$(ps -aux | grep 4410.tp2 | head -n1 | cut -d' ' -f $V)
  kill -9 ${PID} 2> /dev/null
  PID=$(ps -aux | grep 4410.tp2 | tail -n1 | cut -d' ' -f $V)
  kill -9 ${PID} 2> /dev/null
done

echo $PID

