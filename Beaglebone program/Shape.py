#!/usr/bin/python
#coding:utf-8

import os
from time import sleep

# Carré
squareHPositionTable = []
squareVPositionTable = []
# Cercle
circleHPositionTable = []
circleVPositionTable = []
# Infini
infiniteHPositionTable = []
infiniteVPositionTable = []

class Shape():
	# Initialisation des différentes formes possible
	def __init__(self, horizontalServo, verticalServo):
		self.horizontalServo = horizontalServo
		self.verticalServo = verticalServo

		self.initSquareShape()
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
	def initCircleShape(self):
		for value in range(0,30):
			circleHPositionTable.append(value)

		for value in range(-30,31):
			circleHPositionTable.append(value*-1)

		for value in range(-29,0):
			circleHPositionTable.append(value)

		for value in range(-30,31):
			circleVPositionTable.append(value*-1)

		for value in range(-29,30):
			circleVPositionTable.append(value)
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
				self.start(squareHPositionTable, squareVPositionTable)
		elif shape == "Circle":
			for i in range(0, nb):
				self.start(circleHPositionTable, circleVPositionTable)
		elif shape == "Infinite":
			for i in range(0, nb):
				self.start(infiniteHPositionTable, infiniteVPositionTable)
		else:
			return
	def start(self, horizontalPositionTable, verticalPositionTable):
		if len(horizontalPositionTable) != len(verticalPositionTable):
			print "Erreur (Shape): Les tableau des 2 servos ne sont pas de même longueur !!!"
			return

		for i in range(0,len(horizontalPositionTable)):
			self.horizontalServo.setPosition(horizontalPositionTable[i])
			self.verticalServo.setPosition(verticalPositionTable[i])
			#print "horizontalServo: "+str(self.horizontalServo.getPosition())
			#print "verticalServo: "+str(self.verticalServo.getPosition())
			sleep(0.01)