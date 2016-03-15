#!/usr/bin/python
#coding:utf-8

import os
from time import sleep

def writeFile(path, value, writeType):
	wFile = open(path, writeType)
	wFile.write(value)
	wFile.close()

class Servo():
	# Initialisation des servos à 0 degré
	def __init__(self, name, nb, invert):
		self.name = name
		self.nb = nb
		self.invert = invert

		writeFile("/sys/devices/bone_capemgr.8/slots", "bone_pwm_"+name, "a")
		sleep(1)
		writeFile("/sys/devices/ocp.3/pwm_test_"+name+"."+nb+"/period", "20000000", "w")
		self.setPosition(0)

	# Met à jour la position du servo selon la position donné en degrés
	def setPosition(self, position):
		if self.invert:
			position*=-1

		self.position = position
		duty = (50000/9)*position+1500000

		writeFile("/sys/devices/ocp.3/pwm_test_"+self.name+"."+self.nb+"/duty", str(duty), "w")

	# Getters and Setters
	def getName(self):
		return self.name
	def getNb(self):
		return self.nb
	def isInvert(self):
		return self.invert
	def getPosition(self):
		return self.position