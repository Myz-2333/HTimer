# /bin/bash
# wrk -t10 -c10 -d5 -s lottery.lua http://localhost:10080/lottery/v1/get_lucky
wrk -t8 -c100 -d10s -T1s --script=lottery.lua --latency  "http://localhost:10080/lottery/v1/get_lucky"