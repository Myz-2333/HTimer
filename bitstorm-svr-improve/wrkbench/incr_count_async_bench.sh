#!/bin/bash

wrk -t8 -c50 -d10s -T1s --script=incr_count_async.lua --latency  "http://127.0.0.1:8081/demo/incr_count_async"
