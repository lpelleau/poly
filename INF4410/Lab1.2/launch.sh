#!/bin/bash
ant
cd bin/
CMD="$(ps -aux | grep rmiregistry | wc -l)"
if [ ${CMD} = "1" ] 
then
    rmiregistry &
fi
cd ..
./server &
