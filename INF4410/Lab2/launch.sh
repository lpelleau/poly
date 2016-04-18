#!/bin/bash
if [ "$#" != "2" ]
then
  echo "./launch <no_donnee> <address> [unsecured]"
  exit 1
fi
ant
cd bin/
rmiregistry 5001 &

cd ..
./master ressources/donnees-$1.txt $2 $3 >> log.txt &

sleep 1s
./slave 2 0 $2 &

sleep 1s
./slave 4 0 $2 &

sleep 1s
./slave 8 0 $2 &

sleep 4m

./stop 2
./stop 3
./stop 4
./stop 5
./stop 6
./stop 7

sleep 30s

./launch $1 $2 $3

exit 1

for i in {1..3}
do
  sleep 1s
  ./slave 10 0 $2 &
done

