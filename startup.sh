#!/bin/bash

if [ -f /tmp/MyServerPIDFile ]; then
    echo "Found PID file"
    ./shutdown.sh
fi

./your_program.sh "$@" &
echo "$!" > /tmp/MyServerPIDFile