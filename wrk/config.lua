
math.randomseed(os.time())
math.random(); math.random(); math.random()

request = function()
  path = "/word?page=" .. math.random(54)  .. "&limit=4096"
  -- Return the request object with the current URL path
  return wrk.format('GET', path, {['Host'] = 'localhost'})
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
