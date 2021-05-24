/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.tss.pw.secondi.documents;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.bind.adapter.JsonbAdapter;

/**
 *
 * @author 588se
 */
public class DocumentTypeLinkAdapter implements JsonbAdapter<Document.Type, JsonObject> {

    @Override
    public JsonObject adaptToJson(Document.Type obj) throws Exception {
        System.out.println("----------------------------------------------uso adaptToJson  DocumentTypeLinkAdapter----------------------------------------------");
        return Json.createObjectBuilder()
                .add("id", obj.ordinal())
                .add("type", obj.name())
                .build();
    }

    @Override
    public Document.Type adaptFromJson(JsonObject obj) throws Exception {
         System.out.println("----------------------------------------------uso adaptFromJson DocumentTypeLinkAdapter----------------------------------------------");
        return obj.getInt("id")== -1 ? null : Document.Type.values()[obj.getInt("id")];
    }
    
}
