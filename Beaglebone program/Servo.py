#!/usr/bin/python
#coding:utf-8

import os
import Methods
from time import sleep

class Servo():
	"""
	Créait un objet "Servo" qui gére la position d'un servomoteur.
	"""
	# Initialisation des servos à 0 degré
	def __init__(self, name, nb, invert):
		"""
		Initialise le PWM et positionne le servomoteur à 0 degrès.

		Args:
			name (str): Nom du PWM ("P9_14", "P9_22"...).
			nb (str): Nombre après le nom du PWM.
			invert (bool): Défini si l'axe du servomoteur est inversé.
		"""
		self.name = name
		self.nb = nb
		self.invert = invert

		try:
			Methods.writeFile("/sys/devices/bone_capemgr.8/slots", "bone_pwm_"+name, "a")
			sleep(2)
			Methods.writeFile("/sys/devices/ocp.3/pwm_test_"+name+"."+nb+"/period", "20000000", "w")
			self.setPosition(0)
		except IOError:
			print "La configuration des servos a déjà été faites"

	def setPosition(self, position):
		"""
		Met à jour la position du servo selon la position donné en degrés.

		Args:
			position (int): Position en degré
		"""
		if position > 90:
			position = 90
		elif position < -90:
			position = -90

		self.position = position

		if self.invert:
			position*=-1
		duty = (50000/9)*position+1500000

		Methods.writeFile("/sys/devices/ocp.3/pwm_test_"+self.name+"."+self.nb+"/duty", str(duty), "w")

	# Getters and Setters
	def getName(self):
		"""
		Returns:
			str -- Nom du PWM utilisé
		"""
		return self.name
	def getNb(self):
		"""
		Returns:
			str -- Nombre associé au nom du PWM utilisé
		"""
		return self.nb
	def isInvert(self):
		"""
		Returns:
			str -- Vrai si l'axe est inversé
		"""
		return self.invert
	def getPosition(self):
		"""
		Returns:
			str -- Position du servomoteur
		"""
		return self.position