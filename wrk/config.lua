
math.randomseed(os.time())
math.random(); math.random(); math.random()

token = '__COPY__HERE__'

request = function()
  path = "/api/v1/word/-?page=" .. math.random(109)  .. "&limit=2048"
  return wrk.format(
          'GET',
          path,
          {
            ['Host'] = 'localhost',
            ["Content-Type"] = "application/json",
            ["Authorization"] = "Bearer " .. token .. ""
          })
end

response = function(status, headers, body)
  for key, value in pairs(headers) do
    if key == "Location" then
      io.write("Location header found!\n")
      io.write(key)
      io.write(":")
      io.write(value)
      io.write("\n")
      io.write("---\n")
      break
    end
  end
end
