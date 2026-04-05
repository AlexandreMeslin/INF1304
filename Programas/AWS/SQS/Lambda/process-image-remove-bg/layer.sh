#!/bin/bash

rm -r python1
rm -r python2
rm process-image-remove-bg1.zip
rm process-image-remove-bg2.zip

pip install --no-deps -r requirements1.txt -t python1/
pip install --no-deps -r requirements2.txt -t python2/

zip -r process-image-remove-bg1.zip python1/
zip -r process-image-remove-bg2.zip python2/
