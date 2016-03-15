#!/usr/bin/python
#coding:utf-8

import Serial
from time import sleep
from Servo import Servo
from Shape import Shape

def initPeriph():
	wFile = open("/sys/devices/bone_capemgr.8/slots", "a")
	wFile.write("am33xx_pwm")
	wFile.close()
	sleep(2)

	# Creation des servos
	global horizontalServo
	global verticalServo

	horizontalServo = Servo("P9_14", "10", False)
	verticalServo = Servo("P9_22", "11", False)


print "Initialisation des péripheriques..."
initPeriph()
print "Initialisation terminée !"

print "Démarrage du programme"
shape = Shape(horizontalServo, verticalServo)

while 1:
	print "Square"
	shape.startShape("Square", 1);
	sleep(1)
	print "Circle"
	shape.startShape("Circle", 1);
	sleep(1)
	print "Infinite"
	shape.startShape("Infinite", 1);
	sleep(1)