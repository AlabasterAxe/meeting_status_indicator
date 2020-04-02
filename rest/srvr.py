from flask import Flask
from flask import request

from waitress import serve

from gpiozero import LED
from serial import Serial
import threading

app = Flask(__name__)
ser = Serial('/dev/ttyACM0', 9600)
sources = []


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

def display_available():
  ser.write('f'.encode('utf-8'))

def display_busy():
  ser.write('n'.encode('utf-8'))

serve(app, host="0.0.0.0", port=5000)
