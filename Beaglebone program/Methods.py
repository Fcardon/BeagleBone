#!/usr/bin/python
#coding:utf-8

def writeFile(path, value, writeType):
	"""
	Permet d'écrire dans un fichier.

	Args:
		path (str): Chemin du fichier.
		value (str): Texte à écrire dans le fichier.
		writeType (str): Type d'écriture du fichier.
	"""
	wFile = open(path, writeType)
	wFile.write(value)
	wFile.close()

def sendData(uart, posH, posV, laser):
	"""
	Envoi via l'UART les informations de position et de l'état du laser.

	Args:
		uart (UART): Objet UART servant à la communication avec l'IHM.
		posH (int): Position du servo horizontal.
		posV (int): Position du servo vertical.
		laser (str): Etat du laser ("0" ou "1").
	"""
	uart.write(str(posH)+","+str(posV)+","+str(laser)+"\r")