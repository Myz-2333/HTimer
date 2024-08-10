-- local counter = 1
-- local threads = {}
-- -- 设置thread
-- function setup(thread)
--     thread:set("id",counter)
--     thread:set("num",(counter-1)*100000000) -- num在后续的操作中可以直接使用，因为已经设置了
--     table.insert(threads,thread)
--     counter = counter + 1
-- end


function request()
--         num = num + 1
        local body1='{"user_id": '
        local body2=', "user_name": "zhangsan", "ip":"192.168.9.9"}'
        local user_id=tostring(math.random(1000000))
        local req_body=body1..user_id..body2
        wrk.body = req_body
        wrk.headers["Content-Type"] = "application/json"
        wrk.method = "POST"
        return wrk.format('POST', nil, headers, body)
end