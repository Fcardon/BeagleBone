#!/usr/bin/python
#coding:utf-8

from time import sleep
from bbio import *
import serial

import Methods
from Laser import Laser
from Servo import Servo
from Shape import Shape
from Nunchuk import Nunchuk
from UART import UART
from Mode import Mode

class App():
	"""
	Démarre une application qui permet de gérer la position sur deux axes d'un laser via des servomoteurs.
	"""
	def __init__(self):
		"""
		Méthode "main" de l'application.
		"""
		print "Initialisation des péripheriques..."
		self.initPeriph()
		print "Initialisation des servos..."
		self.initServos()
		print "Initialisation terminée !"

		print "Programme prêt !"
		print ""

		#################################################
					
		self.mode = "Manual"
		self.modeObj.setMode(self.mode)

		# Création des formes
		self.shape = Shape(self.horizontalServo, self.verticalServo, self.laser, self.uart)

		try:
			while 1:
				self.reading = self.uart.read()
				if self.reading != "Up" and self.reading != "Left" and self.reading != "Right" and self.reading != "Down" and self.reading != "Center" and self.reading != "Laser" and self.reading != "":
					self.mode = self.reading
					self.modeObj.setMode(self.mode)

				if self.mode == "Auto":
					self.autoMode()
				elif self.mode == "Semi-auto":
					self.semiAutoMode()
				elif self.mode == "Manual":
					self.manualMode()
				elif self.mode == "Wii":
					if self.nunchukIsConnected:
						self.wiiMode()
					else:
						print "La manette \"Nunchuk\" n'est pas connectée"
						self.mode = "Manual"
				else:
					sleep(2)
					self.mode = "Auto"
					self.modeObj.setMode(self.mode)
		except KeyboardInterrupt:
			print "Arret du programme..."
			self.modeObj.setMode("Stop")
			self.laser.OFF()
			self.initServos()
			print "Programme arreté"

	def initPeriph(self):
		"""
		Initialise tout les périphériques servant à l'application (Laser, Servos, Manette nunchuk...) et instancie les objets globals.
		"""
		try:
			# GPIOs
			Methods.writeFile("/sys/class/gpio/export", "66", "a")
			Methods.writeFile("/sys/class/gpio/export", "69", "a")
			Methods.writeFile("/sys/class/gpio/export", "45", "a")

			# PWMs
			Methods.writeFile("/sys/devices/bone_capemgr.8/slots", "am33xx_pwm", "a")

			# UART
			Methods.writeFile("/sys/devices/bone_capemgr.8/slots", "BB-UART1", "a")

			sleep(2)

		except IOError:
			print "La configuration des périphériques a déjà été faites"

		# Création du laser
		self.laser = Laser(45)

		# Création de la gestion d'indication des modes
		self.modeObj = Mode(66, 69)

		# Création des servos
		self.verticalServo = Servo("P9_14", "10", False)
		self.horizontalServo = Servo("P9_22", "11", True)

		# Création de l'UART
		self.uart = UART()

		# Création du Nunchuk
		try:
			self.nunchuk = Nunchuk()
			self.nunchukIsConnected = True
		except IOError:
			print "Erreur de connexion avec la manette \"Nunchuk\""
			self.nunchukIsConnected = False
	def initServos(self):
		"""
		Initialise les servomoteurs à la position 0,0 et éteint le laser.
		"""
		self.horizontalServo.setPosition(0)
		self.verticalServo.setPosition(0)
		Methods.sendData(self.uart, 0, 0, self.laser.getState())

	def autoMode(self):
		"""
		Gére le mode "Auto" de l'application. Le mode "Auto" réalise une succession de toutes les formes pré-enregistré en boucle.
		"""
		while 1:
			self.laser.ON()
			print "Dessine un carré"
			self.shape.startShape("Square", 2)
			self.initServos()

			self.reading = self.uart.read()
			if self.reading == "Auto":
				self.reading = self.uart.read()
			if self.uart.inWaiting() != 0:
				break
			sleep(2)
			print "Dessine un losange"
			self.shape.startShape("Diamond", 3)
			self.initServos()

			if self.uart.inWaiting() != 0:
				break
			sleep(2)
			print "Dessine un cercle"
			self.shape.startShape("Circle", 3)
			self.initServos()
			
			if self.uart.inWaiting() != 0:
				break
			sleep(2)
			print "Dessine un infini"
			self.shape.startShape("Infinite", 3)
			self.initServos()
			sleep(2)
			break
	def semiAutoMode(self):
		"""
		Gére le mode "Semi-auto" de l'application. Le mode "Semi-auto" réalise les formes demandées via l'IHM.
		"""
		self.laser.OFF()
		horizontalPositionTable = []
		verticalPositionTable = []
		laserStateTable = []

		while 1:
			pointDatas = self.uart.read()
			if pointDatas == "Finish":
				break
			if pointDatas != "Semi-auto":
				if pointDatas == "Square":
					print "Dessine un carré"
					self.shape.startShape(pointDatas, 1)
					self.initServos()
				elif pointDatas == "Diamond":
					print "Dessine un losange"
					self.shape.startShape(pointDatas, 1)
					self.initServos()
				elif pointDatas == "Circle":
					print "Dessine un cercle"
					self.shape.startShape(pointDatas, 1)
					self.initServos()
				elif pointDatas == "Infinite":
					print "Dessine un infini"
					self.shape.startShape(pointDatas, 1)
					self.initServos()
				else:
					pointDatasTable = pointDatas.split(",")
					horizontalPositionTable.append(int(pointDatasTable[0])*2)
					verticalPositionTable.append(int(pointDatasTable[1])*2)
					laserStateTable.append(pointDatasTable[2])

		if len(horizontalPositionTable) != 0:
			print "Dessine une forme personnalisé"
			self.shape.start(horizontalPositionTable, verticalPositionTable, laserStateTable)
		self.laser.OFF()
		self.mode = "Manual"
	def manualMode(self):
		"""
		Gére le mode "Manuel" de l'application. Le mode "Manuel" réalise les mouvements simples via l'IHM (Déplacement vert le Haut, la droite, allumer le laser...).
		"""
		for i in range(100):
			self.reading = self.uart.read()
			hPos = self.horizontalServo.getPosition()
			vPos = self.verticalServo.getPosition()
			if self.reading != "":
				if self.reading == "Up":
					vPos+=2
				elif self.reading == "Left":
					hPos-=2
				elif self.reading == "Right":
					hPos+=2
				elif self.reading == "Down":
					vPos-=2
				elif self.reading == "Center":
					hPos = 0
					vPos = 0
				elif self.reading == "Laser":
					if self.laser.getState() == "0":
						self.laser.ON()
					else:
						self.laser.OFF()
				else:
					break
				
				self.horizontalServo.setPosition(hPos)
				self.verticalServo.setPosition(vPos)
				
			Methods.sendData(self.uart, self.horizontalServo.getPosition(), self.verticalServo.getPosition(), self.laser.getState())
			sleep(0.01)
	def wiiMode(self):
		"""
		Gére le mode "Wii" de l'application. Le mode "Wii" réalise les actions via la manette nunchuk.
		"""
		buttons = self.nunchuk.getButtons()
		button_c = buttons[0]
		button_z = buttons[1]

		if button_z:
			self.laser.ON()
		else:
			self.laser.OFF()

		if button_c:
			axis = self.nunchuk.getAccelerometerAxis()
			hAngle = int(axis[0]*1.8-216.0)# Min=70 ; Max=170
			vAngle = int(axis[1]*-1.8+225.0)# Min=175 ; Max=75
		else:
			position = self.nunchuk.getJoystickPosition()
			hAngle = int(position[0]*0.93-123.58)# Min=36 ; Max=229
			vAngle = int(position[1]*0.95-120.48)# Min=32 ; Max=221

		self.horizontalServo.setPosition(hAngle)
		self.verticalServo.setPosition(vAngle)
		Methods.sendData(self.uart, self.horizontalServo.getPosition(), self.verticalServo.getPosition(), self.laser.getState())
		sleep(0.01)

if __name__ == "__main__":
	App()	