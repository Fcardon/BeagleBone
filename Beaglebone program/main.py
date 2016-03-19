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
	horizontalServo = Servo("P9_14", "10", True)
	verticalServo = Servo("P9_22", "11", True)

	# Création de l'UART
	global uart
	uart = UART()

	# Création du Nunchuk
	global nunchuk
	nunchuk = Nunchuk()

def initServos():
	horizontalServo.setPosition(0)
	verticalServo.setPosition(0)
	Methods.sendData(uart, 0, 0, laser.getState())

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
shape = Shape(horizontalServo, verticalServo, laser, uart)

while 1:
	reading = uart.read()
	if reading != "Up" and reading != "Left" and reading != "Right" and reading != "Down" and reading != "Center" and reading != "Laser" and reading != "":
		mode = reading

	if mode == "Auto":
		laser.ON()
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

	elif mode == "Manual":
		for i in range(100):
			reading = uart.read()
			hPos = horizontalServo.getPosition()
			vPos = verticalServo.getPosition()
			if reading != "":
				if reading == "Up":
					vPos+=2
				elif reading == "Left":
					hPos-=2
				elif reading == "Right":
					hPos+=2
				elif reading == "Down":
					vPos-=2
				elif reading == "Center":
					hPos = 0
					vPos = 0
				elif reading == "Laser":
					if laser.getState() == "0":
						laser.ON()
					else:
						laser.OFF()
				else:
					break
				
				horizontalServo.setPosition(hPos)
				verticalServo.setPosition(vPos)
				
			Methods.sendData(uart, horizontalServo.getPosition(), verticalServo.getPosition(), laser.getState())
			sleep(0.01)

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
			hAngle = int(position[0]*0.93-123.58)# Min=36 ; Max=229
			vAngle = int(position[1]*0.95-120.48)# Min=32 ; Max=221

		horizontalServo.setPosition(hAngle)
		verticalServo.setPosition(vAngle)
		Methods.sendData(uart, horizontalServo.getPosition(), verticalServo.getPosition(), laser.getState())
		sleep(0.01)
	else:
		print "Mauvais mode !"
		mode = "Auto"
		sleep(2)