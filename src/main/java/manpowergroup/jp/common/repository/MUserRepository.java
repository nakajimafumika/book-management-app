package manpowergroup.jp.common.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import manpowergroup.jp.common.entity.MUser;

public interface MUserRepository extends JpaRepository<MUser, String> {
	Optional<MUser> findByEmail(String email);
	Optional<MUser> findByOid(String oid); 
}