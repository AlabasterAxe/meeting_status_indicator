/*
 */

#include <Adafruit_NeoPixel.h>

#define PIN 8
#define NUMPIXELS 10
#define DELAYVAL 100  // Time (in milliseconds) to pause between pixels

Adafruit_NeoPixel pixels(NUMPIXELS, PIN, NEO_GRB + NEO_KHZ800);

char receivedChar;
boolean newData = false;
uint32_t available = pixels.Color(0, 50, 0);
uint32_t busy = pixels.Color(50, 0, 0);

// the setup function runs once when you press reset or power the board
void setup() {
  // initialize digital pin LED_BUILTIN as an output.
  Serial.begin(9600);
  Serial.println("<Arduino is ready>");
  pixels.begin();  // INITIALIZE NeoPixel strip object (REQUIRED)
}

void loop() {
  recvOneChar();
  showNewData();
}

void recvOneChar() {
  if (Serial.available() > 0) {
    receivedChar = Serial.read();
    newData = true;
  }
}

void showNewData() {
  if (newData == true) {
    pixels.clear();  // Set all pixel colors to 'off'
    Serial.print("This just in ... ");
    Serial.println(receivedChar);

    bool shouldUpdate = false;
    uint32_t newColor;

    if (receivedChar == 'n') {
      digitalWrite(LED_BUILTIN, HIGH);
      newColor = busy;
      shouldUpdate = true;
    } else if (receivedChar == 'f') {
      digitalWrite(LED_BUILTIN, LOW);
      newColor = available;
      shouldUpdate = true;
    }
    newData = false;
    if (shouldUpdate) {
      for (int i = 0; i < NUMPIXELS; i++) {  // For each pixel...

        // pixels.Color() takes RGB values, from 0,0,0 up to 255,255,255
        // Here we're using a moderately bright green color:
        pixels.setPixelColor(i, newColor);

        pixels.show();  // Send the updated pixel colors to the hardware.

        delay(DELAYVAL);  // Pause before next pass through loop
      }
    }
  }
}
