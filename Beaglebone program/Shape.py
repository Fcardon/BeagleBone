#!/usr/bin/python
#coding:utf-8

import math
from time import sleep
import Methods

# Carré
squareHPositionTable = []
squareVPositionTable = []
# Losange
diamondHPositionTable = []
diamondVPositionTable = []
# Cercle
circleHPositionTable = []
circleVPositionTable = []
# Infini
infiniteHPositionTable = []
infiniteVPositionTable = []

class Shape():
	# Initialisation des différentes formes possible
	def __init__(self, horizontalServo, verticalServo, laser, uart):
		self.horizontalServo = horizontalServo
		self.verticalServo = verticalServo
		self.laser = laser
		self.uart = uart

		self.initSquareShape()
		self.initDiamondShape()
		self.initCircleShape()
		self.initInfiniteShape()

	def initSquareShape(self):
		for value in range(-30,31):
			squareHPositionTable.append(value)
			squareVPositionTable.append(30)

		for value in range(-30,31):
			squareHPositionTable.append(30)
			squareVPositionTable.append(value*-1)

		for value in range(-30, 31):
			squareHPositionTable.append(value*-1)
			squareVPositionTable.append(-30)

		for value in range(-30,31):
			squareHPositionTable.append(-30)
			squareVPositionTable.append(value)
	def initDiamondShape(self):
		for value in range(0,30):
			diamondHPositionTable.append(value)

		for value in range(-30,31):
			diamondHPositionTable.append(value*-1)

		for value in range(-29,0):
			diamondHPositionTable.append(value)

		for value in range(-30,31):
			diamondVPositionTable.append(value*-1)

		for value in range(-29,30):
			diamondVPositionTable.append(value)
	def initCircleShape(self):
		for value in range(0,180):
			circleHPositionTable.append(int(math.cos(math.radians(value*-2.0+90.0))*35.0))
			circleVPositionTable.append(int(math.sin(math.radians(value*-2.0+90.0))*35.0))
	def initInfiniteShape(self):
		for value in range(-30,31):
			infiniteHPositionTable.append(value)

		for value in range(-29,30):
			infiniteHPositionTable.append(value*-1)

		for value in range(0,16):
			infiniteVPositionTable.append(value*2)

		for value in range(-14,16):
			infiniteVPositionTable.append(value*-2)

		for value in range(1,15):
			infiniteVPositionTable.append((value*2)-30)

		for value in range(0,16):
			infiniteVPositionTable.append(value*2)

		for value in range(-14,16):
			infiniteVPositionTable.append(value*-2)

		for value in range(1,15):
			infiniteVPositionTable.append((value*2)-30)

	# Démarre le dessin de la forme choisi
	def startShape(self, shape, nb):
		if shape == "Square":
			for i in range(0, nb):
				self.start(squareHPositionTable, squareVPositionTable, ["1"]*len(squareHPositionTable))
		elif shape == "Diamond":
			for i in range(0, nb):
				self.start(diamondHPositionTable, diamondVPositionTable, ["1"]*len(diamondHPositionTable))
		elif shape == "Circle":
			for i in range(0, nb):
				self.start(circleHPositionTable, circleVPositionTable, ["1"]*len(circleHPositionTable))
		elif shape == "Infinite":
			for i in range(0, nb):
				self.start(infiniteHPositionTable, infiniteVPositionTable, ["1"]*len(infiniteHPositionTable))
		else:
			return

		self.laser.OFF()

	def start(self, horizontalPositionTable, verticalPositionTable, laserStateTable):
		if len(horizontalPositionTable) != len(verticalPositionTable):
			print "Erreur (Shape): Les tableau des 2 servos ne sont pas de même longueur !!!"
			return

		for i in range(0,len(horizontalPositionTable)):
			self.horizontalServo.setPosition(horizontalPositionTable[i])
			self.verticalServo.setPosition(verticalPositionTable[i])
			self.laser.setState(laserStateTable[i])
			Methods.sendData(self.uart, horizontalPositionTable[i], verticalPositionTable[i], self.laser.getState())
			sleep(0.01)