package catebook.modules;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
    
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Account> whoLiked = new ArrayList();
    
}
