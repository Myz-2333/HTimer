wrk.method = "POST"

wrk.body = "{\"app\":\"testXtimer\",\"name\":\"测试Xtimer\",\"cron\":\"0 * * ? * *\",\"notifyHTTPParam\":{\"url\":\"http://127.0.0.1:8082/xtimer/callback\",\"method\":\"POST\",\"body\":\" its time on. this is a callback msg\"}}"

wrk.headers["Content-Type"] = "application/json"

function request()
        return wrk.format('POST',nil,headers,body)
end

