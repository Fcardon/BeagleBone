#!/usr/bin/python
#coding:utf-8

import os
import Methods

class Laser():
	# Initialisation du laser
	def __init__(self, nb):
		self.nb = nb

		self.state = "0"
		Methods.writeFile("/sys/class/gpio/gpio"+str(self.nb)+"/direction", "high", "w")
		self.OFF()

	def OFF(self):
		self.state = "0"
		Methods.writeFile("/sys/class/gpio/gpio"+str(self.nb)+"/value", self.state, "w")
	def ON(self):
		self.state = "1"
		Methods.writeFile("/sys/class/gpio/gpio"+str(self.nb)+"/value", self.state, "w")
	def getState(self):
		return self.state