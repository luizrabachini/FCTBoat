# -*- coding: utf-8 -*-

"""
FCTBoat Server
--------------------------------------------------
Dsc:
Activate boat motors to execute movement
(the movement to back is currently disabled)
--------------------------------------------------
"""

import socket
import sys
import RPi.GPIO as GPIO     # Package to controll raspberry pi GPIO ports

HOST = '0.0.0.0'            # Ip of service
PORT = 26000                # Port of service

SENSIBILITY = 50            # Minimun value to change GPIO state

FRONT_GPIO_CHANNEL = 11     # Channel in Raspberry to control front motor
LEFT_GPIO_CHANNEL = 13      # Channel in Raspberry to control left motor
RIGHT_GPIO_CHANNEL = 15     # Channel in Raspberry to control right motor
#BACK_GPIO_CHANNEL = 17     # Channel in Raspberry to control back motor


if  __name__ =='__main__':
    lastF = False           # Flag to indicate last command to front movement
    lastB = False           # Flag to indicate last command to back movement
    lastL = False           # Flag to indicate last command to left movement
    lastR = False           # Flag to indicate last command to right movement

    newF = False            # Flag to indicate new command to front movement
    newB = False            # Flag to indicate new command to back movement
    newL = False            # Flag to indicate new command to left movement
    newR = False            # Flag to indicate new command to right movement

    print 'Starting server...'

    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    
    try:
        sock.bind((HOST, PORT))
    except socket.error, msg:
        print 'Bind failed. Error code: ' + str(msg[0]) + '\nError message: ' + msg[1]
        sys.exit()

    print 'Socket bind complete'

    sock.listen(1)

    print 'Socket listening'

    print 'Server started [' + HOST + ':' + str(PORT) + ']'

    while 1:
        print 'Waiting client...'

        conn, addr = sock.accept()
        data = ''

        print 'Client accepted [' + addr[0] + ':' + str(addr[1]) + ']'

        GPIO.setmode(GPIO.BOARD)

    	GPIO.setup(11, GPIO.OUT)
    	GPIO.setup(13, GPIO.OUT)
    	GPIO.setup(15, GPIO.OUT)
        #GPIO.setup(17, GPIO.OUT)
 
        GPIO.output(FRONT_GPIO_CHANNEL, False)
        GPIO.output(LEFT_GPIO_CHANNEL, False)
        GPIO.output(RIGHT_GPIO_CHANNEL, False)
        #GPIO.output(BACK_GPIO_CHANNEL, False)

        while 1:
            try:
                datarec = conn.recv(1024)
                #print '> ' + datarec
                datasplit = datarec.split('&')
                parameters = datasplit[0].split(';')

                #print ':: ' + parameters[0] + ', ' + parameters[1] # Log

                difX = int(parameters[0])
                difY = int(parameters[1])

                #---------------------------------------

                # Front movement
                if (difY < -1 * SENSIBILITY):
                    newF = True
                else:
                    newF = False

                # Change front movement
                if (newF != lastF):
                    lastF = newF
                    if (lastF):
                        GPIO.output(FRONT_GPIO_CHANNEL, True)
                        print 'F' # Debug
                    else:
                        GPIO.output(FRONT_GPIO_CHANNEL, False)
                        print 'NF' # Debug

                #---------------------------------------

                # Left movement
                if(difX < -1 * SENSIBILITY):
                    newL = True
                else:
                    newL = False

                # Change left movement
                if (newL != lastL):
                    lastL = newL
                    if (lastL):
                        GPIO.output(LEFT_GPIO_CHANNEL, True)
                        print 'L' # Debug
                    else:
                        GPIO.output(LEFT_GPIO_CHANNEL, False)
                        print 'NL' # Debug

                #---------------------------------------

                # Right movement
                if(difX > SENSIBILITY):
                    newR = True
                else:
                    newR = False

                # Change right movement
                if (newR != lastR):
                    lastR = newR
                    if (lastR):
                        GPIO.output(RIGHT_GPIO_CHANNEL, True)
                        print 'R' # Debug
                    else:
                        GPIO.output(RIGHT_GPIO_CHANNEL, False)
                        print 'NR' # Debug

                #---------------------------------------

                # Back movement
                if(difY > SENSIBILITY):
                    newB = True
                else:
                    newB = False

                # Change back movement
                if (newB != lastB):
                    lastB = newB
                    if (lastB):
                        #GPIO.output(BACK_GPIO_CHANNEL, True)
                        print 'B' # Debug
                    else:
                        #GPIO.output(BACK_GPIO_CHANNEL, False)
                        print 'NB' # Debug


            except Exception, msg:
                print msg
                break         

        conn.close()
        GPIO.cleanup()

    sock.close()

GPIO.cleanup()