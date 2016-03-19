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

	def setState(self, state):
		self.state = state
		Methods.writeFile("/sys/class/gpio/gpio"+str(self.nb)+"/value", self.state, "w")

	def OFF(self):
		self.setState("0")
	def ON(self):
		self.setState("1")
	def getState(self):
		return self.state