package catebook.objects;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WallCommentLike extends AbstractPersistable<Long> {
    
    private Long commentId;
    @ManyToMany
    private List<Account> whoLiked = new ArrayList();
    
}
