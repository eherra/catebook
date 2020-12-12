
package catebook.objects;

import javax.persistence.Basic;
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
    
    private String photoText;
    
    private String name;
    private String mediaType;
    private Long size;

    @Lob
    private byte[] content;
}
