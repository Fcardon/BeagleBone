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

		# I2C
		Methods.writeFile("/sys/devices/bone_capemgr.8/slots", "BB-I2C1", "a")
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

print "Initialisation des péripheriques..."
initPeriph()
print "Initialisation terminée !"

print "Démarrage du programme"
shape = Shape(horizontalServo, verticalServo)

while 1:
	joystickPosition = nunchuk.getJoystickPosition()
	print "Buttons: ["+str(joystickPosition[0])+","+str(joystickPosition[1])+"]"