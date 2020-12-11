
package casebook;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
    @Size(min = 4, max = 30)
    private String fullName;
    
    @NotEmpty
    @Size(min = 4, max = 30)
    private String profileName;
    
    @NotEmpty
    private String password;
    
    @ManyToMany
    List<Account> friendRequests = new ArrayList();
    
    @ManyToMany
    List<Comment> wallComments = new ArrayList();   
    
    @ManyToMany
    List<Account> friends = new ArrayList();
    
  
}
