#!/usr/bin/python
#coding:utf-8

import os
import Methods

class Laser():
	"""
	Créait un objet "Laser" qui gére l'état du laser (allumé ou éteint).
	"""
	# Initialisation du laser
	def __init__(self, nb):
		"""
		Initialise le laser

		Args:
			nb (int): Numéro de la GPIO utilisé.
		"""
		self.nb = nb

		self.state = "0"
		Methods.writeFile("/sys/class/gpio/gpio"+str(self.nb)+"/direction", "high", "w")
		self.OFF()

	def setState(self, state):
		"""
		Permet d'e changer l'état du laser (allumé ou éteint).

		Args:
			state (str): État du laser. "0" pour éteint, "1" pour allumé.
		"""
		self.state = state
		Methods.writeFile("/sys/class/gpio/gpio"+str(self.nb)+"/value", self.state, "w")

	def OFF(self):
		"""
		Éteint le laser.
		"""
		self.setState("0")
	def ON(self):
		"""
		Allume le laser.
		"""
		self.setState("1")
	def getState(self):
		"""
		Returns:
			str -- L'état du laser.
		"""
		return self.state