/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.tss.pw.secondi.users;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.bind.adapter.JsonbAdapter;
import javax.ws.rs.NotFoundException;

/**
 *
 * @author 588se
 */
public class UserLinkAdapter implements JsonbAdapter<User, JsonObject> {

    @Inject
    UserStore store;

    @Override
    public JsonObject adaptToJson(User user) throws Exception {
        System.out.println("------------------------------- Sto usando UserLinkAdapter - adaptToJson -------------------------------");
        return Json.createObjectBuilder()
                .add("id", user.getId())
                .add("username", user.getUsr())
                .build();
    }

    @Override
    public User adaptFromJson(JsonObject json) throws Exception {
        System.out.println("------------------------------- Sto usando UserLinkAdapter - adaptFromJson -------------------------------");
        return store.find(Long.valueOf(json.getInt("id"))).orElseThrow(() -> new NotFoundException());
    }

    /*
    implementando JsonbAdapter<User, JsonObject> quando mi richiedono un json grazie all annotazione @JsonbTypeAdapter(UserLinkAdapter.class) sul campo User owner 
    (necessaria per il funzionamento di UserLinkAdapter) User passa prima da qua e viene ricreato il json con i campi che vogliamo noi.
    https://jakarta.ee/specifications/platform/8/apidocs/javax/json/bind/adapter/JsonbAdapter.html
     */
}

