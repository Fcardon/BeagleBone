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
		data = self.read()
		return data[2],data[3],data[4]

	def getButtons(self):
		data = self.read()
		butc = (data[5] & 0x02)
		butz = (data[5] & 0x01)
		return butc == 0,butz == 0