#!/usr/bin/python
#coding:utf-8

from time import sleep
from smbus import SMBus

class Nunchuk(object):
	"""
	Créait un objet "Nunchuk" qui gére l'acquisition des données de la manette nunchuk.
	"""
	def __init__(self):
		"""
		Initialise la manette nunchuk.
		"""
		self.bus = SMBus(1)
		self.bus.write_byte_data(0x52,0x40,0x00)
		sleep(0.1)

	def read(self):
		"""
		Renvoi les informations de la manette.

		Returns:
			int[] -- Tableau des différentes informations de la manette.
				[0]: Position du joystick sur l'axe horizontal.
				[1]: Position du joystick sur l'axe vertical.
				[2]: Axe X de l'accelerometre de la manette.
				[3]: Axe Y de l'accelerometre de la manette.
				[4]: Axe Z de l'accelerometre de la manette.
				[5]: État des boutons de la manette.
		"""
		self.bus.write_byte(0x52,0x00)
		sleep(0.0001)
		temp = [(0x17 + (0x17 ^ self.bus.read_byte(0x52))) for i in range(6)]
		return temp

	def getJoystickPosition(self):
		"""
		Renvoi la position du joystick de la manette.

		Returns:
			int[] -- Tableau des position du joystick de la manette.
				[0]: Position du joystick sur l'axe horizontal.
				[1]: Position du joystick sur l'axe vertical.
		"""
		data = self.read()
		return data[0],data[1]
	def getAccelerometerAxis(self):
		"""
		Renvoi les trois axes de l'accelerometre de la manette.

		Returns:
			int[] -- Tableau des trois axes de l'accelerometre de la manette.
				[0]: Axe X de l'accelerometre de la manette.
				[1]: Axe Y de l'accelerometre de la manette.
				[2]: Axe Z de l'accelerometre de la manette.
		"""
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
		"""
		Renvoi les état des boutons de la manette.

		Returns:
			int[] -- Tableau des états des boutons de la manette.
				[0]: État du bouton "C".
				[1]: État du bouton "Z".
		"""
		data = self.read()
		butc = (data[5] & 0x02)
		butz = (data[5] & 0x01)
		return butc == 0,butz == 0