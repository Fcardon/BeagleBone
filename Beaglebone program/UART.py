#!/usr/bin/python
#coding:utf-8

import serial

class UART(object):
	def __init__(self):
		self.uart = serial.Serial(port="/dev/ttyO1", baudrate=115200, parity=serial.PARITY_NONE, stopbits=serial.STOPBITS_ONE, bytesize=serial.EIGHTBITS, timeout=0.001)

	def read(self):
		datas = ""
		while 1:
			data = self.uart.read()
			if data == "\r" or data == "":
				return datas
			datas+=data;

	def write(self, datas):
		self.uart.write(datas)

	def inWaiting(self):
		return self.uart.inWaiting()