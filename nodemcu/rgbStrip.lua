rgbStripColor = "0,0,0"
function initializeRgbStrip() 
    ws2812.init()
    print("WS2812 initialized")
    rgbBuffer = ws2812.newBuffer(8, 3)
end

function setRgbStripColor(red, green, blue)    
    rgbBuffer:fill(green,red, blue)
    ws2812.write(rgbBuffer:dump())
    print("filled")
end