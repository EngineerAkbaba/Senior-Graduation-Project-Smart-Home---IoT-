broker = "m11.cloudmqtt.com"  
mqttPort = 14341
userID = "soygur@hotmail.com" 
userPWD = "omerpaylal"
macID = "18fe349e543f" 
clientID = "ESP8266-" ..  node.chipid() 
SSID = "Renesas" 
wifiPWD = "NECRenesas2006"
sf = 0
--Esp8266 internete acılıyor.
wifi.setmode(wifi.STATION)
wifi.sta.config(SSID,wifiPWD)
wifi.sta.connect()
--Mqtt client created
m = mqtt.Client(clientID, 120, userID, userPWD)
m:lwt("/lwt", clientID, 0, 0)
output={}
--Recursive connection:Esp8266 kartındaki yazılımın devamlı calismasi icin
m:on("offline", function(con)
print ("Checking MQTT Server...")
connectionCheck()

print(node.heap())
end)

--Publish data to Broker
function publishData()
if sf == 0 then
sf = 1
m:publish("channelinfo/channel1","hello",0,0, function(conn)
sf = 0
end)
end
end

function gonder()
 uart.setup(0,115200,8,0,1,0)
 m:on("message", function(conn, topic, data)
 local ch1=string.byte(data,1)
 local ch2=string.byte(data,3)
 local ch3=string.byte(data,5)
 hex1=tonumber(ch1,16)
 hex2=tonumber(ch2,16)
 hex3=tonumber(ch3,16)
 uart.write(0,170)
 uart.write(0,85)
 uart.write(0,hex1)
 uart.write(0,hex2)
 uart.write(0,hex3)
 end)
end

--Check connection of Broker
--channelinfo mqtt topicinde channel 1 2 ve 3 dataları var
function connectionCheck()
tmr.stop(5)
if wifi.sta.status() == 5 and wifi.sta.getip() ~= nil then
m:connect(broker, mqttPort, 0, function(conn)
print("connected")
m:subscribe("omer",0, function(conn) print("Subscribed Channel1")
tmr.alarm(3, 1000, 1, publishData)
end)
gonder()
end)
else
tmr.alarm(5,1000,1,connectionCheck)
print("Retry..")
end
end

tmr.alarm(5,1000,1,connectionCheck)
 