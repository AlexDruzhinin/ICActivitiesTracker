package ic.devops.uploadingfiles.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="users")
public class User {

    @Getter @Setter @Id private Long id;
    @Getter @Setter private String handle;
    @Getter @Setter private String orionEmail;
    @Getter @Setter private String name;


}
