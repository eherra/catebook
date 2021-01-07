
package catebook.modules;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account extends AbstractPersistable<Long> {
    
    @NotEmpty
    @Size(min = 4, max = 30)
    private String username;
    
    @NotEmpty
    @Size(min = 4, max = 16)
    private String profileName;
    
    @NotEmpty
    @Size(min = 6)
    private String password;
    
    private Long profilePhotoId = -1L;
    
    @ManyToMany
    List<Account> friendRequests = new ArrayList();
    
    @ManyToMany
    List<Comment> wallComments = new ArrayList();   
    
    @ManyToMany
    List<Account> friends = new ArrayList();
    
    @ManyToMany
    List<Photo> albumPhotos = new ArrayList();   
}
