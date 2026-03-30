package manpowergroup.jp.bookSystem.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import manpowergroup.jp.bookSystem.entity.BookRequestsEntity;

public interface BookRequestJpaRepository extends JpaRepository<BookRequestsEntity, Long> {

}
