#!/usr/bin/python
#coding:utf-8

import serial

class UART(object):
	"""
	Créait un objet "UART" qui gére la communocation avec un port série.
	"""
	def __init__(self):
		"""
		Initialise le port série
		"""
		self.uart = serial.Serial(port="/dev/ttyO1", baudrate=115200, parity=serial.PARITY_NONE, stopbits=serial.STOPBITS_ONE, bytesize=serial.EIGHTBITS, timeout=0.001)

	def read(self):
		"""
		Returns:
			str -- Donnée reçu sur le port série en chaine de caractères
		"""
		datas = ""
		while 1:
			data = self.uart.read()
			if data == "\r" or data == "":
				return datas
			datas+=data;
	def write(self, datas):
		"""
		Ecrit des données sur le port série.

		Args:
			datas (str): Données à écrire.
		"""
		self.uart.write(datas)
	def inWaiting(self):
		"""
		Returns:
			int -- Nombre de données en attente dans le buffer de reception
		"""
		return self.uart.inWaiting()