from flask import Flask
from flask import request

from waitress import serve

from gpiozero import LED
from serial import Serial, SerialException
from serial.tools.list_ports import comports

import threading

from sys import exit

app = Flask(__name__)

possible_devices = ["/dev/ttyACM0", "/dev/ttyACM1"]
max_connect_attempts = len(possible_devices) * 2

ser = None
num_connect_attempts = 0
while ser is None and num_connect_attempts < max_connect_attempts: 
  try:
    ser = Serial(possible_devices[num_connect_attempts % len(possible_devices)], 9600, write_timeout=5)
  except SerialException:
    num_connect_attempts += 1

if ser is None:
  exit(1)

sources = []

ADAFRUIT_MANUFACTURER = "Adafruit LLC"

def send(msg):
  connected_devices = comports()
  for device in connected_devices:
    if device.manufacturer == ADAFRUIT_MANUFACTURER:
      ser = Serial(device.device, 9600, write_timeout=5)
      ser.write(msg.encode('utf-8'))
      ser.close()


@app.route('/')
def hello_world():
  return 'ok'

@app.route('/off')
def off():
  source = request.args.get('source')
  if source:
    if source in sources:
      sources.remove(source)

      if not sources: 
        display_available()
      
  
  else:
    display_available()

  return 'We have lift off.'

@app.route('/on')
def on():
  source = request.args.get('source')
  display_busy()
  
  if source and source not in sources:
    sources.append(source)

  return 'We have lift on.'

@app.after_request
def show_stats(response):
  print("These are the currently active sources:", sources)
  print("This is the number of used threads:", threading.active_count())
  return response

@app.before_request
def ack_receipt():
  print(request.url)

def display_available():
  send('f')

def display_busy():
  send('n')

serve(app, host="0.0.0.0", port=5000)
