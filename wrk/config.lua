
math.randomseed(os.time())
math.random(); math.random(); math.random()

token = 'eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwczovL3N2bi5zdS9pc3N1ZXIiLCJzdWIiOiI4Njk3ZTE1Yi1iMDdmLTQ2NDgtOGFjMi1hYmI4YmMyZjNmNDUiLCJpYXQiOjE2NzM2NTA5NTgsImV4cCI6MTY3MzY4Njk1OCwiYXVkIjoiOTNmNTJlNTktZWE4Ni00MGRmLWJhNmItNzQ2ZDIzYTczMTNlIiwianRpIjoiNWQ2Yzk5MWEtMmM1Ni00ZTAzLWIzNGItYTRlYmZhMWE1OGRhIn0.cP3XS2ThFjUdCsFy25Crso-KnQymw-7VO3RjyNIP-n0w9ePjJ0whQhkjiZr-rQDzwAK5lbSwnKYpV3RrN1Tskz6DhADRtocl1vcRgf4Id7tyAx0O320urNcjTS8FpTrRfrD1_mvMWrCebXupfNdq8zfWLpifyzXHCw7uIPBAQqw4BMLKAbmNzNw816Ah4ppEIMFCOFX3XzEopedtscZg1aSZDtS93Z6lQbddpD2OP3_rNu5_eic119Oq25nIYn02xy9lxAZ2az7w_mrwHIWzQfWhG3itTep036AKvfPZ93owuZE0RnMtjiWGwvDj3LiUpIMlNYhT4XpQphkoNFf5nQ'

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
