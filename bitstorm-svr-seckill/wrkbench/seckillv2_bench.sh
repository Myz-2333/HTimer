#!/bin/bash

wrk -t8 -c100 -d30s -T1s --script=sec_kill_v2.lua --latency  "http://127.0.0.1:8001/sec_kill/v2/sec_kill"