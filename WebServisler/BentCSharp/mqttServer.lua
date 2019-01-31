
broker = "m11.cloudmqtt.com"  
mqttPort = 14341
userID = "soygur@hotmail.com" 
userPWD = "omerpaylal"
macID = "18fe349e543f" 
clientID = "ESP8266-" ..  node.chipid() 
count = 0 
mqttState = 0 
current_topic  = 1  
topicsub_delay = 50 
t0 = tmr.time()
SSID = "Renesas" 
wifiPWD = "NECRenesas2006"


function wifi_connect()
 wifi.setmode(wifi.STATION)
 wifi.sta.config(SSID,wifiPWD)
 wifi.sta.connect()
end


function mqtt_do()
 count = count + 1 

 if mqttState < 5 then
 mqttState = wifi.sta.status()
 wifi_connect()

 elseif mqttState == 5 then
 print("Starting to connect...")
 m = mqtt.Client(clientID, 120, userID, userPWD)

 m:on("offline", function(conn) 
 print ("Checking IoTF server...") 
 mqttState = 0 
 end)

 m:connect(broker , mqttPort, 0, 
 function(conn)
 print("Connected to " .. broker .. ":" .. mqttPort)
 mqttState = 20
 end)

 elseif mqttState == 20 then
 mqttState = 25 
 t1 = tmr.time() - t0
 if t1 > 100 then
 t1 = 0
 t0 = tmr.time()
 end 

 m:publish("omer","hello",0,0, function(conn)  
 m:subscribe("channelinfo/channel1",0,function(conn) end) -- # isareti sayesinde channelinfo nun altındaki 3 channel bilgisi geliyor. 
 print("Sent message #"..count.." data:"..t1)

 mqttState = 20 
 end)
 
 m:on("message",function(conn,topic,msg)
    print("Topic"..topic..":"..msg)
    
end)	
 else print("Waiting..."..mqttState)
 mqttState = mqttState - 1 
 end
end

tmr.alarm(2, 4000, 1, function() mqtt_do() end)