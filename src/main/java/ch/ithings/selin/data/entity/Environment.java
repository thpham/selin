package ch.ithings.selin.data.entity;

import ch.ithings.selin.data.entity.EnvironmentConverter;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


/**
 * An Environment JPA entity.
 *
 * @author Thomas Pham
 */
@Entity
// Environment must annotated with @DataObject because it is used as a parameter type in EnvironmentAsyncService
@DataObject(generateConverter = true)
public class Environment {

  @Id
  @GeneratedValue
  private Long id;

  private String name;

  private String creator;

  // Mandatory for JPA entities
  protected Environment() {
  }

  public Environment(String name, String author) {
    this.name = name;
    this.creator = author;
  }

  // Mandatory for data objects
  public Environment(JsonObject jsonObject) {
    EnvironmentConverter.fromJson(jsonObject, this);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCreator() {
    return creator;
  }

  public void setCreator(String creator) {
    this.creator = creator;
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    EnvironmentConverter.toJson(this, json);
    return json;
  }

  @Override
  public String toString() {
    return "Environment{" +
      "id=" + id +
      ", name='" + name + '\'' +
      ", creator='" + creator + '\'' +
      '}';
  }
}
