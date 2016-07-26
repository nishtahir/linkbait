#!/bin/bash
# Downloads images of national 720 pokemon in sequential order
#
# Disclaimer:
# Official pokedex resource is http://www.pokemon.com/us/pokedex/
# All Pokémon content is © Nintendo, Game Freak, and The Pokémon Company.
#

# Note:
# Last I checked 721 is not available

_END=720
_BASE_URL=https://assets.pokemon.com/assets/cms2/img/pokedex/detail/
_EXT=.png

for i in $(seq -f "%03g" 1 $_END)
do
 curl -o ${i}${_EXT} ${_BASE_URL}${i}${_EXT}
done
