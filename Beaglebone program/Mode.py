#!/usr/bin/python
#coding:utf-8

from time import sleep
import threading

import Methods

class Mode():
	# Initialisation du laser
	def __init__(self, led1, led2):
		self.led1 = led1
		self.led2 = led2
		self.mode = ""
		self.threadStop = True

		Methods.writeFile("/sys/class/gpio/gpio"+str(self.led1)+"/direction", "high", "w")
		Methods.writeFile("/sys/class/gpio/gpio"+str(self.led2)+"/direction", "high", "w")

	def setMode(self, mode):
		self.mode = mode
		if mode == "Manual" or mode == "Semi-auto":
			print "Mode manuel"
			self.threadStop = True
			Methods.writeFile("/sys/class/gpio/gpio"+str(self.led1)+"/value", "1", "w")
			Methods.writeFile("/sys/class/gpio/gpio"+str(self.led2)+"/value", "0", "w")
		elif mode == "Wii":
			print "Mode Wii"
			self.threadStop = True
			Methods.writeFile("/sys/class/gpio/gpio"+str(self.led1)+"/value", "1", "w")
			Methods.writeFile("/sys/class/gpio/gpio"+str(self.led2)+"/value", "1", "w")
		elif mode == "Auto":
			print "Mode automatique"
			self.threadStop = False
			threading.Thread(target=self.blink).start()
		else:
			print "Mode inconnu !"

	def getMode(self):
		return self.mode

	def blink(self):
		while 1:
			try:
				if self.threadStop:
					break
				Methods.writeFile("/sys/class/gpio/gpio"+str(self.led1)+"/value", "0", "w")
				Methods.writeFile("/sys/class/gpio/gpio"+str(self.led2)+"/value", "1", "w")
				sleep(0.3)
				if self.threadStop:
					break
				Methods.writeFile("/sys/class/gpio/gpio"+str(self.led1)+"/value", "1", "w")
				Methods.writeFile("/sys/class/gpio/gpio"+str(self.led2)+"/value", "0", "w")
				sleep(0.3)
			except KeyboardInterrupt:
				break