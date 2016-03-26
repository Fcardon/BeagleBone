#!/usr/bin/python
#coding:utf-8

from time import sleep
import threading

import Methods

class Mode():
	"""
	Créait un objet "Mode" qui gére les différents mode de fonctionnement de l'application ainsi que les LEDs associées.
	"""
	def __init__(self, led1, led2):
		"""
		Initialise les GPIO des LEDs

		Args:
			led1 (int): Numéro de la GPIO de la LED 1.
			led2 (int): Numéro de la GPIO de la LED 2.
		"""
		self.led1 = led1
		self.led2 = led2
		self.mode = ""
		self.shutdownThread = threading.Event()

		Methods.writeFile("/sys/class/gpio/gpio"+str(self.led1)+"/direction", "high", "w")
		Methods.writeFile("/sys/class/gpio/gpio"+str(self.led2)+"/direction", "high", "w")

	def setMode(self, mode):
		"""
		Change le mode de l'application

		Args:
			mode (str): Nom du mode ("Manual", "Semi-auto", "Wii", "Auto" ou "Stop").
		"""
		self.mode = mode
		if mode == "Manual" or mode == "Semi-auto":
			print "Mode manuel"
			self.shutdownThread.set()
			sleep(0.7)
			Methods.writeFile("/sys/class/gpio/gpio"+str(self.led1)+"/value", "1", "w")
			Methods.writeFile("/sys/class/gpio/gpio"+str(self.led2)+"/value", "0", "w")
		elif mode == "Wii":
			print "Mode Wii"
			self.shutdownThread.set()
			sleep(0.7)
			Methods.writeFile("/sys/class/gpio/gpio"+str(self.led1)+"/value", "1", "w")
			Methods.writeFile("/sys/class/gpio/gpio"+str(self.led2)+"/value", "1", "w")
		elif mode == "Auto":
			print "Mode automatique"
			self.shutdownThread.clear()
			threading.Thread(target=self.blink).start()
		elif mode == "Stop":
			self.shutdownThread.set()
			sleep(0.7)
			Methods.writeFile("/sys/class/gpio/gpio"+str(self.led1)+"/value", "0", "w")
			Methods.writeFile("/sys/class/gpio/gpio"+str(self.led2)+"/value", "0", "w")
		else:
			print "Mode inconnu !"
	def getMode(self):
		"""
		Returns:
			str -- Le mode actuel de l'application
		"""
		return self.mode

	def blink(self):
		"""
		Fait clignoter les LEDs alternativement.
		"""
		while not self.shutdownThread.is_set():
			Methods.writeFile("/sys/class/gpio/gpio"+str(self.led1)+"/value", "0", "w")
			Methods.writeFile("/sys/class/gpio/gpio"+str(self.led2)+"/value", "1", "w")
			sleep(0.3)
			Methods.writeFile("/sys/class/gpio/gpio"+str(self.led1)+"/value", "1", "w")
			Methods.writeFile("/sys/class/gpio/gpio"+str(self.led2)+"/value", "0", "w")
			sleep(0.3)