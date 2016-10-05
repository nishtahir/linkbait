#!/bin/bash
for template in templates/*; do
  base=${template%.*}
  convert $template -resize 350 "${base}.png"
  rm "${base}.jpg"
done
