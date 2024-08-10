#!/bin/bash

wrk -t8 -c50 -d120s -T1s --script=create_task.lua --latency  "http://127.0.0.1:8081/demo/create_task"
