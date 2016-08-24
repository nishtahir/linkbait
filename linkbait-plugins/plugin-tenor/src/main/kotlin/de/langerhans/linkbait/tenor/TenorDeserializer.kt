package de.langerhans.linkbait.tenor

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.node.ObjectNode
import de.langerhans.linkbait.tenor.model.TenorBaseResponse
import de.langerhans.linkbait.tenor.model.TenorError
import de.langerhans.linkbait.tenor.model.TenorResponse

/**
 * Created by maxke on 24.08.2016.
 * Custom deserializer to handle both error and success objects
 */
class TenorDeserializer: StdDeserializer<TenorBaseResponse>(TenorBaseResponse::class.java) {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): TenorBaseResponse {
        val mapper = p.codec as ObjectMapper
        val obj: ObjectNode = mapper.readTree(p)
        val elementsIterator = obj.fields()

        while (elementsIterator.hasNext()) {
            val element = elementsIterator.next()
            if (element.key == "error") {
                // We found a key "error" so we can assume this is an error object
                return mapper.treeToValue(obj, TenorError::class.java)
            }
        }

        // No "error" found
        return mapper.treeToValue(obj, TenorResponse::class.java)
    }

}