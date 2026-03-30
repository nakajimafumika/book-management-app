package manpowergroup.jp.bookSystem.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import manpowergroup.jp.bookSystem.entity.BookListsEntity;

public interface BookListsJpaRepository extends JpaRepository<BookListsEntity, Integer> {
	boolean existsByManagementId(String managementId);
}
