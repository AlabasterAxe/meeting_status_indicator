from flask import Flask

from gpiozero import LED
from serial import Serial

app = Flask(__name__)
ser = Serial('/dev/ttyACM0', 9600)


@app.route('/')
def hello_world():
  return 'ok'

@app.route('/off')
def off():
  ser.write('f'.encode('utf-8'))
  return 'We have lift off.'

@app.route('/on')
def on():
  ser.write('n'.encode('utf-8'))
  return 'We have lift on.'

app.run(host="0.0.0.0")

