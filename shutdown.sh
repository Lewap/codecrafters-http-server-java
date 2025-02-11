#!/bin/bash

pid=$(head -n 1 /tmp/MyServerPIDFile)
echo "killing "$pid
kill $pid
rm /tmp/MyServerPIDFile