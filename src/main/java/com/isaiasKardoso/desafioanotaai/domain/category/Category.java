package com.isaiasKardoso.desafioanotaai.domain.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import netscape.javascript.JSObject;
import org.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collation = "categories")
@Getter
@Setter
@NoArgsConstructor
public class Category {
    @Id // Id gerado automaticamente pelo MongoDB
    private String id;
    private String title;
    private String description;
    private String ownerId;

    public Category(CategoryDTO categoryDTO){
        this.title = categoryDTO.title();
        this.description = categoryDTO.description();
        this.ownerId = categoryDTO.ownerId();

    }
    @Override
    public String toString(){
        JSONObject json = new JSONObject();
        json.put("title", this.title);
        json.put("description", this.description);
        json.put("ownerId", this.ownerId);
        json.put("id", this.id);

        return json.toString();

    }

}
