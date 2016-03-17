def writeFile(path, value, writeType):
	wFile = open(path, writeType)
	wFile.write(value)
	wFile.close()

def sendPosition(uart, posH, posV):
	uart.write(str(posH)+","+str(posV)+"\r\n")