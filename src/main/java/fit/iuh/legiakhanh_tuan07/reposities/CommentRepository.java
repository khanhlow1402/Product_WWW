package fit.iuh.legiakhanh_tuan07.reposities;

import fit.iuh.legiakhanh_tuan07.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
}