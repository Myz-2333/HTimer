-- body1="{\"user_id\": "
-- body2=", \"user_name\": \"zhangsan\", \"ip\":\"192.168.9.9\"}"
-- user_id=tostring(math.random(1000000))
-- body=body1..user_id..body2
-- print(body)

body1='{"user_id": '
body2=', "user_name": "zhangsan", "ip":"192.168.9.9"}'
user_id=tostring(math.random(1000000))
body=body1..user_id..body2
print(body)
