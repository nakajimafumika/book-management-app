package manpowergroup.jp.common.login.prod;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import manpowergroup.jp.common.entity.MUser;

public interface LoginUserRepository extends JpaRepository<MUser, String> {
	Optional<MUser> findByOid(String oid);

	Optional<MUser> findByName(String name);
}