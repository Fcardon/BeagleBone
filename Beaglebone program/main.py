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

def initPeriph():
	try:
		# GPIOs
		Methods.writeFile("/sys/class/gpio/export", "45", "a")

		# PWMs
		Methods.writeFile("/sys/devices/bone_capemgr.8/slots", "am33xx_pwm", "a")

		# UART
		Methods.writeFile("/sys/devices/bone_capemgr.8/slots", "BB-UART1", "a")

		sleep(2)
	except IOError:
		print "La configuration des périphériques à déjà été faites"

	# Création du laser
	global laser
	laser = Laser(45)

	# Création des servos
	global horizontalServo
	global verticalServo
	horizontalServo = Servo("P9_14", "10", False)
	verticalServo = Servo("P9_22", "11", False)

	# Création de l'UART
	global uart
	uart = serial.Serial(port="/dev/ttyO1", baudrate=115200, parity=serial.PARITY_NONE, stopbits=serial.STOPBITS_ONE, bytesize=serial.EIGHTBITS)

	# Création du Nunchuk
	global nunchuk
	nunchuk = Nunchuk()

def initServos():
	horizontalServo.setPosition(0)
	verticalServo.setPosition(0)

print "Initialisation des péripheriques..."
initPeriph()
print "Initialisation des servos..."
initServos()
print "Initialisation terminée !"

print "Programme prêt !"
print ""

#################################################
mode = "Wii"

# Création des formes
shape = Shape(horizontalServo, verticalServo)

while 1:
	if mode == "Auto":
		print "Mode auto"
		print "Dessine un carré"
		shape.startShape("Square", 3)
		initServos()
		sleep(2)
		print "Dessine un cercle"
		shape.startShape("Circle", 3)
		initServos()
		sleep(2)
		print "Dessine un infini"
		shape.startShape("Infinite", 3)
		initServos()
		sleep(2)

	elif mode == "Manuelle":
		print "Pas encore implémenté !"

	elif mode == "Wii":
		#print "Mode Wii"
		buttons = nunchuk.getButtons()
		button_c = buttons[0]
		button_z = buttons[1]

		if button_z:
			laser.ON()
		else:
			laser.OFF()

		if button_c:
			axis = nunchuk.getAccelerometerAxis()
			hAngle = int(axis[0]*1.8-216.0)# Min=70 ; Max=170
			vAngle = int(axis[1]*-1.8+225.0)# Min=175 ; Max=75
		else:
			position = nunchuk.getJoystickPosition()
			hAngle = int(position[0]*0.92-122.31)# Min=35 ; Max=230
			vAngle = int(position[1]*0.96-120.64)# Min=32 ; Max=220

		horizontalServo.setPosition(hAngle)
		verticalServo.setPosition(vAngle)
		print "Positions: ["+str(hAngle)+","+str(vAngle)+"]"