
package catebook.modules;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Photo extends AbstractPersistable<Long>  {
    private int likes;
    private Long ownerId;
    private String description;
    
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] content;
}
