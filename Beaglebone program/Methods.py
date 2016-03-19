def writeFile(path, value, writeType):
	wFile = open(path, writeType)
	wFile.write(value)
	wFile.close()

def sendData(uart, posH, posV, laser):
	uart.write(str(posH)+","+str(posV)+","+str(laser)+"\r")