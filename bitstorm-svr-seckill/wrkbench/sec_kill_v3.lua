math.randomseed(os.time())

function getRandom(n)
    local t = {
        "0","1","2","3","4","5","6","7","8","9",
        "a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z",
        "A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
    }
    local s = ""
    for i =1, n do
        s = s .. t[math.random(#t)]
    end;
    return s
end;

wrk.method = "POST"
wrk.body = "{\"goodsNum\":\"abc123\", \"num\":1}"
wrk.headers["Content-Type"] = "application/json"

function request()
        wrk.headers["Trace-ID"] = getRandom(32)
        wrk.headers["User-ID"] = math.random(1000000)
        return wrk.format('POST', nil, headers, body)
end

