def writeFile(path, value, writeType):
	wFile = open(path, writeType)
	wFile.write(value)
	wFile.close()