/**
 * Script to download information about pokemon from the
 * PokeAPI and output them in CSV format. This can then
 * be used to import them into an SQLite db using a tool
 * like SQLiteBrowser.
 *
 * <But why not just construct the DB here?> - Because effort...
 */

@Grapes([
        @Grab(group = 'com.fasterxml.jackson.core', module = 'jackson-core', version = '2.8.1'),
        @Grab(group = 'com.fasterxml.jackson.core', module = 'jackson-databind', version = '2.8.1'),
        @Grab(group = 'com.fasterxml.jackson.dataformat', module = 'jackson-dataformat-csv', version = '2.8.1')
])

import com.fasterxml.jackson.databind.ObjectWriter
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvSchema

List<Pokemon> pokemonList = []

(1..251).each { index ->

    def pokemonUrl = "http://pokeapi.co/api/v2/pokemon/$index/".toURL()
    def speciesUrl = "http://pokeapi.co/api/v2/pokemon-species/$index/".toURL()

    Pokemon pokemon = new Pokemon()

    pokemon.id = index
    pokemon.thumbnail = "https://assets.pokemon.com/assets/cms2/img/pokedex/detail/${index}.png"

    // first round - get general info
    def pokemonJson = getJsonFromUrl(pokemonUrl)

    pokemonJson.get("abilities").each { ability ->
        if (ability.get("is_hidden") == false) {
            pokemon.abilities.add(ability.get("ability").get("name"))
        }
    }

    pokemonJson.get("types").each { type ->
        pokemon.type.add(type.get("type").get("name"))
    }

    pokemon.name = pokemonJson.get("species").get("name")
    pokemon.weight = pokemonJson.get("weight")
    pokemon.height = pokemonJson.get("height")

    def speciesJson = getJsonFromUrl(speciesUrl)

    pokemon.color = speciesJson.get("color").get("name")

    speciesJson.get("flavor_text_entries").each {
        if (it.get("language").get("name") == "en" && it.get("version").get("name") == "x") {
            pokemon.description = it.get("flavor_text").replaceAll("\n|\f", " ")
        }
    }

    pokemonList.add(pokemon)
}

CsvMapper mapper = new CsvMapper()
CsvSchema schema = mapper.schemaFor(Pokemon.class)
schema = schema.withColumnSeparator(',' as char)
schema = schema.withUseHeader(true)

// output writer
ObjectWriter myObjectWriter = mapper.writer(schema)
File tempFile = new File("Pokemon.csv")
FileOutputStream tempFileOutputStream = new FileOutputStream(tempFile);
BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(tempFileOutputStream, 1024)
OutputStreamWriter writerOutputStream = new OutputStreamWriter(bufferedOutputStream, "UTF-8")
myObjectWriter.writeValue(writerOutputStream, pokemonList)

static def getJsonFromUrl(def url) {
    // Need to spoof user agent, otherwise connection is refused for some reason
    def USER_AGENT = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2700.0 Safari/537.36'
    String response = url.getText(requestProperties: ['User-Agent': USER_AGENT])
    return new JsonSlurper().parseText(response)
}

class Pokemon {

    int id = 0

    String name = ""

    int height = 0

    int weight = 0

    String color = ""

    String thumbnail = ""

    List<String> abilities = []

    List<String> type = []

    String description = ""

}
