#!/usr/bin/python
#coding:utf-8

from time import sleep
from smbus import SMBus

class Nunchuk(object):
	def __init__(self):
		self.bus = SMBus(1)
		self.bus.write_byte_data(0x52,0x40,0x00)
		sleep(0.1)

	def read(self):
		self.bus.write_byte(0x52,0x00)
		sleep(0.0001)
		temp = [(0x17 + (0x17 ^ self.bus.read_byte(0x52))) for i in range(6)]
		return temp

	def getJoystickPosition(self):
		data = self.read()
		return data[0],data[1]

	def getAccelerometerAxis(self):
		x = 0
		y = 0
		z = 0
		stability = 5

		for i in range(stability):
			data = self.read()
			x+=data[2]
			y+=data[3]
			z+=data[4]
		return x/stability,y/stability,z/stability

	def getButtons(self):
		data = self.read()
		butc = (data[5] & 0x02)
		butz = (data[5] & 0x01)
		return butc == 0,butz == 0