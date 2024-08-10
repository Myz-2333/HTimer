#!/bin/bash

wrk -t8 -c100 -d10s -T1s --script=sec_kill_v1.lua --latency  "http://127.0.0.1:8001/sec_kill/v1/sec_kill"
